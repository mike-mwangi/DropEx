package com.example.driverapplication;

import android.app.Application;

import com.example.driverapplication.Model.DriverModel;

public class DriverClient extends Application {
    public DriverModel getDriver() {
        return driver;
    }

    public void setDriver(DriverModel driver) {
        this.driver = driver;
    }

    private DriverModel driver;


}
