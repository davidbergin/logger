/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.handler;

import net.davidbergin.logger.util.Trace;

/**
 * Base interface for file type handlers to extend.
 * Handling file content involves two phases - validate and transform.
 * If validation succeeds, then the content is transformed using a mapper class to the desired output format.
 * The default handle method orchestrates the phases, and centrally logs any errors.
 * A file which cannot be handled is just logged, and the exception does not propagate.
 */
public interface Handler {

    /**
     * Validate and transform the content to an output format.
     * @param content the input format.
     * @return the transformed output format or null if the input format cannot be transformed.
     */
    default String handle(String content) {
        
        try {
            if (validate(content)) {
                return transform(content);
            }            
        } catch (Exception e) {
            Trace.error(this.getClass(), "Error occurred handling content " + content, e);
        }
        return null;
    }

    /**
     * Validates the input content against a schema, if supported by this Handler implementation.
     * @param content the input format.
     * @return true if the content passed validation, false otherwise.
     * @throws Exception if an error occurred.
     */
    boolean validate(String content) throws Exception;

    /**
     * Transforms the input format to the output format.
     * @param content the input format.
     * @return the transformed output format.
     */
    String transform(String content);
    
}
