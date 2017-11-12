package com.reversecoder.gk7.driver.model;

/**
 * Created by rashed on 3/2/16.
 */
public class AcceptJobDetails {

    private String orderId = "";
    private String driverId = "";
    private String customerId = "";
    private String customerLat = "";
    private String customerLon = "";
    private String drive_from_address = "";
    private String jobInitiated = "";
    private String jobStatus = "";
    private String IsStarted = "";
    private String paid_distance = "";
    private String cost = "";
    private String duration = "";
    private AcceptJobDriverDetails driverDetails = new AcceptJobDriverDetails();
    private String pre_distance = "";
    private String pre_duration = "";
    private String notification = "";
    private String receiver_type = "";
    private String sender_type = "";


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public String getDrive_from_address() {
        return drive_from_address;
    }

    public void setDrive_from_address(String drive_from_address) {
        this.drive_from_address = drive_from_address;
    }

    public String getJobInitiated() {
        return jobInitiated;
    }

    public void setJobInitiated(String jobInitiated) {
        this.jobInitiated = jobInitiated;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public AcceptJobDriverDetails getDriverDetails() {
        return driverDetails;
    }

    public String getIsStarted() {
        return IsStarted;
    }

    public void setIsStarted(String isStarted) {
        IsStarted = isStarted;
    }

    public void setDriverDetails(AcceptJobDriverDetails driverDetails) {
        this.driverDetails = driverDetails;
    }

    public String getPre_distance() {
        return pre_distance;
    }

    public void setPre_distance(String pre_distance) {
        this.pre_distance = pre_distance;
    }

    public String getPre_duration() {
        return pre_duration;
    }

    public void setPre_duration(String pre_duration) {
        this.pre_duration = pre_duration;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getPaid_distance() {
        return paid_distance;
    }

    public void setPaid_distance(String paid_distance) {
        this.paid_distance = paid_distance;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "AcceptJobDetails{" +
                "orderId='" + orderId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerLat='" + customerLat + '\'' +
                ", customerLon='" + customerLon + '\'' +
                ", drive_from_address='" + drive_from_address + '\'' +
                ", jobInitiated='" + jobInitiated + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", IsStarted='" + IsStarted + '\'' +
                ", paid_distance='" + paid_distance + '\'' +
                ", cost='" + cost + '\'' +
                ", duration='" + duration + '\'' +
                ", driverDetails=" + driverDetails +
                ", pre_distance='" + pre_distance + '\'' +
                ", pre_duration='" + pre_duration + '\'' +
                ", notification='" + notification + '\'' +
                ", receiver_type='" + receiver_type + '\'' +
                ", sender_type='" + sender_type + '\'' +
                '}';
    }
}