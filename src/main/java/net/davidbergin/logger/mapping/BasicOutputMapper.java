/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.mapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.davidbergin.logger.format.Activity;
import net.davidbergin.logger.format.BasicOutputFormat;
import net.davidbergin.logger.util.Trace;

/**
 * A Singleton class with responsibility for mapping our input format an
 * {@link Activity} instance produced from either XML or JSON input to an instance of
 * {@link BasicOutputFormat} - which can easily be represented in output JSON form. It
 * caches the contents of a file named activity.csv, which is supplied from the
 * current directory and uses this to help transforming some input.
 */
public class BasicOutputMapper {

    private static final BasicOutputMapper INSTANCE = new BasicOutputMapper();
    private static final String MAPPER_CONFIG_FILE = "activity.csv";
    private Map<Integer, String> activityTransformer = new ConcurrentHashMap<>();

    /**
     * Private constructor for singleton creation which loads the mapping file and
     * caches the result.
     */
    private BasicOutputMapper() {

        try {

            List<String> lines = Files.readAllLines(Path.of(MAPPER_CONFIG_FILE));

            for (String line : lines) {
                String[] tokens = line.split(",");
                if (tokens.length == 2) {// assuming this is a well formed csv, but just in case skip any lines that don't correspond
                    int key = Integer.parseInt(tokens[0]);
                    String value = tokens[1];
                    activityTransformer.put(key, value);
                }
            }

        } catch (Exception e) {
            Trace.fatal(BasicOutputMapper.class, "Failed to load mapping file.", e);
        }

    }

    /**
     * Gets the singleton instance of {@link BasicOutputMapper}
     * @return the instance of {@link BasicOutputMapper}
     */
    public static BasicOutputMapper instance() {
        return INSTANCE;
    }

    /**
     * Maps an instance of {@link Activity} to an instance of {@link BasicOutputFormat}
     * @param app the instance of {@link Activity}
     * @return the mapped instance of {@link BasicOutputFormat}
     */
    public BasicOutputFormat map(final Activity app) {

        final String user = app.getUserName();
        final String website = app.getWebsiteName();
        final String activityTypeDescription = (app.getActivityTypeDescription() != null
                ? app.getActivityTypeDescription()
                : activityTransformer.get(app.getActivityTypeCode()));
        final Date signedInTime = app.getSignedInTime();

        return new BasicOutputFormat(user, website, activityTypeDescription, signedInTime);

    }

}
