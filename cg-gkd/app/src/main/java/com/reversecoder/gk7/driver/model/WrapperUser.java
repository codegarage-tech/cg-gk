package com.reversecoder.gk7.driver.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperUser {

    private User user=new User();
    private String message = "";
    private String error="";

    public String getResponseMessage() {
        return message;
    }

    public void setResponseMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static WrapperUser getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperUser.class);
    }

    @Override
    public String toString() {
        return "WrapperUser{" +
                "user=" + user +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
