package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperDriverBidJobs {

    private String driverName="";
    private String message = "";
    private String error="";
    private ArrayList<DriverBidJob> driverBidJobs=new ArrayList<DriverBidJob>();

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public ArrayList<DriverBidJob> getDriverBidJobs() {
        return driverBidJobs;
    }

    public void setDriverBidJobs(ArrayList<DriverBidJob> driverBidJobs) {
        this.driverBidJobs = driverBidJobs;
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

    public static WrapperDriverBidJobs getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperDriverBidJobs.class);
    }

    @Override
    public String toString() {
        return "WrapperDriverBidJobs{" +
                "driverName='" + driverName + '\'' +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", driverBidJobs=" + driverBidJobs +
                '}';
    }
}
