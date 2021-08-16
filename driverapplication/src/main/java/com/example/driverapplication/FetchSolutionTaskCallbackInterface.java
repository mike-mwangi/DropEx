package com.example.driverapplication;

import java.util.List;

public interface FetchSolutionTaskCallbackInterface {

    void onError(int message);

//    void onPostExecute(List<Point> points);

    void onPostExecute(List<com.mapbox.geojson.Point> points);
}
