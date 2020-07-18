package com.kibbezero.extralife.eventhandler;

import com.kibbezero.extralife.donordriveclient.Connection;
import com.kibbezero.extralife.donordriveclient.Donation;
import com.kibbezero.extralife.donordriveclient.Incentive;
import com.kibbezero.extralife.donordriveclient.Participant;
import com.kibbezero.extralife.playercapability.DonorDriveTagCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Scheduler {

    private static long ticks = 0;
    private static ArrayList<Participant> participants;
    private static HashMap<String, Donation[]> donations;
    private static final Logger LOGGER = LogManager.getLogger();


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

    public static void updateTick(World world){
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
                            boolean triggeredEvent = false;
                            for (Donation previousDonation : donationsLocal) {
                                if(previousDonation.getDonationID().equals(donation.getDonationID())) {
                                    if (!previousDonation.getEventTriggered()) {
                                        if(!triggeredEvent) {
                                            for (PlayerEntity player:world.getPlayers()) {
                                                //noinspection ConstantConditions
                                                player.getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY).ifPresent(capability -> {
                                                    if(capability.getDonorDriveId().equals(previousDonation.getParticipantID())){

                                                        ProcessDonationEvent(player, donation);
                                                        previousDonation.setEventTriggered(true);
                                                    }
                                                });
                                            }
                                            if(previousDonation.getEventTriggered()){
                                                triggeredEvent = true;
                                            }
                                        } else {
                                            donation.setEventTriggered(previousDonation.getEventTriggered());
                                        }
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
                LOGGER.error(e);
                return;
            }
            ticks = 0;
        }
    }

    public static void ProcessDonationEvent(PlayerEntity player, Donation donation) {

        Connection connection = new Connection("https://extralife.donordrive.com");

        try {

            String chosenIncentiveId = "";

            if (donation.getIncentiveID().isEmpty()){
                Incentive [] incentives = connection.getParticipantIncentives(donation.getParticipantID());
                if (incentives != null) {
                    for (Incentive incentive : incentives) {
                        if (incentive.getAmount() <= donation.getAmount()) {
                            chosenIncentiveId = incentive.getIncentiveID();
                        }
                    }
                }
            }

            if (chosenIncentiveId.isEmpty()) {
                return;
            }

            if(chosenIncentiveId.equals("A85C9A9C-EDA2-2ECC-F952F2E458322C57")) {
                Item[] items = {Items.GRAVEL};
                GiveRandomItemEvent.giveRandomItem(player, items, 1, 64);
            }
        } catch (IOException exception) {
            LOGGER.error(exception);
        }

    }
}
