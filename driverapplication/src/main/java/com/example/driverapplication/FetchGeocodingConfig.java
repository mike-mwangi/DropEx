package com.example.driverapplication;

public class FetchGeocodingConfig {

    final String query;
    final String locale;
    final int limit;
    final boolean reverse;
    final String point;
    final String provider;

    public FetchGeocodingConfig(String query, String locale, int limit, boolean reverse, String point, String provider) {
        this.query = query;
        this.locale = locale;
        this.limit = limit;
        this.reverse = reverse;
        this.point = point;
        this.provider = provider;
    }
}