package com.reversecoder.gk7.driver.model;

/**
 * Created by rashed on 4/26/16.
 */
public class EstimatedFare {

    private String baseFare = "";
    private String permileFare = "";
    private String distance = "";
    private String duration = "";
    private String estimatedFare = "";

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getPermileFare() {
        return permileFare;
    }

    public void setPermileFare(String permileFare) {
        this.permileFare = permileFare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(String estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    @Override
    public String toString() {
        return "FareCalculation{" +
                "baseFare='" + baseFare + '\'' +
                ", permileFare='" + permileFare + '\'' +
                ", distance='" + distance + '\'' +
                ", duration='" + duration + '\'' +
                ", estimatedFare='" + estimatedFare + '\'' +
                '}';
    }
}
