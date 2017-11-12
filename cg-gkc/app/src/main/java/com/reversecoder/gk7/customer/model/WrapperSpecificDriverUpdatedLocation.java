package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperSpecificDriverUpdatedLocation {

    private String success = "";
    DriverUpdatedLocation driverCurrentLocation = new DriverUpdatedLocation();
    private String error = "";

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public DriverUpdatedLocation getDriverCurrentLocation() {
        return driverCurrentLocation;
    }

    public void setDriverCurrentLocation(DriverUpdatedLocation driverCurrentLocation) {
        this.driverCurrentLocation = driverCurrentLocation;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static WrapperSpecificDriverUpdatedLocation getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperSpecificDriverUpdatedLocation.class);
    }

    @Override
    public String toString() {
        return "WrapperSpecificDriverUpdatedLocation{" +
                "success='" + success + '\'' +
                ", driverCurrentLocation=" + driverCurrentLocation +
                ", error='" + error + '\'' +
                '}';
    }
}
