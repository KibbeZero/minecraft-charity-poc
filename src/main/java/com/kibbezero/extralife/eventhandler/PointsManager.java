package com.kibbezero.extralife.eventhandler;

import net.minecraft.entity.player.PlayerEntity;

public class PointsManager {

    public static void addPoints(PlayerEntity player, int points) {
        player.addScore(points);
    }
}
