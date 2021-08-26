package com.example.dropex.Model;

public class PostedJob {
    private String jobID;
    private String userID;
    private String pickUpLocation;

    public PostedJob() {
    }

    public PostedJob(String jobID, String userID, String pickUpLocation) {
        this.jobID = jobID;
        this.userID = userID;
        this.pickUpLocation = pickUpLocation;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
