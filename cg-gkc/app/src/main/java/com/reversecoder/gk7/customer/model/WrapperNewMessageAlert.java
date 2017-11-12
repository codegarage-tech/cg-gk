package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperNewMessageAlert {

    private String newMsg="";
    private String success = "";
    private String error="";

    public String getNewMessages() {
        return newMsg;
    }

    public void setNewMessages(String newMsg) {
        this.newMsg = newMsg;
    }

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

    public static WrapperNewMessageAlert getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperNewMessageAlert.class);
    }

    @Override
    public String toString() {
        return "WrapperNewMessageAlert{" +
                "newMsg='" + newMsg + '\'' +
                ", success='" + success + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
