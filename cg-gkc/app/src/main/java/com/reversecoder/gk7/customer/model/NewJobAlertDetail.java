package com.reversecoder.gk7.customer.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class NewJobAlertDetail {

    private String orderId = "";
    private String customerId = "";
    private String customerLat = "";
    private String customerLon = "";
    private String customerAddress = "";
    private String isBid = "";
    private String isPhone = "";
    private String isCorporate = "";
    private String orderRequestId = "";
    private String CustomerName = "";
    private String CustomerEmail = "";
    private String CustomerMobile = "";
    private String customerPhone = "";
    private String estimatedFare = "";
    private String customerDestinationAddress = "";
    private String IsFourPassenger = "";
    private String distance = "";
    private String customerDestinationLat="";
    private String customerDestinationLon="";

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerLon() {
        return customerLon;
    }

    public void setCustomerLon(String customerLon) {
        this.customerLon = customerLon;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getIsBid() {
        return isBid;
    }

    public void setIsBid(String isBid) {
        this.isBid = isBid;
    }

    public String getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(String isPhone) {
        this.isPhone = isPhone;
    }

    public String getIsCorporate() {
        return isCorporate;
    }

    public void setIsCorporate(String isCorporate) {
        this.isCorporate = isCorporate;
    }

    public String getOrderRequestId() {
        return orderRequestId;
    }

    public void setOrderRequestId(String orderRequestId) {
        this.orderRequestId = orderRequestId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        CustomerMobile = customerMobile;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(String estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public String getCustomerDestinationAddress() {
        return customerDestinationAddress;
    }

    public void setCustomerDestinationAddress(String customerDestinationAddress) {
        this.customerDestinationAddress = customerDestinationAddress;
    }

    public String getIsFourPassenger() {
        return IsFourPassenger;
    }

    public void setIsFourPassenger(String isFourPassenger) {
        IsFourPassenger = isFourPassenger;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCustomerDestinationLat() {
        return customerDestinationLat;
    }

    public void setCustomerDestinationLat(String customerDestinationLat) {
        this.customerDestinationLat = customerDestinationLat;
    }

    public String getCustomerDestinationLon() {
        return customerDestinationLon;
    }

    public void setCustomerDestinationLon(String customerDestinationLon) {
        this.customerDestinationLon = customerDestinationLon;
    }

    public static NewJobAlertDetail getObject(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, NewJobAlertDetail.class);
    }

    public static String getObjectData(NewJobAlertDetail newJobAlertDetail) {
        Gson gson = new Gson();
        return gson.toJson(newJobAlertDetail);
    }

    @Override
    public String toString() {
        return "NewJobAlertDetail{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerLat='" + customerLat + '\'' +
                ", customerLon='" + customerLon + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", isBid='" + isBid + '\'' +
                ", isPhone='" + isPhone + '\'' +
                ", isCorporate='" + isCorporate + '\'' +
                ", orderRequestId='" + orderRequestId + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", CustomerEmail='" + CustomerEmail + '\'' +
                ", CustomerMobile='" + CustomerMobile + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", estimatedFare='" + estimatedFare + '\'' +
                ", customerDestinationAddress='" + customerDestinationAddress + '\'' +
                ", IsFourPassenger='" + IsFourPassenger + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}