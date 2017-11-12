package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperCommonResponse {

    private String success = "";
    private String error="";

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static WrapperCommonResponse getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperCommonResponse.class);
    }

    @Override
    public String toString() {
        return "WrapperCommonResponse{" +
                "success='" + success + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
