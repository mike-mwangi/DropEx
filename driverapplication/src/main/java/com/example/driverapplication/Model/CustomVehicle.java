package com.example.driverapplication.Model;


import com.graphhopper.directions.api.client.model.Vehicle;
import com.graphhopper.directions.api.client.model.VehicleType;
import com.mapbox.geojson.Point;

public class CustomVehicle extends Vehicle {
    private String driverID;
    private String vehiclePlateNumber;

    private VehicleType vehicleType;

    public CustomVehicle(String driverName, String vehiclePlateNumber, VehicleType vehicleType) {
        this.driverID = driverName;
        this.vehiclePlateNumber = vehiclePlateNumber;

        this.vehicleType=vehicleType;

    }

    public CustomVehicle() {
    }



    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverName) {
        this.driverID = driverName;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }


    public VehicleType getCustomVehicleType() {
        return vehicleType;
    }

    public void setCustomVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
