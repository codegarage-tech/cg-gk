package com.reversecoder.gk7.driver.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperDriverNewJobs {

    private String driverName="";
    private String message = "";
    private String error="";
    private ArrayList<DriverNewJob> driverNewJobs=new ArrayList<DriverNewJob>();

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public ArrayList<DriverNewJob> getDriverNewJobs() {
        return driverNewJobs;
    }

    public void setDriverNewJobs(ArrayList<DriverNewJob> driverNewJobs) {
        this.driverNewJobs = driverNewJobs;
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

    public static WrapperDriverNewJobs getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperDriverNewJobs.class);
    }

    @Override
    public String toString() {
        return "WrapperDriverNewJobs{" +
                "driverName='" + driverName + '\'' +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", driverNewJobs=" + driverNewJobs +
                '}';
    }
}
