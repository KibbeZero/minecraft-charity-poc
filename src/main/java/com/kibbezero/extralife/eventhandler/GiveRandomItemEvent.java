package com.kibbezero.extralife.eventhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
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

    public static void giveRandomItem (PlayerEntity player, Item[] items, int minStack, int maxStack){
        ItemStack item = getRandomItemStack(player.getEntityWorld(), items, minStack, maxStack);
        if (item != null && !player.inventory.addItemStackToInventory(item)) {

            player.dropItem(item, true, true);
        }


        //noinspection ConstantConditions
        player.getEntityWorld().getServer().getPlayerList().sendMessage(new StringTextComponent(player.getGameProfile().getName() + " just received a donation...and some gravel!"));
        player.sendMessage(new StringTextComponent("You got a donation and some gravel!"));
        player.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5f, 1.0f);
    }
}
