package com.example.dropex;

import android.os.AsyncTask;
import android.util.Log;

import com.graphhopper.directions.api.client.ApiException;
import com.graphhopper.directions.api.client.api.SolutionApi;
import com.graphhopper.directions.api.client.model.Activity;
import com.graphhopper.directions.api.client.model.Address;
import com.graphhopper.directions.api.client.model.Route;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FetchSolutionTask extends AsyncTask<FetchSolutionConfig, Void, List<Point>> {

    private final String ghKey;
    private final FetchSolutionTaskCallbackInterface callbackInterface;

    public FetchSolutionTask(FetchSolutionTaskCallbackInterface callbackInterface, String ghKey) {
        this.callbackInterface = callbackInterface;
        this.ghKey = ghKey;
    }

    @Override
    protected List<Point> doInBackground(FetchSolutionConfig... solutions) {

        if (solutions.length != 1)
            throw new IllegalArgumentException("It's only possible to fetch one solution at a time");

        List<Point> points = new ArrayList<>();
        SolutionApi api = new SolutionApi();

        try {
            com.graphhopper.directions.api.client.model.Response res = api.getSolution(ghKey, solutions[0].jobId);
            Log.e("FETCHSOLUTIONTASK",solutions[0].jobId);
            List<Route> routes = res.getSolution().getRoutes();

            for (Route route : routes) {
                if (route.getVehicleId().equals(solutions[0].vehicleId) || solutions[0].vehicleId == null) {
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

            if (points.isEmpty())
                callbackInterface.onError(R.string.error_vehicle_not_found);

        } catch (ApiException e) {
            callbackInterface.onError(R.string.error_fetching_solution);
            Timber.e(e, "An exception occured when fetching a solution with jobId %s", solutions[0].jobId);
        }

        return points;
    }

    @Override
    protected void onPostExecute(List<Point> points) {
        callbackInterface.onPostExecute(points);
    }
}