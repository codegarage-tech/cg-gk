package com.reversecoder.gk7.driver.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperCompleteJobResponse {

    private String success = "";
    private String error="";
    private CompleteJobInfo info=new CompleteJobInfo();

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

    public CompleteJobInfo getInfo() {
        return info;
    }

    public void setInfo(CompleteJobInfo info) {
        this.info = info;
    }

    public static WrapperCompleteJobResponse getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperCompleteJobResponse.class);
    }

    @Override
    public String toString() {
        return "WrapperCompleteJobResponse{" +
                "success='" + success + '\'' +
                ", error='" + error + '\'' +
                ", info=" + info +
                '}';
    }
}
