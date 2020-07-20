package com.kibbezero.extralife.donordriveclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Donation implements Serializable
{

    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("participantID")
    @Expose
    private Integer participantID;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("donorID")
    @Expose
    private String donorID;
    @SerializedName("avatarImageURL")
    @Expose
    private String avatarImageURL;
    @SerializedName("createdDateUTC")
    @Expose
    private String createdDateUTC;
    @SerializedName("eventID")
    @Expose
    private Integer eventID;
    @SerializedName("teamID")
    @Expose
    private Integer teamID;
    @SerializedName("donationID")
    @Expose
    private String donationID;

    public String getIncentiveID() {
        if(incentiveID == null){
            return "";
        }
        return incentiveID;
    }

    public void setIncentiveID(String incentiveID) {
        this.incentiveID = incentiveID;
    }

    @SerializedName("incentiveID")
    @Expose
    private String incentiveID;

    private final static long serialVersionUID = -6147877872862817704L;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getParticipantID() {
        return participantID.toString();
    }

    public void setParticipantID(Integer participantID) {
        this.participantID = participantID;
    }

    public void setParticipantID(String participantID) {
        this.participantID = Integer.parseInt(participantID);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDonorID() {
        return donorID;
    }

    public void setDonorID(String donorID) {
        this.donorID = donorID;
    }

    public String getAvatarImageURL() {
        return avatarImageURL;
    }

    public void setAvatarImageURL(String avatarImageURL) {
        this.avatarImageURL = avatarImageURL;
    }

    public String getCreatedDateUTC() {
        return createdDateUTC;
    }

    public void setCreatedDateUTC(String createdDateUTC) {
        this.createdDateUTC = createdDateUTC;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public Integer getTeamID() {
        return teamID;
    }

    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    public String getDonationID() {
        return donationID;
    }

    public void setDonationID(String donationID) {
        this.donationID = donationID;
    }


    @Override
    public String toString() {
        return "Donation{" +
                "displayName='" + displayName + '\'' +
                ", amount=" + amount +
                ", createdDateUTC='" + createdDateUTC + '\'' +
                '}';
    }
}
