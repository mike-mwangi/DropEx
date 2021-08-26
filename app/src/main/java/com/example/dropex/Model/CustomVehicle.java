package com.example.dropex.Model;



import com.graphhopper.directions.api.client.model.Vehicle;
import com.graphhopper.directions.api.client.model.VehicleType;
import com.mapbox.geojson.Point;

public class CustomVehicle extends Vehicle {
    private String driverName;
    private String vehiclePlateNumber;
    private Point driverLocation;
    private VehicleType customVehicleType;
    private int icon;

    public CustomVehicle(String driverName, String vehiclePlateNumber, Point driverLocation, VehicleType customVehicleType,int icon) {
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

    public VehicleType getCustomVehicleType() {
        return customVehicleType;
    }

    public void setCustomVehicleType(VehicleType customVehicleType) {
        this.customVehicleType = customVehicleType;
    }
}
