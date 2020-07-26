package com.kibbezero.extralife.eventhandler;

import com.kibbezero.extralife.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GiveRandomItemEvent {

    private static final Logger LOGGER = LogManager.getLogger();

    public static ItemStack getRandomItemStack (World world, Item[] items, int minStack, int maxStack) {

        if (minStack > maxStack){
            LOGGER.warn("MintSack is bigger than MaxStack. Returning Nothing.");
            return null;
        }

        int random = world.rand.nextInt(items.length);
        ItemStack randomItemStack = new ItemStack(items[random]);
        int trueMaxStack = Math.min(maxStack, randomItemStack.getMaxStackSize());
        int trueMinStack = Math.min(minStack, randomItemStack.getMaxStackSize());
        int randomSize = Math.max(world.rand.nextInt(trueMaxStack), trueMinStack);

        randomItemStack.setCount(randomSize);
        return randomItemStack;
    }

    public static BlockPos getSpotForDoubleChestAbovePlayer (PlayerEntity player) {
        BlockPos cursor = player.getPosition().up(10);

        while (!player.getEntityWorld().isAirBlock(cursor) && !player.getEntityWorld().isAirBlock(cursor.east())){
            cursor = cursor.up();
        }

        if (cursor.getY() > player.getEntityWorld().getMaxHeight()) {
            //Do nothing
            return BlockPos.ZERO;
        }

        return cursor;
    }

    public static void giveRandomItem (PlayerEntity player, Item[] items, int minStack, int maxStack){
        ItemStack item = getRandomItemStack(player.getEntityWorld(), items, minStack, maxStack);

        giveItem(player, item);
    }

    public static boolean stealInventory (PlayerEntity player) {
        BlockPos chestPos = getSpotForDoubleChestAbovePlayer(player);
        if (chestPos.equals(BlockPos.ZERO)) {
            return false;
        }

        BlockState block = Blocks.CHEST.getDefaultState();
        BlockState block2 = Blocks.CHEST.getDefaultState();
        World world = player.getEntityWorld();
        world.setBlockState(chestPos, block);
        world.setBlockState(chestPos.east(), block2);

        final ChestTileEntity chest = (ChestTileEntity) world.getTileEntity(chestPos);
        final ChestTileEntity chest2 = (ChestTileEntity) world.getTileEntity(chestPos.east());
        for(int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack itemStack = player.inventory.mainInventory.get(i);
            assert chest != null;
            if(i < chest.getSizeInventory()) {
                chest.setInventorySlotContents(i, itemStack);
            } else {
                assert chest2 != null;
                chest2.setInventorySlotContents(i - chest.getSizeInventory(), itemStack);
            }
            player.inventory.mainInventory.set(i, getRandomItemStack(world, new Item[] {ModBlocks.WINKLE_POOP_BLOCK.get().asItem()}, 64, 64));
        }
        return true;
    }

    public static ItemStack giveItemRandomEnchantment(PlayerEntity player, ItemStack item) {
        EnchantmentHelper.addRandomEnchantment(player.getRNG(), item, player.getRNG().nextInt(20) + 10, true);
        return item;
    }


    public static void giveItem(PlayerEntity player, ItemStack item) {
        if (item != null && !player.inventory.addItemStackToInventory(item)) {

            player.dropItem(item, true, true);
        }
    }
}
