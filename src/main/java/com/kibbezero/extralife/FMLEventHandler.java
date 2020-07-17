package com.kibbezero.extralife;

import com.kibbezero.extralife.eventhandler.Scheduler;
import com.kibbezero.extralife.playercapability.DonorDriveTagCapability;
import com.kibbezero.extralife.servercommands.ServerCommands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@SuppressWarnings("ConstantConditions") //Suppressed because @CapabilityInject takes care of instantiating DONOR_DRIVE_TAG_CAPABILITY
@Mod.EventBusSubscriber(value={Dist.DEDICATED_SERVER}, bus=Mod.EventBusSubscriber.Bus.FORGE, modid="extralife")
public class FMLEventHandler {

    @SubscribeEvent
    public static void onServerStart(final FMLServerStartingEvent event){
        ServerCommands.register(event.getCommandDispatcher());
    }

    @SubscribeEvent
    public static void onAttachingCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event){

        if(event.getObject() instanceof ServerPlayerEntity){
            event.addCapability(new ResourceLocation("extralife", "donordriveid"), new DonorDriveTagCapability());
        }



    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event){
        if (event.isWasDeath()) {
            PlayerEntity player = event.getPlayer();
            event.getOriginal().getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY, null)
                    .ifPresent(source -> player.getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY, null)
                    .ifPresent(newDonorDriveTag -> newDonorDriveTag.setPlayerAssociation(source.getDonorDriveId())));
        }
    }

    @SubscribeEvent
    public static void onTick(WorldTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            Scheduler.updateTick(event.world);
        }
    }
}
