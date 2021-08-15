package com.example.dropex;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dropex.Model.Job;
import com.example.dropex.Model.JobSolution;
import com.graphhopper.directions.api.client.ApiException;
import com.graphhopper.directions.api.client.api.SolutionApi;
import com.graphhopper.directions.api.client.model.Activity;
import com.graphhopper.directions.api.client.model.Address;
import com.graphhopper.directions.api.client.model.Route;
import com.graphhopper.directions.api.client.model.Solution;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FetchSolutionTaskButReturnJobSolution extends AsyncTask<FetchSolutionConfig, Void, JobSolution> {

    private final String ghKey;
    private final FetchSolutionCallBackInterfaceButWithJobSolution callbackInterface;

    public FetchSolutionTaskButReturnJobSolution(FetchSolutionCallBackInterfaceButWithJobSolution callbackInterface, String ghKey) {
        this.callbackInterface = callbackInterface;
        this.ghKey = ghKey;
    }

    @Override
    protected JobSolution doInBackground(FetchSolutionConfig... solutions) {

        if (solutions.length != 1)
            throw new IllegalArgumentException("It's only possible to fetch one solution at a time");

        List<Point> points = new ArrayList<>();
        SolutionApi api = new SolutionApi();
        JobSolution jobSolution=new JobSolution();

        try {
            com.graphhopper.directions.api.client.model.Response res = api.getSolution(ghKey, solutions[0].jobId);
            Log.e("FETCHSOLUTIONTASK",solutions[0].jobId);
            List<Route> routes = res.getSolution().getRoutes();
            final Solution solution = res.getSolution();
            jobSolution.setNoVehicles(solution.getNoVehicles());
            jobSolution.setCosts(solution.getCosts());
            jobSolution.setTime(solution.getTime());
            jobSolution.setCompletionTime(solution.getCompletionTime());
            jobSolution.setDistance(solution.getDistance());

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

            if (points.isEmpty()) {
                callbackInterface.onError(R.string.error_vehicle_not_found);
            }
            else {
                jobSolution.setPoints(points);
            }
        } catch (ApiException e) {
            callbackInterface.onError(R.string.error_fetching_solution);
            Timber.e(e, "An exception occured when fetching a solution with jobId %s", solutions[0].jobId);
        }

        return jobSolution;
    }

    @Override
    protected void onPostExecute(JobSolution jobSolution) {
        callbackInterface.onPostExecute(jobSolution);
    }
}