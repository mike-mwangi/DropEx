package com.example.driverapplication.ApiDataFetchers.Solution;

public class FetchSolutionConfig {

    final String jobId;
    final String vehicleId;

    public FetchSolutionConfig(String jobId, String vehicleId) {
        this.jobId = jobId;
        this.vehicleId = vehicleId;
    }
}