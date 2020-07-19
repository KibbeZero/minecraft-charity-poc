package com.kibbezero.extralife;

import com.kibbezero.extralife.blocks.ModBlocks;
import com.kibbezero.extralife.donordriveclient.Connection;
import com.kibbezero.extralife.donordriveclient.Scheduler;
import com.kibbezero.extralife.items.ModItems;
import com.kibbezero.extralife.playercapability.DonorDriveTagCapability;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onDedicatedServerStarting);

        MinecraftForge.EVENT_BUS.register(this);

        // Register custom blocks and items
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event) {
        DonorDriveTagCapability.register();
    }

    public void onDedicatedServerStarting(final FMLDedicatedServerSetupEvent event) {
        Scheduler.startup();
    }
}
