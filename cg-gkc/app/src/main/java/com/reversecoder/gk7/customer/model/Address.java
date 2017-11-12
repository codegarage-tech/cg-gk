package com.reversecoder.gk7.customer.model;

/**
 * Created by rashed on 3/2/16.
 */
public class Address {

    private String id = "";
    private String customer_id = "";
    private String address_title = "";
    private String street = "";
    private String area_id = "";
    private String postal_code = "";
    private String lat = "";
    private String lon = "";
    private String status = "";
    private String created = "";
    private String modified = "";
    private String created_by = "";
    private String modified_by = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getAddress_title() {
        return address_title;
    }

    public void setAddress_title(String address_title) {
        this.address_title = address_title;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", address_title='" + address_title + '\'' +
                ", street='" + street + '\'' +
                ", area_id='" + area_id + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", status='" + status + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", created_by='" + created_by + '\'' +
                ", modified_by='" + modified_by + '\'' +
                '}';
    }
}
