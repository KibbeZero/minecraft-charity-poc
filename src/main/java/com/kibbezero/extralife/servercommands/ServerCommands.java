package com.kibbezero.extralife.servercommands;

import com.kibbezero.extralife.donordriveclient.Donation;
import com.kibbezero.extralife.eventhandler.Scheduler;
import com.kibbezero.extralife.playercapability.DonorDriveTagCapability;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

@SuppressWarnings("ConstantConditions") //Suppressed because @CapabilityInject takes care of instantiating DONOR_DRIVE_TAG_CAPABILITY
public final class ServerCommands {

    public ServerCommands (){

    }

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();


    public static void register(final CommandDispatcher<CommandSource> dispatcher){

        LOGGER.info("Registering Server Commands");

        LiteralArgumentBuilder<CommandSource> root = Commands.literal("extralife");
        dispatcher.register(Commands.literal("extralife")
                .then(Commands.literal("set")
                        .then(Commands.argument("DonorDrive ID", StringArgumentType.word())
                            .executes(commandContext -> setDonorDriveID(commandContext.getSource(), StringArgumentType.getString(commandContext, "DonorDrive ID")))))
                .then(Commands.literal("get")
                        .executes(commandContext -> getDonorDriveID(commandContext.getSource())))
                .then(Commands.literal("runevent")
                        .then(Commands.argument("Player", GameProfileArgument.gameProfile())
                        .then(Commands.argument("Dollar Amount", DoubleArgumentType.doubleArg())
                            .executes(commandContext -> executeEvent(commandContext.getSource(), GameProfileArgument.getGameProfiles(commandContext, "Player" ), DoubleArgumentType.getDouble(commandContext, "Dollar Amount")))))));


    }

    private static int executeEvent(CommandSource source, Collection<GameProfile> gameProfiles, double dollar_amount) {

        GameProfile profile = gameProfiles.stream().findFirst().orElse(null);
        if(profile == null){
            return 0;
        }
        PlayerEntity player = source.getServer().getPlayerList().getPlayerByUUID(profile.getId());

        if (player == null) {
            source.sendFeedback(new StringTextComponent("User not found."), false);
            return 0;
        }
        player.getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY).ifPresent(capability -> {
            Donation donation = new Donation();
            donation.setAmount(dollar_amount);
            donation.setParticipantID(capability.getDonorDriveId());
            Scheduler.ProcessDonationEvent(player, donation);
        });


        return 1;

    }

    //TODO: Clean getDonorDriveID and setDonorDriveID to use some common methods
    private static int getDonorDriveID(CommandSource source) {
        try {
            source.assertIsEntity();
            if (!source.asPlayer().getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY).isPresent()) {
                source.sendErrorMessage(new StringTextComponent("Cannot find DonorDrive Property on " + source.getName() + "."));
                return 0;
            }
            source.asPlayer().getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY, null)
                    .ifPresent(capability -> source.sendFeedback(new StringTextComponent(capability.getDonorDriveId()), false));
        } catch (CommandSyntaxException e) {
            source.sendErrorMessage(new StringTextComponent(e.getMessage()));
            return 0;
        }

        return 1;
    }

    private static int setDonorDriveID(CommandSource commandSource, String donorDriveID) {
        try {
            commandSource.assertIsEntity();
            if (!commandSource.asPlayer().getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY).isPresent()) {
                commandSource.sendErrorMessage(new StringTextComponent("Cannot find DonorDrive Property on " + commandSource.getName() + "."));
                return 0;
            }
            commandSource.asPlayer().getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY, null)
                    .ifPresent(capability -> capability.setPlayerAssociation(donorDriveID));
        } catch (CommandSyntaxException e) {
            commandSource.sendErrorMessage(new StringTextComponent(e.getMessage()));
            return 0;
        }

        if (donorDriveID.equals("")) {
            commandSource.sendFeedback(new StringTextComponent("DonorDrive ID has been cleared."), true);
        } else {
            commandSource.sendFeedback(new StringTextComponent("DonorDrive ID has been set to '" + donorDriveID + "'."), true);
        }

        return 1;
    }




}
