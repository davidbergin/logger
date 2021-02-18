/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.handler;

import net.davidbergin.logger.util.Config;
import net.davidbergin.logger.util.Trace;

/**
 * A public Factory class for this package, providing a simple getInstance() API to get the required Handlier for a fileName.
 * This class uses the HandlerCache to avoid unneccessary objects being creates, and returns handlers from the cache.
 */
public class HandlerFactory {

    private static final HandlerCache HANDLER_CACHE = new HandlerCache();

    /**
     * Gets the appropriate instance of a Handler which matches the filename (based on extension).
     * @param fileName the file name to get a matching handler for.
     * @return Handler instance from the cache appropriate to the file name.
     */
    public static Handler getInstance(String fileName) {

        Trace.info(HandlerFactory.class, "Getting handler for " + fileName);

        try {

            final String extension = getFileExtension(fileName);

            if (extension != null && extension.length() > 0) {

                final String handlerName = Config.instance().getString("logger.handler." + extension);

                if (handlerName == null) {

                    Trace.warn(HandlerFactory.class, "No handler found for " + fileName);

                } else {

                    Handler handler = HANDLER_CACHE.get(extension);

                    if (handler == null) { // as it will be the first time

                        handler = (Handler) Class.forName(handlerName).getConstructor().newInstance();
                        HANDLER_CACHE.put(extension, handler);
                        handler = HANDLER_CACHE.get(extension); // always get the cached instance, to avoid leaking

                    }
                    
                    Trace.info(HandlerFactory.class, "Got handler for " + fileName + " - " + handler.getClass());
                    return handler;

                }


            } else {
                Trace.warn(Handler.class, "No extension found on " + fileName);
            }

        } catch (Exception e) {
            Trace.error(HandlerFactory.class, "Error occurred getting handler for " + fileName, e); //Don't propagate as not a big issue if unknown file type encountered
        }

        return null;

    }

    // Gets the file extension from the filename, or null if there isn't one.
    private static String getFileExtension(String fileName) {

        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex > -1) {
            return fileName.substring(extensionIndex + 1).toLowerCase();
        } else {
            return null;
        }
        
    }

}
