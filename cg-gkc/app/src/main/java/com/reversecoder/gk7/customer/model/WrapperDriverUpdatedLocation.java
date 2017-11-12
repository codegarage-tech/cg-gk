package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperDriverUpdatedLocation {

    private String success = "";
    private ArrayList<DriverUpdatedLocation> driverCurrentLocation = new ArrayList<DriverUpdatedLocation>();
    private String error = "";

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<DriverUpdatedLocation> getDriverCurrentLocation() {
        return driverCurrentLocation;
    }

    public void setDriverCurrentLocation(ArrayList<DriverUpdatedLocation> driverCurrentLocation) {
        this.driverCurrentLocation = driverCurrentLocation;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static WrapperDriverUpdatedLocation getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperDriverUpdatedLocation.class);
    }

    @Override
    public String toString() {
        return "WrapperDriverUpdatedLocation{" +
                "success='" + success + '\'' +
                ", driverCurrentLocation=" + driverCurrentLocation +
                ", error='" + error + '\'' +
                '}';
    }
}
