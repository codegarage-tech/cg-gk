package com.reversecoder.gk7.driver.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperDriverJobs {

    private String driverName="";
    private String message = "";
    private String error="";
    private ArrayList<DriverJob> driverjobs=new ArrayList<DriverJob>();

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public ArrayList<DriverJob> getDriverJobs() {
        return driverjobs;
    }

    public void setDriverJobs(ArrayList<DriverJob> driverBidJobs) {
        this.driverjobs = driverBidJobs;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResponseMessage() {
        return message;
    }

    public void setResponseMessage(String message) {
        this.message = message;
    }

    public static WrapperDriverJobs getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperDriverJobs.class);
    }

    @Override
    public String toString() {
        return "WrapperDriverJobs{" +
                "driverName='" + driverName + '\'' +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", driverBidJobs=" + driverjobs +
                '}';
    }
}
