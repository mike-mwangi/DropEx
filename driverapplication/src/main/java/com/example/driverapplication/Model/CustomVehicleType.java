package com.example.driverapplication.Model;

import com.graphhopper.directions.api.client.model.VehicleType;

public class CustomVehicleType extends VehicleType {
    private int costPerMetre;
    private int costPerMinute;


    public CustomVehicleType(int costPerMetre, int costPerMinute) {
        this.costPerMetre = costPerMetre;
        this.costPerMinute = costPerMinute;
    }

    public int getCostPerMetre() {
        return costPerMetre;
    }

    public void setCostPerMetre(int costPerMetre) {
        this.costPerMetre = costPerMetre;
    }

    public int getCostPerMinute() {
        return costPerMinute;
    }

    public void setCostPerMinute(int costPerMinute) {
        this.costPerMinute = costPerMinute;
    }
}
