package com.kibbezero.extralife.playercapability;

public interface IDonorDriveTag {
    void setPlayerAssociation(final String DonorDriveId);
    void removePlayerAssociation();
    String getDonorDriveId();
}

