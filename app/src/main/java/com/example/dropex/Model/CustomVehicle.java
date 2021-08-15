package com.example.dropex.Model;



import com.graphhopper.directions.api.client.model.Vehicle;
import com.mapbox.geojson.Point;

public class CustomVehicle extends Vehicle {
    private String driverName;
    private String vehiclePlateNumber;
    private Point driverLocation;
    private CustomVehicleType customVehicleType;
    private int icon;

    public CustomVehicle(String driverName, String vehiclePlateNumber, Point driverLocation, CustomVehicleType customVehicleType,int icon) {
        this.driverName = driverName;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.driverLocation = driverLocation;
        this.customVehicleType = customVehicleType;
        this.icon=icon;
    }

    public CustomVehicle() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public Point getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(Point driverLocation) {
        this.driverLocation = driverLocation;
    }

    public CustomVehicleType getCustomVehicleType() {
        return customVehicleType;
    }

    public void setCustomVehicleType(CustomVehicleType customVehicleType) {
        this.customVehicleType = customVehicleType;
    }
}
