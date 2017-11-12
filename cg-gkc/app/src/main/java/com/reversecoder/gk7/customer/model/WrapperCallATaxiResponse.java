package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperCallATaxiResponse {

    private String success = "";
    private String error="";
    private String orderId="";

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public static WrapperCallATaxiResponse getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperCallATaxiResponse.class);
    }

    @Override
    public String toString() {
        return "WrapperCommonResponse{" +
                "success='" + success + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
