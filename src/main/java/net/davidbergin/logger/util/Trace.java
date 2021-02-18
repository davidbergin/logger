/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.util;

import java.time.Instant;

/**
 * A simple logging facade class providing a set of basic log/trace methods to
 * STDOUT. One central place to change and introduce a full-featured log stack
 * without having to change other code.
 */
public class Trace {

    /**
     * Logs the supplied message from the supplied source at INFO priority.
     * 
     * @param source  the source class calling the log statement.
     * @param message the message.
     */
    public static void info(final Class source, final String message) {
        // System.out.println(Instant.now() + " INFO [" + source.getCanonicalName() + "] - " + message);
    }

    /**
     * Logs the supplied message from the supplied source at WARN priority.
     * 
     * @param source  the source class calling the log statement.
     * @param message the message.
     */
    public static void warn(final Class source, final String message) {
        System.out.println(Instant.now() + " WARN [" + source.getCanonicalName() + "] - " + message);
    }

    /**
     * Logs the supplied message from the supplied source at ERROR priority and
     * prints the stack trace.
     * 
     * @param source  the source class calling the log statement.
     * @param message the message.
     * @param cause   the error encountered
     */
    public static void error(final Class source, final String message, final Throwable cause) {
        System.out.println(
                Instant.now() + " ERROR [" + source.getCanonicalName() + "] - " + message + " - " + cause.getMessage());
        cause.printStackTrace();
    }

    /**
     * Logs the supplied message from the supplied source at FATAL priority and
     * prints the stack trace. Also attempts to exit the application, so use
     * judiciously.
     * 
     * @param source  the source class calling the log statement.
     * @param message the message.
     * @param cause   the error encountered
     */
    public static void fatal(final Class source, final String message, final Throwable cause) {
        System.out.println(
                Instant.now() + " FATAL [" + source.getCanonicalName() + "] - " + message + " - " + cause.getMessage());
        cause.printStackTrace();
        System.exit(1);
    }

}
