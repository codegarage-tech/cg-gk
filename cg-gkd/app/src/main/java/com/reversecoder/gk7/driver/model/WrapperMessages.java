package com.reversecoder.gk7.driver.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperMessages {

    private String CustomerName = "";
    private String message = "";
    private String error = "";
    private ArrayList<Message> msgs = new ArrayList<Message>();

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getResponseMessage() {
        return message;
    }

    public void setResponseMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<Message> getAllMessages() {
        return msgs;
    }

    public void setAllMessages(ArrayList<Message> msgs) {
        this.msgs = msgs;
    }

    public static WrapperMessages getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperMessages.class);
    }

    @Override
    public String toString() {
        return "WrapperMessages{" +
                "CustomerName='" + CustomerName + '\'' +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", msgs=" + msgs +
                '}';
    }

}
