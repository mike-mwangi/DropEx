package com.example.dropex.Model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.dropex.NavigationLauncherActivity;
import com.example.dropex.R;
import com.graphhopper.directions.api.client.JSON;
import com.graphhopper.directions.api.client.model.GeocodingLocation;
import com.graphhopper.directions.api.client.model.Service;
import com.graphhopper.directions.api.client.model.Shipment;
import com.graphhopper.directions.api.client.model.Stop;
import com.graphhopper.directions.api.client.model.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.dropex.Common.Common.currentCustomer;

public class Job {
    private String jobID;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Shipment> shipments;
    private ArrayList<Service> services=new ArrayList<>();
    private GeocodingLocation pickUpLocation;


    public GeocodingLocation getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(GeocodingLocation pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Job(ArrayList<Vehicle> vehicles, ArrayList<Shipment> shipments) {
        this.vehicles = vehicles;
        this.shipments = shipments;
    }

    public Job() {

    }

    public String getJobID() {
        return this.jobID;
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

    public String postToGraphhopper(String s, Activity activity) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,s);
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/vrp/optimize?key=c9a37a2f-6bfd-4c87-9bf9-b482e64dfbee")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        Log.e("JOB ", s);

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
                    Log.e("JOB ID",jobID);
                    currentCustomer.getCurrentJob().setJobID(jobID);
                    Intent goToNavLauncher=new Intent(activity, NavigationLauncherActivity.class);
                    String uriString="https://graphhopper.com/api/1/vrp/solution/"+currentCustomer.getCurrentJob().getJobID()+"?vehicle_id="+"default"+"&key="+activity.getString(R.string.gh_key);
                    Uri uri=Uri.parse("https://graphhopper.com/api/1/vrp/solution/"+currentCustomer.getCurrentJob().getJobID()+"?vehicle_id="+"default"+"&key="+activity.getString(R.string.gh_key));
                    goToNavLauncher.setData(uri);
                    Log.e("URI", String.valueOf(uri));
                    Log.e("URI string", uriString);

                    activity.startActivity(goToNavLauncher);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return jobID;
    }

    public String buildJsonRequest(){
        JSONObject jobObject=new JSONObject();
        JSONObject vehicles=new JSONObject();
        JSONObject shipments=new JSONObject();
        JSONObject location=new JSONObject();
        JSONObject deliveyAddress=new JSONObject();
        JSONArray vehiclesArray=new JSONArray();
        JSONArray shipmentArray=new JSONArray();

        final ArrayList<Shipment> shipments1 = getShipments();
        try {
            vehicles.put("vehicle_id","default");
            //final Stop delivery = shipments1.get(0).getDelivery();
            location.put("location_id",pickUpLocation.getOsmId());
            location.put("lon",pickUpLocation.getPoint().getLng());
            location.put("lat",pickUpLocation.getPoint().getLat());

            vehicles.put("start_address",location);
            vehiclesArray.put(vehicles);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        int i=0;

        for (Shipment shipment: shipments1) {
shipments=new JSONObject();
            try {
                shipments.put("id",shipment.getId()+String.valueOf(i));
             //   deliveyAddress.put("address",location);
              //  shipments.put("pickup",deliveyAddress);
                final Stop delivery = shipment.getDelivery();
                location.put("location_id",String.valueOf(i)+"randomness");
                location.put("lon",delivery.getAddress().getLon());
                location.put("lat",delivery.getAddress().getLat());
                deliveyAddress.put("address",location);
               // deliveyAddress.put("preparation_time",100000);
                shipments.put("address",location);
               // shipments.put("max_time_in_vehicle",1000);
                shipmentArray.put(shipments);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            i++;
        }

        try {
            jobObject.put("vehicles",vehiclesArray);
            jobObject.put("services",shipmentArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       String jsonString=jobObject.toString();


Log.e("services",shipmentArray.toString());

        return jsonString;
    }
}
