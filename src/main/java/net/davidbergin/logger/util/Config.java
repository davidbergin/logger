/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.util;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/** Singleton configuration cache, which loads the logger configuration from a properties file on the classpath. */
public class Config {

    private static final String PROPERTIES_FILE = "app.properties";
    private static final Config INSTANCE = new Config();
    private final Properties properties;

    /**
     * Private contructor for singleton initialisation.
     */
    private Config() {
        properties = new Properties();
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            // log and exit if properties cannot be loaded
            Trace.fatal(Config.class, "could not load properties from " + PROPERTIES_FILE, e);
        }
        Trace.info(Config.class, "loaded properties - " + properties);
    }

    /**
     * Returns the singleton instance of {@link Config}
     * @return the instance of {@link Config}
     */
    public static Config instance() {
        return INSTANCE;
    }

    /**
     * Gets a String value for the specified key, or null if it doesn't exist in the properties.
     * @param key the key
     * @return a value for the supplied key, or null if it doesn't exist
     */
    public String getString(final String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a String value for the specified key, or the default value if it doesn't exist in the properties.
     * @param key the key
     * @param defaultVal the default value to return if no mapping is found
     * @return a value for the supplied key, or the default value if it doesn't exist
     */
    public String getString(final String key, final String defaultVal) {
        return Optional.ofNullable(properties.getProperty(key)).orElse(defaultVal);
    }

    /**
     * Gets an Integer value for the specified key, or the default value if it doesn't exist in the properties.
     * @param key the key
     * @param defaultVal the default value to return if no mapping is found
     * @return a value for the supplied key, or the default value if it doesn't exist
     */
    public int getInteger(final String key, final int defaultVal) {
        return Optional.ofNullable(Integer.parseInt(properties.getProperty(key))).orElse(defaultVal);
    }
    
}
