package com.kibbezero.extralife;

import com.kibbezero.extralife.donordriveclient.Connection;
import com.kibbezero.extralife.donordriveclient.Donation;
import com.kibbezero.extralife.donordriveclient.Participant;
import com.kibbezero.extralife.playercapability.DonorDriveTagCapability;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("extralife")
public class ExtraLife
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private Connection donorConnection; //Initialized during onServerStart

    public ExtraLife() {

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));


        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onDedicatedServerStarting);




        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event)
    {

        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        donorConnection = new Connection("https://try.donordrive.com");
        DonorDriveTagCapability.register();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("extralife", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }



    public void onDedicatedServerStarting(final FMLDedicatedServerSetupEvent event) {
        


        LOGGER.info(String.format("Loaded DonorDrive Connection: %s",donorConnection.getDonorSite()));

        try {
            Participant participant = donorConnection.getParticipant("19793");
            LOGGER.info(String.format("Found %s! Participant ID: %s", participant.getDisplayName(), participant.getParticipantID()));

            Participant[] codeMonkeys = donorConnection.getTeamParticipants("8897");
            LOGGER.info(Arrays.toString(codeMonkeys));

            Donation[] donations = donorConnection.getParticipantDonations("19793");
            LOGGER.info(Arrays.toString(donations));

        } catch (IOException exception) {
            LOGGER.error("Cannot get data from Donor Drive.", exception);
        }
        LOGGER.info("HELLO from server starting");


    }
}
