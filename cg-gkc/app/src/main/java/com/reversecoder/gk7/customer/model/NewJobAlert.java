package com.reversecoder.gk7.customer.model;

import java.util.ArrayList;

/**
 * Created by rashed on 3/2/16.
 */
public class NewJobAlert {

    private String normal = "";
    private ArrayList<NewJobAlertDetail> normalJobsDetails = new ArrayList<NewJobAlertDetail>();
    private String bid = "";
    private ArrayList<NewJobAlertDetail> bidJobsDetails = new ArrayList<NewJobAlertDetail>();
    private String receiver_type= "";
    private String sender_type= "";

    public ArrayList<NewJobAlertDetail> getNormalJobsDetails() {
        return normalJobsDetails;
    }

    public void setNormalJobsDetails(ArrayList<NewJobAlertDetail> normalJobsDetails) {
        this.normalJobsDetails = normalJobsDetails;
    }

    public ArrayList<NewJobAlertDetail> getBidJobsDetails() {
        return bidJobsDetails;
    }

    public void setBidJobsDetails(ArrayList<NewJobAlertDetail> bidJobsDetails) {
        this.bidJobsDetails = bidJobsDetails;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
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

    @Override
    public String toString() {
        return "NewJobAlert{" +
                "normal='" + normal + '\'' +
                ", normalJobsDetails=" + normalJobsDetails +
                ", bid='" + bid + '\'' +
                ", bidJobsDetails=" + bidJobsDetails +
                ", receiver_type='" + receiver_type + '\'' +
                ", sender_type='" + sender_type + '\'' +
                '}';
    }
}