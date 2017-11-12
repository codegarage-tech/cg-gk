package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperAcceptJobDetailsAlert {

    private String success = "";
    private AcceptJobDetails JobDetails = new AcceptJobDetails();
    private String error = "";

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public AcceptJobDetails getJobDetails() {
        return JobDetails;
    }

    public void setJobDetails(AcceptJobDetails jobDetails) {
        JobDetails = jobDetails;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static WrapperAcceptJobDetailsAlert getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperAcceptJobDetailsAlert.class);
    }

    @Override
    public String toString() {
        return "WrapperAcceptJobDetails{" +
                "success='" + success + '\'' +
                ", JobDetails=" + JobDetails +
                ", error='" + error + '\'' +
                '}';
    }
}