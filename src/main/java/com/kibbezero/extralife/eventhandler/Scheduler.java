package com.kibbezero.extralife.eventhandler;

import com.kibbezero.extralife.donordriveclient.Connection;
import com.kibbezero.extralife.donordriveclient.Donation;
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
    private static HashMap<String, Donation> donations = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();


    public static void startup() {
        Connection connection = new Connection("https://extralife.donordrive.com");
        donations = new HashMap<>();
        try {
            ArrayList<Participant> participants = new ArrayList<>(Arrays.asList(connection.getTeamParticipants("50922")));
            for (Participant participant : participants){
                Donation[] donationList = connection.getParticipantDonations(participant.getParticipantID());

                for(Donation donation: donationList) {
                    donations.put(donation.getDonationID(), donation);
                }
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
                for (Participant participant : connection.getTeamParticipants("50922")){
                    Donation[] donationsRemote = connection.getParticipantDonations(participant.getParticipantID());

                    for (Donation remoteDonation : donationsRemote) {
                        if(!donations.containsKey(remoteDonation.getDonationID())) {
                            for (PlayerEntity player:world.getPlayers()) {
                                //noinspection ConstantConditions
                                player.getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY).ifPresent(capability -> {
                                    if(capability.getDonorDriveId().equals(remoteDonation.getParticipantID())){
                                        ProcessDonationEvent(player, remoteDonation);
                                        donations.put(remoteDonation.getDonationID(), remoteDonation);
                                    }
                                });
                            }
                        }
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

        String chosenIncentiveId = donation.getIncentiveID();

        if (chosenIncentiveId.isEmpty()){
            double amount = donation.getAmount();
            if (amount >= 100.0) {
                //The Wither spawns somewhere in the overworld
            } else if (amount == 77.77) {
                //Give the player a stack of gold, a stack of diamond, a stack of emerald, and a stack of ender pearls
            } else if (amount >= 75.0) {
                //Totem of Resurrection + full set of enchanted armor
            } else if (amount == 66.66) {
                //Your entire inventory turns to poop
            } else if (amount >= 50.0) {
                //Weapon or armor with random enchants
            } else if (amount >= 25.0) {
                //Start new round of Feast or Famine or change up everyone's targets
            } else if (amount >= 15.0) {
                //Give random task the player can complete for random amount of points
            } else if (amount >= 10.0) {
                //give random stack of mid-level metal (not diamond or emerald)
            } else if (amount == 6.66) {
                //Smite the person
            } else if (amount >= 5.0) {
                //give random building blocks or decorations
            }
        } else {
            if(chosenIncentiveId.equals("A85C9A9C-EDA2-2ECC-F952F2E458322C57")) {
                Item[] items = {Items.GRAVEL};
                GiveRandomItemEvent.giveRandomItem(player, items, 1, 64);
            }
        }


    }
}
