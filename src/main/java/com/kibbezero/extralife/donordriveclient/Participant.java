package com.kibbezero.extralife.donordriveclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Participant implements Serializable
{

    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("fundraisingGoal")
    @Expose
    private Double fundraisingGoal;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("createdDateUTC")
    @Expose
    private String createdDateUTC;
    @SerializedName("eventID")
    @Expose
    private Double eventID;
    @SerializedName("sumDonations")
    @Expose
    private Double sumDonations;
    @SerializedName("participantID")
    @Expose
    private Double participantID;
    @SerializedName("teamName")
    @Expose
    private String teamName;
    @SerializedName("avatarImageURL")
    @Expose
    private String avatarImageURL;
    @SerializedName("teamID")
    @Expose
    private Double teamID;
    @SerializedName("isTeamCaptain")
    @Expose
    private Boolean isTeamCaptain;
    @SerializedName("sumPledges")
    @Expose
    private Double sumPledges;
    @SerializedName("numDonations")
    @Expose
    private int numDonations;
    private final static long serialVersionUID = 2260877478661805033L;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getFundraisingGoal() {
        return fundraisingGoal;
    }

    public void setFundraisingGoal(Double fundraisingGoal) {
        this.fundraisingGoal = fundraisingGoal;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getCreatedDateUTC() {
        return createdDateUTC;
    }

    public void setCreatedDateUTC(String createdDateUTC) {
        this.createdDateUTC = createdDateUTC;
    }

    public String getEventID() {
        return String.format("%.0f", eventID);
    }

    public void setEventID(Double eventID) {
        this.eventID = eventID;
    }

    public Double getSumDonations() {
        return sumDonations;
    }

    public void setSumDonations(Double sumDonations) {
        this.sumDonations = sumDonations;
    }

    public String getParticipantID() {
        return String.format("%.0f", participantID);
    }

    public void setParticipantID(Double participantID) {
        this.participantID = participantID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAvatarImageURL() {
        return avatarImageURL;
    }

    public void setAvatarImageURL(String avatarImageURL) {
        this.avatarImageURL = avatarImageURL;
    }

    public String getTeamID() {
        return String.format("%.0f", teamID);
    }

    public void setTeamID(Double teamID) {
        this.teamID = teamID;
    }

    public Boolean getIsTeamCaptain() {
        return isTeamCaptain;
    }

    public void setIsTeamCaptain(Boolean isTeamCaptain) {
        this.isTeamCaptain = isTeamCaptain;
    }

    public Double getSumPledges() {
        return sumPledges;
    }

    public void setSumPledges(Double sumPledges) {
        this.sumPledges = sumPledges;
    }

    public int getNumDonations() {
        return numDonations;
    }

    public void setNumDonations(int numDonations) {
        this.numDonations = numDonations;
    }

}