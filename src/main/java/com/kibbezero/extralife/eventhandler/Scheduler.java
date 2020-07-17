package com.kibbezero.extralife.eventhandler;

import com.kibbezero.extralife.donordriveclient.Connection;
import com.kibbezero.extralife.donordriveclient.Donation;
import com.kibbezero.extralife.donordriveclient.Participant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Scheduler {

    private static long ticks = 0;
    private static ArrayList<Participant> participants;
    private static HashMap<String, Donation[]> donations;

    public static void startup() {
        Connection connection = new Connection("https://extralife.donordrive.com");
        donations = new HashMap<>();
        try {
            participants = new ArrayList<>(Arrays.asList(connection.getTeamParticipants("50922")));
            for (Participant participant : participants){
                Donation[] donationList = connection.getParticipantDonations(participant.getParticipantID());
                for (Donation donation : donationList) {
                    donation.setEventTriggered(true); //When the server starts, we assume all donations before the server started are complete
                }
                donations.put(participant.getParticipantID(), donationList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateTick(){
        ticks++;
        if (ticks >= 1200){
            Connection connection = new Connection("https://extralife.donordrive.com");
            try {
                for (Participant participant : participants){
                    Donation[] donationsRemote = connection.getParticipantDonations(participant.getParticipantID());
                    Donation[] donationsLocal = donations.getOrDefault(participant.getParticipantID(), null);
                    if (donationsLocal == null) {
                        for (Donation donation : donationsRemote) {
                            donation.setEventTriggered(true);
                        }
                        donations.put(participant.getParticipantID(), donationsRemote);
                    } else {
                        for (Donation donation : donationsRemote) {
                            for (Donation previousDonation : donationsLocal) {
                                if(previousDonation.getDonationID().equals(donation.getDonationID())) {
                                    if (!previousDonation.getEventTriggered()) {
                                        ProcessDonationEvent(donation);
                                    } else {
                                        donation.setEventTriggered(true);
                                    }
                                }
                            }

                        }
                        donations.replace(participant.getParticipantID(), donationsRemote);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ticks = 0;
        }
    }

    private static void ProcessDonationEvent(Donation donation) {
        //TODO Do Donation Events Here

        donation.setEventTriggered(true);
    }
}
