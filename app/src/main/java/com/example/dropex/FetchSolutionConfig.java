package com.example.dropex;

public class FetchSolutionConfig {

    final String jobId;
    final String vehicleId;

    public FetchSolutionConfig(String jobId, String vehicleId) {
        this.jobId = jobId;
        this.vehicleId = vehicleId;
    }
}