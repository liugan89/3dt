package com.takumi.wms.model;

public class MandatoryAtts {

    private String id;
    private Double countSum;
    private Double acceptedCountSum;
    private Double weightKgSum;
    private Double acceptedWeightKgSum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCountSum() {
        return countSum;
    }

    public void setCountSum(Double countSum) {
        this.countSum = countSum;
    }

    public Double getAcceptedCountSum() {
        return acceptedCountSum;
    }

    public void setAcceptedCountSum(Double acceptedCountSum) {
        this.acceptedCountSum = acceptedCountSum;
    }

    public Double getWeightKgSum() {
        return weightKgSum;
    }

    public void setWeightKgSum(Double weightKgSum) {
        this.weightKgSum = weightKgSum;
    }

    public Double getAcceptedWeightKgSum() {
        return acceptedWeightKgSum;
    }

    public void setAcceptedWeightKgSum(Double acceptedWeightKgSum) {
        this.acceptedWeightKgSum = acceptedWeightKgSum;
    }
}
