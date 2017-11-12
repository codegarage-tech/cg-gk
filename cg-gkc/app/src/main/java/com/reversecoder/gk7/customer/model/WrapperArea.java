package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperArea {

    private String error = "";
    private String success = "";
    private ArrayList<Area> areas = new ArrayList<Area>();

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

    public ArrayList<Area> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<Area> areas) {
        this.areas = areas;
    }

    public static WrapperArea getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperArea.class);
    }

    @Override
    public String toString() {
        return "WrapperArea{" +
                "error='" + error + '\'' +
                ", success='" + success + '\'' +
                ", areas=" + areas +
                '}';
    }
}
