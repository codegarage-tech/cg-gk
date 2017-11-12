package com.reversecoder.gk7.driver.model;

/**
 * Created by rashed on 3/2/16.
 */
public class CompleteJobInfo {

    private String paidDistance = "";
    private String durationTaken = "";
    private String BillToPay = "";

    public String getPaidDistance() {
        return paidDistance;
    }

    public void setPaidDistance(String paidDistance) {
        this.paidDistance = paidDistance;
    }

    public String getDurationTaken() {
        return durationTaken;
    }

    public void setDurationTaken(String durationTaken) {
        this.durationTaken = durationTaken;
    }

    public String getBillToPay() {
        return BillToPay;
    }

    public void setBillToPay(String billToPay) {
        BillToPay = billToPay;
    }

    @Override
    public String toString() {
        return "CompleteJobInfo{" +
                "paidDistance='" + paidDistance + '\'' +
                ", durationTaken='" + durationTaken + '\'' +
                ", BillToPay='" + BillToPay + '\'' +
                '}';
    }
}
