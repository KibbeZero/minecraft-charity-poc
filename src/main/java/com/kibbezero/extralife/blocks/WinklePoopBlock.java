package com.kibbezero.extralife.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nonnull;

public class WinklePoopBlock extends ModFlower {
    public WinklePoopBlock(Effect p_i49984_1_, int effectDuration, Properties properties) {
        super(p_i49984_1_, effectDuration, properties);
        if(FMLEnvironment.dist.isClient()) {
            RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
        }
    }

    @Override
    protected boolean isValidGround(final BlockState state, @Nonnull final IBlockReader worldIn, @Nonnull final BlockPos pos) {
        Block block = state.getBlock();
        return !block.equals(Blocks.AIR) && !block.equals(Blocks.CAVE_AIR) && !block.equals(Blocks.VOID_AIR) && !block.equals(this);
    }

    @Nonnull
    @SuppressWarnings("deprecation") //It says that overrides are fine, you just shouldn't use the base class (which means don't super)
    @Override
    public BlockRenderType getRenderType(@Nonnull final BlockState state) {
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
        return BlockRenderType.MODEL;
    }
}