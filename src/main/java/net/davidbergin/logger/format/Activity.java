/*
 * Copyright (c) 2021 David Bergin
 */
package net.davidbergin.logger.format;

import java.util.Date;

/**
 * Simple POJO to represent an activity record in the XML and JSON input formats.
 * Contains a union of all the fields.
 */
public class Activity {

        private String userName;
        private String websiteName;
        private String activityTypeDescription;
        private int activityTypeCode;
        private Date signedInTime;
        private int number_of_views;

        public Activity(String userName, String websiteName, String activityTypeDescription, int activityTypeCode, Date signedInTime, int number_of_views) {
                this.userName = userName;
                this.websiteName = websiteName;
                this.activityTypeDescription = activityTypeDescription;
                this.activityTypeCode = activityTypeCode;
                this.signedInTime = signedInTime;
                this.number_of_views = number_of_views;
        }

        public String getUserName() {
                return userName;
        }

        public String getWebsiteName() {
                return websiteName;
        }

        public String getActivityTypeDescription() {
                return activityTypeDescription;
        }

        public int getActivityTypeCode() {
                return activityTypeCode;
        }

        public Date getSignedInTime() {
                return signedInTime;
        }

        public int getNumber_of_views() {
                return number_of_views;
        }
        
}
