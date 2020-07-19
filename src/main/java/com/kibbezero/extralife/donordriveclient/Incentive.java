package com.kibbezero.extralife.donordriveclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Incentive implements Serializable {

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("incentiveID")
    @Expose
    private String incentiveID;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncentiveID() {
        return incentiveID;
    }

    public void setIncentiveID(String incentiveID) {
        this.incentiveID = incentiveID;
    }
}
