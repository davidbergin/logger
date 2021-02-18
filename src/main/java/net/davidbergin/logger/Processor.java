/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.davidbergin.logger.handler.Handler;
import net.davidbergin.logger.handler.HandlerFactory;
import net.davidbergin.logger.util.Config;
import net.davidbergin.logger.util.Trace;

/**
 * Main processing class in the application. Instantiating and calling run (with
 * optional arguments) will start processing. Files will be read and processed
 * concurrently, in a size-limited threadpool. The converted output is piped to
 * a concurrent blocking queue. The output writer then reads from this queue and
 * pushes updates to the output file in a thread-safe manner.
 */
public class Processor {

    private static final String DEFAULT_INPUT_DIRECTORY = ".";
    private static final String DEFAULT_OUTPUT_FILE = "./output.txt";

    public static void main(String[] args) {
        final int code = new Processor().run(args);
        System.exit(code);
    }

    /**
     * Runs the logger processor and returns a code indicating overall success or
     * failure.
     * 
     * @param args containing optional two arguments <input-directory> and
     *             <output-file> - in that order.
     * @return 0 for a successful run or 1 in case of an unhandled Exception.
     */
    int run(String[] args) {

        final String input;
        if (args.length > 0) {
            input = args[0];
        } else {
            input = DEFAULT_INPUT_DIRECTORY;
        }

        final String output;
        if (args.length > 1) {
            output = args[1];
        } else {
            output = DEFAULT_OUTPUT_FILE;
        }

        try {
            new Processor().process(input, output);
            return 0;
        } catch (Exception e) {
            Trace.error(Processor.class, "An error happened", e);
            return 1;
        }
    }

    /**
     * Process the input directory. All files which can be mapped to a handler are then converted to the output format.
     * This method orchestrates and separates the input and output phases.
     * Input processing is handled by an executor service with a configurable thread pool. Files are processed in parallel.
     * Processed output is pushed to a concurrent {@link BlockingQueue}.
     * Output processing pulls from the queue in a single-threaded manner and will wait until all inputs are done before completing.
     * 
     * @param inputDir the director of input files
     * @param outputFile the output file name
     * @throws IOException
     */
    private void process(String inputDir, String outputFile) throws IOException {

        final long start = System.currentTimeMillis();
        Trace.warn(Processor.class, "Started processing - millis - " + start);

        final int inputThreads = Config.instance().getInteger("logger.input.threads", 1);
        Trace.info(Processor.class, "Running with " + inputThreads + " threads");

        final ExecutorService executorService = Executors.newFixedThreadPool(inputThreads);
        final BlockingQueue<String> outputQueue = new LinkedBlockingDeque<>();

        handleInput(inputDir, executorService, outputQueue);
        generateOutput(outputFile, executorService, outputQueue);

        Trace.warn(Processor.class, "Completed processing - elapsed millis - " + (System.currentTimeMillis() - start));

    }

    /**
     * Process input with the executor service with a configurable thread pool. Files are processed in parallel.
     * Processed output is pushed to a concurrent {@link BlockingQueue}.
     */
    private void handleInput(final String inputDir, final ExecutorService executorService, final BlockingQueue<String> outputQueue) throws IOException {

        final Path path = Paths.get(inputDir);

        // Get all files in the directory - ignoring directories
        List<File> files = Files.list(path)
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .collect(Collectors.toList());

        Trace.info(Processor.class, "Found " + files.size() + " files to process.");

        for (File file : files) {

            Callable<String> callableTask = () -> {
                final String content = Files.readString(file.toPath());
                final Handler handler = HandlerFactory.getInstance(file.getName());
                if (handler != null) {
                    final String result = handler.handle(content);
                    if (result != null) { //can't put null on queue - and no point anyway
                        outputQueue.offer(result);
                    }
                    return result;
                }
                return null;
            };

            executorService.submit(callableTask);

        }

        executorService.shutdown();

    }

    /**
     * Generate output by reading from the outputQueue.
     * Output is done once this queue is empty and the input processor is also done.
     * Output is written to the outputFile name provided.
     */
    private void generateOutput(final String outputFile, final ExecutorService executorService, final BlockingQueue<String> outputQueue) {

        try (PrintWriter writer = new PrintWriter(new File(outputFile))) {

            while (!executorService.isTerminated() || outputQueue.size() > 0) {
                String output = outputQueue.poll(10, TimeUnit.SECONDS);
                if (output != null) {
                    writer.println(output);
                }
            }

        } catch (Exception e) {
            Trace.error(Processor.class, "problem waiting for completion.", e);
        }

    }

}
