package com.example.dropex.Model;

import com.graphhopper.directions.api.client.JSON;
import com.graphhopper.directions.api.client.model.Shipment;
import com.graphhopper.directions.api.client.model.Vehicle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Job {
    private String jobID;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Shipment> shipments;

    public Job(ArrayList<Vehicle> vehicles, ArrayList<Shipment> shipments) {
        this.vehicles = vehicles;
        this.shipments = shipments;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(ArrayList<Shipment> shipments) {
        this.shipments = shipments;
    }

    public String postToGraphhopper(String s) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,s);
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/vrp/optimize?key=c9a37a2f-6bfd-4c87-9bf9-b482e64dfbee")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String jsonData = response.body().string();
                JSONObject Jobject = null;
                try {
                    Jobject = new JSONObject(jsonData);
                    jobID= (String) Jobject.get("job_id");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return jobID;
    }
}
