/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.format;

/**
 * Simple wrapper class to hold an instance of Activity.
 * Needed because JSON has an anonymous wrapper object around the inner activity object.
 */
public class ActivityWrapper {

    private Activity activity;

    ActivityWrapper() {

    }

    public Activity getActivity() {
        return activity;
    }
    
}
