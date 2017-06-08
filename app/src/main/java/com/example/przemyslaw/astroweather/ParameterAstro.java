package com.example.przemyslaw.astroweather;

import java.io.Serializable;

/**
 * Created by Przemyslaw on 2017-05-06.
 */

public class ParameterAstro implements Serializable{

    double longitude;
    double latitude;
    int timeToRefresh;

    public ParameterAstro(double longitude, double latitude, int timeToRefresh) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeToRefresh = timeToRefresh;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getTimeToRefresh() {
        return timeToRefresh;
    }

    public void setTimeToRefresh(int timeToRefresh) {
        this.timeToRefresh = timeToRefresh;
    }
}
