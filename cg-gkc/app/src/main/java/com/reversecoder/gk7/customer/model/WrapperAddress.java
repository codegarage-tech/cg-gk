package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class WrapperAddress {

    private String error = "";
    private String success = "";
    private ArrayList<Address> addresses = new ArrayList<Address>();

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

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public static WrapperAddress getResponseData(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, WrapperAddress.class);
    }

    @Override
    public String toString() {
        return "WrapperAddress{" +
                "error='" + error + '\'' +
                ", success='" + success + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}
