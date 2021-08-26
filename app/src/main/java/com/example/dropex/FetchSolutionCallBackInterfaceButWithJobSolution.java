package com.example.dropex;

import com.example.dropex.Model.JobSolution;
import com.graphhopper.directions.api.client.model.Solution;
import com.mapbox.geojson.Point;

import java.util.List;

public interface FetchSolutionCallBackInterfaceButWithJobSolution {
    void onError(int message);

    void onPostExecute(Solution jobSolution);
}
