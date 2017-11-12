package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperEstimatedFare {

    EstimatedFare result = new EstimatedFare();
    private String error = "";
    private String success = "";

    public EstimatedFare getResult() {
        return result;
    }

    public void setResult(EstimatedFare result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public static WrapperEstimatedFare getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperEstimatedFare.class);
    }

    @Override
    public String toString() {
        return "WrapperEstimatedFare{" +
                "result=" + result +
                ", error='" + error + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
