package com.kibbezero.extralife.blocks;

import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.swing.text.html.parser.Entity;

public class WinklePoopBlock extends ModFlower {
    public WinklePoopBlock(Effect p_i49984_1_, int effectDuration, Properties properties) {
        super(p_i49984_1_, effectDuration, properties);
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
    }

    @Override
    protected boolean isValidGround(final BlockState state, final IBlockReader worldIn, final BlockPos pos) {
        Block block = state.getBlock();
        return !block.equals(Blocks.AIR) && !block.equals(Blocks.CAVE_AIR) && !block.equals(Blocks.VOID_AIR) && !block.equals(this);
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) {
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
        return BlockRenderType.MODEL;
    }
}