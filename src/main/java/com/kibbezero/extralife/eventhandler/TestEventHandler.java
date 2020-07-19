package com.kibbezero.extralife.eventhandler;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value={Dist.DEDICATED_SERVER}, bus=Mod.EventBusSubscriber.Bus.FORGE, modid="extralife")
public class TestEventHandler {
    @SubscribeEvent
    public static void onEntityItemPickupEvent(EntityItemPickupEvent event) {
        if(event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
             if(event.getItem().getItem().getItem() == Items.DIAMOND_BLOCK) {
                 double playerx = player.chasingPosX;
                 double playery = player.chasingPosY;
                 double playerz = player.chasingPosZ;
                 ServerWorld world = player.getServerWorld();
                 LightningBoltEntity no = new LightningBoltEntity(world, playerx, playery, playerz, false);
                 world.addLightningBolt(no);

             }
        }
    }
}
