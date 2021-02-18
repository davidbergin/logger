/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.format;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Simple POJO which holds the values for the simple JSON output format.
 * Constructing an instance and calling {@link toJSONString} will produce the desired output.
 */
public class BasicOutputFormat {
    // {"user" : "Sam","website" : "abc.com","activityTypeDescription" :
    // "Viewed","signedInTime" : "2020-01-13 00:00:00"}
    private String user;
    private String website;
    private String activityTypeDescription;
    private Date signedInTime;

    /**
     * Create a record with the default format
     * 
     * @param user
     * @param website
     * @param activityTypeDescription
     * @param signedInTime
     */
    public BasicOutputFormat(String user, String website, String activityTypeDescription, Date signedInTime) {
        this.user = user;
        this.website = website;
        this.activityTypeDescription = activityTypeDescription;
        this.signedInTime = signedInTime;
    }

    /**
     * Converts this POJO to the expected JSON representation.
     * @return a representation of this POJO in JSON.
     */
    public String toJSONString() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

}
