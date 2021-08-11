package com.example.dropex.Model;



import com.graphhopper.directions.api.client.model.Solution;
import com.mapbox.geojson.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JobSolution extends Solution {
    private List<Point> points;


    public JobSolution( List<Point> points) {

        this.points = points;
    }

    public JobSolution() {

    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
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
}
