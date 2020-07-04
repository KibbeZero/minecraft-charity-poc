package com.kibbezero.extralife.donordriveclient;

import com.kibbezero.extralife.ServerCommands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber
@OnlyIn(Dist.DEDICATED_SERVER)
public class FMLEventHandler {

    @SubscribeEvent
    public static void onServerStart(final FMLServerStartingEvent event){
        ServerCommands.register(event.getCommandDispatcher());
    }
}
