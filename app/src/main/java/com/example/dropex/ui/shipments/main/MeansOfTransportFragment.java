package com.example.dropex.ui.shipments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dropex.Model.ConstantVehicleTypes;
import com.example.dropex.Model.CustomVehicle;
import com.example.dropex.R;
import com.graphhopper.directions.api.client.model.VehicleType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.dropex.Common.Common.currentCustomer;


public class MeansOfTransportFragment extends Fragment implements View.OnClickListener{



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView carText;

    private String mParam1;
    private String mParam2;
    private int numberOfCars;
    private int numberOfBikes;
    private int numberOfTrucks;

    public MeansOfTransportFragment() {
        // Required empty public constructor
    }



    public static MeansOfTransportFragment newInstance(String param1, String param2) {
        MeansOfTransportFragment fragment = new MeansOfTransportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_means_of_transport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int cost = MeansOfTransportFragmentArgs.fromBundle(getArguments()).getCost();
        carText=view.findViewById(R.id.explain_why_cars);
        carText.setText(cost);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.bikes:
                setBikes(numberOfBikes);
            break;
            case R.id.cars:
                setCars(numberOfCars);
            break;
            case R.id.trucks:
                setTrucks(numberOfTrucks);
                break;
        }
    }
    public void setCars(int numberOfCars){
        ArrayList<CustomVehicle> vehicles=new ArrayList<>();
        ArrayList<VehicleType> vehicleTypes=new ArrayList<>();
        for (int i=0;i<numberOfCars;i++){
            CustomVehicle vehicle=new CustomVehicle();
            vehicle.setVehicleId("default"+i);
            vehicle.setCustomVehicleType(ConstantVehicleTypes.getCarVehicleType());
            vehicles.add(vehicle);
        }
        currentCustomer.getCurrentJob().setVehicles(vehicles);
        goToMaps();

    }
    public void setBikes(int numberOfBikes){
        ArrayList<CustomVehicle> vehicles=new ArrayList<>();
        ArrayList<VehicleType> vehicleTypes=new ArrayList<>();
        for (int i=0;i<numberOfBikes;i++){
            CustomVehicle vehicle=new CustomVehicle();
            vehicle.setVehicleId("default"+i);
            vehicle.setCustomVehicleType(ConstantVehicleTypes.getBikeVehicleType());
            vehicles.add(vehicle);
        }
        currentCustomer.getCurrentJob().setVehicles(vehicles);
        goToMaps();

    }
    public void setTrucks(int numberOfTrucks){
        ArrayList<CustomVehicle> vehicles=new ArrayList<>();
        ArrayList<VehicleType> vehicleTypes=new ArrayList<>();
        for (int i=0;i<numberOfTrucks;i++){
            CustomVehicle vehicle=new CustomVehicle();
            vehicle.setVehicleId("default"+i);
            vehicle.setCustomVehicleType(ConstantVehicleTypes.getVanVehicleType());
            vehicles.add(vehicle);
        }
        currentCustomer.getCurrentJob().setVehicles(vehicles);
        goToMaps();

    }
    public void goToMaps(){
        final String s = currentCustomer.getCurrentJob().buildJsonRequest();
        currentCustomer.getCurrentJob().postToGraphhopper(s, this.getActivity());
        currentCustomer.getJobs().add(currentCustomer.getCurrentJob());
    }
}