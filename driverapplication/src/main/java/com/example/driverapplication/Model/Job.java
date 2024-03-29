package com.example.driverapplication.Model;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.firestore.ServerTimestamp;
import com.graphhopper.directions.api.client.model.Address;
import com.graphhopper.directions.api.client.model.GeocodingLocation;
import com.graphhopper.directions.api.client.model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Job {

    private String jobID;
    private ArrayList<CustomVehicle> vehicles=new ArrayList<>();
    private ArrayList<CustomVehicleType> vehicleTypes;
    private ArrayList<CustomService> services=new ArrayList<>();
    private GeocodingLocation pickUpLocation;
    private boolean hasBeenFulfilled=false;
    private int cost;
    public JobSolution solution;

    @ServerTimestamp
    Date time;


    public JobSolution getSolution() {
        return solution;
    }

    public void setSolution(JobSolution solution) {
        this.solution = solution;
    }

    public GeocodingLocation getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(GeocodingLocation pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Job(ArrayList<CustomVehicle> vehicles, ArrayList<CustomService> services) {
        this.vehicles = vehicles;
     this.services=services;
    }

    public Job() {
    }

    public Job(String jobID, ArrayList<CustomVehicle> vehicles, ArrayList<CustomVehicleType> vehicleTypes, ArrayList<CustomService> services, GeocodingLocation pickUpLocation, boolean hasBeenFulfilled, int cost, Date time) {
        this.jobID = jobID;
        this.vehicles = vehicles;
        this.vehicleTypes = vehicleTypes;
        this.services = services;
        this.pickUpLocation = pickUpLocation;
        this.hasBeenFulfilled = hasBeenFulfilled;
        this.cost = cost;
        this.time = time;
    }

    public boolean isHasBeenFulfilled() {
        return hasBeenFulfilled;
    }

    public void setHasBeenFulfilled(boolean hasBeenFulfilled) {
        this.hasBeenFulfilled = hasBeenFulfilled;
    }

    public String getJobID() {
        return this.jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public ArrayList<CustomVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<CustomVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<CustomService> getServices() {
        return services;
    }

    public void setServices(ArrayList<CustomService> services) {
        this.services = services;
    }

    public Call postToGraphhopper(String s, Activity activity) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,s);
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/vrp/optimize?key=c9a37a2f-6bfd-4c87-9bf9-b482e64dfbee")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        Log.e("JOB ", s);

        final Call call = client.newCall(request);

        return call;
    }

    public String buildJsonRequest(){

        JSONObject jobObject=new JSONObject();
        JSONObject vehicleJsonObject=new JSONObject();
        JSONObject serviceJsonObject=new JSONObject();
        JSONObject location=new JSONObject();
        JSONObject deliveyAddress=new JSONObject();
        JSONArray vehiclesArray=new JSONArray();
        JSONArray servicesJsonArray=new JSONArray();

        services = getServices();

        try {



                vehicleJsonObject.put("vehicle_id", "default");
                location.put("location_id", pickUpLocation.getOsmId());
                location.put("lon", pickUpLocation.getPoint().getLng());
                location.put("lat", pickUpLocation.getPoint().getLat());

                vehicleJsonObject.put("start_address", location);
                vehiclesArray.put(vehicleJsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        int i=0;

        for (Service service: services) {
            serviceJsonObject=new JSONObject();
            try {
                serviceJsonObject.put("id",service.getId()+String.valueOf(i));
                final Address delivery = service.getAddress();
                location=new JSONObject();
                location.put("location_id",String.valueOf(i)+"randomness");
                location.put("lon",delivery.getLon());
                location.put("lat",delivery.getLat());
                deliveyAddress.put("address",location);
                serviceJsonObject.put("address",location);
                servicesJsonArray.put(serviceJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            i++;
        }

        try {
            jobObject.put("vehicles",vehiclesArray);
            jobObject.put("services",servicesJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

       String jsonString=jobObject.toString();


        Log.e("services",servicesJsonArray.toString());

        return jsonString;
    }
}
