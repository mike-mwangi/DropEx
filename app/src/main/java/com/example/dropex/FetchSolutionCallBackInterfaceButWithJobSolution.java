package com.example.dropex;

import com.example.dropex.Model.JobSolution;
import com.mapbox.geojson.Point;

import java.util.List;

public interface FetchSolutionCallBackInterfaceButWithJobSolution {
    void onError(int message);

    void onPostExecute(JobSolution jobSolution);
}
