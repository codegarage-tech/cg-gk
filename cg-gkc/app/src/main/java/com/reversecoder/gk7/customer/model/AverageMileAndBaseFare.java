package com.reversecoder.gk7.customer.model;

/**
 * Created by rashed on 3/2/16.
 */
public class AverageMileAndBaseFare {

    private String avgPerMileFare = "";
    private String avgBaseFare = "";

    public String getAvgPerMileFare() {
        return avgPerMileFare;
    }

    public void setAvgPerMileFare(String avgPerMileFare) {
        this.avgPerMileFare = avgPerMileFare;
    }

    public String getAvgBaseFare() {
        return avgBaseFare;
    }

    public void setAvgBaseFare(String avgBaseFare) {
        this.avgBaseFare = avgBaseFare;
    }

    @Override
    public String toString() {
        return "AverageMileAndBaseFare{" +
                "avgPerMileFare='" + avgPerMileFare + '\'' +
                ", avgBaseFare='" + avgBaseFare + '\'' +
                '}';
    }
}
