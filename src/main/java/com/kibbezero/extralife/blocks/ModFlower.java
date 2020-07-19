package com.kibbezero.extralife.blocks;

import java.util.Random;
import net.minecraft.block.*;
import net.minecraft.item.DyeColor;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModFlower extends FlowerBlock {
//    protected ModFlower(DyeColor color, Properties properties) {
//        super(flowerEffect(color), 8, properties);
//        this.color = color;
//    }
    public ModFlower(Effect p_i49984_1_, int effectDuration, Properties properties) {
        super(p_i49984_1_, effectDuration, properties);
    }
}