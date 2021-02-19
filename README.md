# logger
A software tool which reads supported file types from a directory and converts to the desired output form. Input processing is parallel, and output processing is single-threaded to a file.

## Building
Clone or download the repo from here.
On a system with Maven and Java 8+ installed, from the root of the project run:

`mvn clean package`

This will pull down dependencies, compile, test and package the tool.


## Running
The tool is built as a SpringBoot application, so can be simply run by calling:

`java -jar target/logger-0.1-SNAPSHOT.jar`

By default, the tool will read the current directory for input files, and output any files it can convert to a file named output.txt.

Additional arguments can be supplied to the tool (in specified order):

`java -jar target/logger-0.1-SNAPSHOT.jar inputDirectory/ outputFileName`


## Files types and extending the tool
The tool identifies files based on the extension only. The extension allows the `HandlerFactory` to give the appropiate Handler instance back to the application.

To add support for a different file type, a new implementation of the `Handler` interface would be written and supporting configuration to bind this implementation added to `app.properties`

The use of SpringBoot as a packaging and runtime framework would allow the easy addition of a REST endpoint, for enquiring on processing status - something which is useful across a fleet of instances in a production context.


## Logging
Logging is very simple and is provided by the `Trace` class. Logs go to STDOUT.


## Performance and scalability
The tool uses a thread pool to concurrently process input files, and the size of this pool is controlled by the `logger.input.threads` property in the embedded `app.properties` confguration file.

Altering this value to take best advantage of the CPU, memory and I/O capacity of your system will give the best throughput.

For larger workloads, multiple copies of this tool would run, potentially on different machines, each reading from a distinct input directory and producing a distinct output file, for later combination.

The tool was timed on a 6-core system processing 8000 files in 2.3 seconds. XML file processing is significantly more expensive than JSON processing.


## Patterns used
### Singleton
- Used for the central `Config` cache which holds the tool configuration.
- Used for the `Mapper` singleton object.

### Factory
- Used in `HandlerFacory` to construct and cache the appropriate Handler object for a file type.

### Facade
- Simple use of a facade in `Trace` for the trace logging. The implementation is naive, but as all trace logging calls go through the facade, it will allow simple retrofitting of log4j or other configurable logging engines.
