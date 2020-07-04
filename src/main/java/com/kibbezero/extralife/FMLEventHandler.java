package com.kibbezero.extralife;

import com.kibbezero.extralife.playercapability.DonorDriveTagCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

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
}
