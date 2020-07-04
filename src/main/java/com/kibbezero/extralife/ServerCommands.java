package com.kibbezero.extralife;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ServerCommands {

    public ServerCommands (){

    }

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();


    public static void register(final CommandDispatcher<CommandSource> dispatcher){

        LOGGER.info("Registering Server Commands");

        dispatcher.register(Commands.literal("ExtraLife"));

    }

}
