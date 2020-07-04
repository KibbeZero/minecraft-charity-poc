package com.kibbezero.extralife.playercapability;

public class DonorDriveTag implements IDonorDriveTag {

    private String donorDriveId = "";

    @Override
    public void setPlayerAssociation(String DonorDriveId) {
        donorDriveId = DonorDriveId;
    }

    @Override
    public void removePlayerAssociation() {
        donorDriveId = "";
    }

    @Override
    public String getDonorDriveId() {
        return donorDriveId;
    }
}
