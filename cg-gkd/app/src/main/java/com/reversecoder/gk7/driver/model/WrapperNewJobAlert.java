package com.reversecoder.gk7.driver.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperNewJobAlert {

    private String error = "";
    private String success = "";
    private NewJobAlert newjobs = new NewJobAlert();
    private SenderAndReceiverType SenderReceiver=new SenderAndReceiverType();

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

    public NewJobAlert getNewjobs() {
        return newjobs;
    }

    public void setNewjobs(NewJobAlert newjobs) {
        this.newjobs = newjobs;
    }

    public static WrapperNewJobAlert getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperNewJobAlert.class);
    }

    public SenderAndReceiverType getSenderReceiver() {
        return SenderReceiver;
    }

    public void setSenderReceiver(SenderAndReceiverType senderReceiver) {
        SenderReceiver = senderReceiver;
    }

    @Override
    public String toString() {
        return "WrapperNewJobAlert{" +
                "error='" + error + '\'' +
                ", success='" + success + '\'' +
                ", newjobs=" + newjobs +
                ", SenderReceiver=" + SenderReceiver +
                '}';
    }
}
