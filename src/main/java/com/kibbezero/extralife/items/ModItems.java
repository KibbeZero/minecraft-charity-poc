package com.kibbezero.extralife.items;

import com.kibbezero.extralife.blocks.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    // deferred register for items
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "extralife");

    // register item and link with equivalent block if it should be placeable
//    public static final RegistryObject<Item> WINKLE_POOP = ITEMS.register("winkle_poop", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WINKLE_POOP_BLOCK = ITEMS.register("winkle_poop_block", () -> new BlockItem(ModBlocks.WINKLE_POOP_BLOCK.get(), new Item.Properties()));
}