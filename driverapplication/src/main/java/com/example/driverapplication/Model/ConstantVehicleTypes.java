package com.example.driverapplication.Model;

import com.graphhopper.directions.api.client.model.VehicleType;

import java.util.ArrayList;
import java.util.List;

public abstract class ConstantVehicleTypes {


    public static VehicleType getBikeVehicleType(){
        VehicleType bike=new VehicleType();
        List<Integer> capacity= new ArrayList<>();
        capacity.add(5);
        capacity.add(5);
        bike.setCapacity(capacity);
        bike.setCostPerMeter(10.0);
        bike.setCostPerSecond(0.7);
        bike.setProfile(VehicleType.ProfileEnum.BIKE);
        return bike;
    }
    public static VehicleType getVanVehicleType(){
        VehicleType bike=new VehicleType();
        List<Integer> capacity= new ArrayList<>();
        capacity.add(1000);
        capacity.add(1000);
        bike.setCapacity(capacity);
        bike.setCostPerMeter(18.0);
        bike.setCostPerSecond(0.7);
        bike.setProfile(VehicleType.ProfileEnum.SMALL_TRUCK);
        return bike;
    }
    public static VehicleType getCarVehicleType(){
        VehicleType bike=new VehicleType();
        List<Integer> capacity= new ArrayList<>();
        capacity.add(500);
        capacity.add(500);
        bike.setCostPerMeter(15.0);
        bike.setCostPerSecond(0.7);
        bike.setCapacity(capacity);
        bike.setProfile(VehicleType.ProfileEnum.CAR);
        return bike;
    }


}
