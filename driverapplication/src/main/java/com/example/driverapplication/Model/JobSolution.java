package com.example.driverapplication.Model;



import com.graphhopper.directions.api.client.model.Activity;
import com.graphhopper.directions.api.client.model.Address;
import com.graphhopper.directions.api.client.model.Route;
import com.graphhopper.directions.api.client.model.Solution;
import com.mapbox.geojson.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JobSolution extends Solution {



    public JobSolution( ) {

    }

    public void setValuesFromJsonObject(JSONObject Jobject){

        try {
            setCosts(Jobject.getInt("costs"));
            setDistance(Jobject.getInt("distance"));
     /*       setCompletion_time( Jobject.getInt("completion_time"));
            setTime(Jobject.getInt("transport_time"));
            setMax_operation_time(Jobject.getInt("max_operation_time"));
            setNo_vehicles(Jobject.getInt("no_vehicles"));

      */
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public static List<Point> gePoints(List<Route> routes1){
        List<Route> routes=routes1;
        List<Point> points=new ArrayList<>();
        for (Route route : routes) {
            if (route.getVehicleId().equals("default")) {
                //Found the right vehicle
                List<Activity> activities = route.getActivities();
                for (int i = 0; i < activities.size(); i++) {

                    Activity activity = activities.get(i);
                    Address address = activity.getAddress();
                    points.add(Point.fromLngLat(address.getLon(), address.getLat()));

                }
                break;
            }
        }
        return points;
    }
}
