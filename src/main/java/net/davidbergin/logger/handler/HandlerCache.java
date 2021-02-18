/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Package scoped cache to hold instances of Handler objects keyed by fileExtension.
 * To avoid cache churn, putting an object will only succeed if there isn't already a mapping for they key.
 */
class HandlerCache {
    
    private Map<String,Handler> cache = new ConcurrentHashMap<>();

    /**
     * Gets the matching Handler for the file extension supplied.
     * @param fileExtension the file extension
     * @return the matching Handler if one is found, null otherwise.
     */
    Handler get(String fileExtension) {
        return cache.get(fileExtension);
    }

    /**
     * Puts a new mapping for this Handlier and file extension into the cache if one is not already present.
     * Does nothing if there is already a mapping.
     * @param fileExtension the file extension
     * @param handler the Handler instance
     */
    void put (String fileExtension, Handler handler) {
        cache.putIfAbsent(fileExtension, handler);
    }

}
