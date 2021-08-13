package com.example.dropex.Model;

import com.example.dropex.ui.shipments.main.AutoCompleteAdapter;
import com.graphhopper.directions.api.client.model.VehicleType;

import java.util.ArrayList;
import java.util.List;

public abstract class ConstantVehicleTypes {


    public static CustomVehicleType getBikeVehicleType(){
        CustomVehicleType bike=new CustomVehicleType(20,13);
        List<Integer> capacity= new ArrayList<>();
        capacity.add(5);
        capacity.add(5);
        bike.setCapacity(capacity);
        bike.setProfile(VehicleType.ProfileEnum.BIKE);
        return bike;
    }
    public static CustomVehicleType getVanVehicleType(){
        CustomVehicleType bike=new CustomVehicleType(20,13);
        List<Integer> capacity= new ArrayList<>();
        capacity.add(1000);
        capacity.add(1000);
        bike.setCapacity(capacity);
        bike.setProfile(VehicleType.ProfileEnum.SMALL_TRUCK);
        return bike;
    }
    public static CustomVehicleType getCarVehicleType(){
        CustomVehicleType bike=new CustomVehicleType(20,13);
        List<Integer> capacity= new ArrayList<>();
        capacity.add(500);
        capacity.add(500);
        bike.setCapacity(capacity);
        bike.setProfile(VehicleType.ProfileEnum.CAR);
        return bike;
    }


}
