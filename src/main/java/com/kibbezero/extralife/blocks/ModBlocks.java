package com.kibbezero.extralife.blocks;

import com.kibbezero.extralife.ExtraLife;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.SoundType;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.swing.text.html.parser.Entity;

public class ModBlocks {
    // deferred registration for mod blocks
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, "extralife");

    // register Winkle's poop
    public static final RegistryObject<Block> WINKLE_POOP_BLOCK = BLOCKS.register("winkle_poop_block", () ->
        new WinklePoopBlock(
            Effects.NAUSEA,
            12,
            Block.Properties
                .create(Material.PLANTS)
                .doesNotBlockMovement()
                .notSolid()
                .harvestLevel(1)
                .sound(SoundType.SLIME)
                .slipperiness(3.0f)
        )
    );
}