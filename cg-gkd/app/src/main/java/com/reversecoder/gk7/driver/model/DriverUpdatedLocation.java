package com.reversecoder.gk7.driver.model;

/**
 * Created by rashed on 3/2/16.
 */
public class DriverUpdatedLocation {

    private String did = "";
    private String post_code = "";
    private String lat = "";
    private String lon = "";
    private String address_string = "";

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAddress_string() {
        return address_string;
    }

    public void setAddress_string(String address_string) {
        this.address_string = address_string;
    }

    @Override
    public String toString() {
        return "DriverLocation{" +
                "did='" + did + '\'' +
                ", post_code='" + post_code + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", address_string='" + address_string + '\'' +
                '}';
    }
}
