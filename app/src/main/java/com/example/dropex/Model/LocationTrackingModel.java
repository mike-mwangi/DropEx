package com.example.dropex.Model;

public class LocationTrackingModel {
    private int driverID;
    private int vehicleType;
    private double latitude;
    private double longitude;
    private double lastUpdatedTime;

    public LocationTrackingModel(int driverID, int vehicleType, double latitude, double longitude, double lastUpdatedTime) {
        this.driverID = driverID;
        this.vehicleType = vehicleType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public LocationTrackingModel() {
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(double lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
