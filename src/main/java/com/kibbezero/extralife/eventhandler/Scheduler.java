package com.kibbezero.extralife.eventhandler;

import com.kibbezero.extralife.donordriveclient.Connection;
import com.kibbezero.extralife.donordriveclient.Donation;
import com.kibbezero.extralife.donordriveclient.Participant;
import com.kibbezero.extralife.playercapability.DonorDriveTagCapability;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Scheduler {

    private static long ticks = 0;
    private static ConcurrentHashMap<String, Donation> donations = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();


    public static void startup() {
        Connection connection = new Connection("https://extralife.donordrive.com");
        donations = new ConcurrentHashMap<>();
        try {
            ArrayList<Participant> participants = new ArrayList<>(Arrays.asList(connection.getTeamParticipants("50922")));
            for (Participant participant : participants){
                Donation[] donationList = connection.getParticipantDonations(participant.getParticipantID());

                for(Donation donation: donationList) {
                    donations.put(donation.getDonationID(), donation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Item[] getCraftableItemDonation(World world)
    {
        List<?> itemGroups = Arrays.asList(
                ItemGroup.DECORATIONS,
                ItemGroup.BUILDING_BLOCKS
        );

        return world.getRecipeManager().getRecipes()
                        .stream()
                        .map(r -> r.getRecipeOutput().getItem())
                        .filter(i -> itemGroups.contains(i.getGroup()))
                        .toArray(Item[]::new);
    }

    public static void updateTick(World world){
        ticks++;
        if (ticks >= 1200){
            Connection connection = new Connection("https://extralife.donordrive.com");
            try {
                for (Participant participant : connection.getTeamParticipants("50922")){
                    Donation[] donationsRemote = connection.getParticipantDonations(participant.getParticipantID());

                    for (Donation remoteDonation : donationsRemote) {
                        if(!donations.containsKey(remoteDonation.getDonationID())) {
                            for (PlayerEntity player:world.getPlayers()) {
                                //noinspection ConstantConditions
                                player.getCapability(DonorDriveTagCapability.DONOR_DRIVE_TAG_CAPABILITY).ifPresent(capability -> {
                                    if(capability.getDonorDriveId().equals(remoteDonation.getParticipantID())){
                                        Objects.requireNonNull(world.getServer()).deferTask(() -> {
                                            ProcessDonationEvent(player, remoteDonation);
                                            donations.put(remoteDonation.getDonationID(), remoteDonation);
                                        });
                                    }
                                });
                            }
                        }
                    }

                }
            } catch (IOException e) {
                LOGGER.error(e);
                return;
            }
            ticks = 0;
        }
    }

    public static void ProcessDonationEvent(PlayerEntity player, Donation donation) {
        String chosenIncentiveId = donation.getIncentiveID();

        if (chosenIncentiveId.isEmpty()){
            double amount = donation.getAmount();
            if (amount >= 100.0) {

                Random random = player.getEntityWorld().getRandom();
                for (int witherNum = 0; witherNum < 3; witherNum++) {
                    WitherEntity witherentity = EntityType.WITHER.create(player.getEntityWorld());
                    BlockPos blockpos = new BlockPos(random.nextInt(5000) - 2500, 80, random.nextInt(5000) - 2500);
                    assert witherentity != null;
                    witherentity.setLocationAndAngles((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.55D, (double) blockpos.getZ() + 0.5D, 0.0F , 0.0F);
                    witherentity.renderYawOffset = 0.0F;
                    witherentity.forceSpawn = true;
                    ((ServerWorld) player.getEntityWorld()).summonEntity(witherentity);
                    witherentity.ignite();
                    //noinspection ConstantConditions
                    player.getServer().getPlayerList().sendMessage(new StringTextComponent("The Wither has been spawned at (" + blockpos.getX() + ", " + blockpos.getZ() + ")."), true);
                }
            } else if (amount == 77.77) {
                GiveRandomItemEvent.giveRandomItem(player, new Item[] {Items.DIAMOND}, 64, 64);
                GiveRandomItemEvent.giveRandomItem(player, new Item[] {Items.EMERALD}, 64, 64);
                GiveRandomItemEvent.giveRandomItem(player, new Item[] {Items.GOLD_INGOT}, 64, 64);
                GiveRandomItemEvent.giveRandomItem(player, new Item[] {Items.ENDER_PEARL}, 64, 64);
                //noinspection ConstantConditions
                player.getEntityWorld().getServer().getPlayerList().sendMessage(new StringTextComponent(player.getGameProfile().getName() + " just received a donation...and you received stacks of valuable materials!"));
                player.sendMessage(new StringTextComponent("You got a donation that granted you an item!"));
            } else if (amount >= 75.0) {
                GiveRandomItemEvent.giveItem(player, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
                ItemStack diamondChest = new ItemStack(Items.DIAMOND_CHESTPLATE);
                GiveRandomItemEvent.giveItemRandomEnchantment(player, diamondChest);
                GiveRandomItemEvent.giveItem(player, diamondChest);

                ItemStack diamondBoots = new ItemStack(Items.DIAMOND_BOOTS);
                GiveRandomItemEvent.giveItemRandomEnchantment(player, diamondBoots);
                GiveRandomItemEvent.giveItem(player, diamondBoots);

                ItemStack diamondHelm = new ItemStack(Items.DIAMOND_HELMET);
                GiveRandomItemEvent.giveItemRandomEnchantment(player, diamondHelm);
                GiveRandomItemEvent.giveItem(player, diamondHelm);

                ItemStack diamondLegs = new ItemStack(Items.DIAMOND_LEGGINGS);
                GiveRandomItemEvent.giveItemRandomEnchantment(player, diamondLegs);
                GiveRandomItemEvent.giveItem(player, diamondLegs);

                ItemStack diamondSword = new ItemStack(Items.DIAMOND_SWORD);
                GiveRandomItemEvent.giveItemRandomEnchantment(player, diamondSword);
                GiveRandomItemEvent.giveItem(player, diamondSword);

                //noinspection ConstantConditions
                player.getEntityWorld().getServer().getPlayerList().sendMessage(new StringTextComponent(player.getGameProfile().getName() + " just received a donation...and now is ready for war!"));
                player.sendMessage(new StringTextComponent("You got a donation and now you are ready for war!"));

            } else if (amount == 66.66) {
                if(GiveRandomItemEvent.stealInventory(player)) {
                    player.sendMessage(new StringTextComponent("Someone just donated to Extra Life.... but I wouldn't look at your inventory if I were you."));
                } else {
                    player.sendMessage(new StringTextComponent("Someone just donated to Extra Life! Unfortunately we couldn't find a place to give the 'prize' to you."));
                }
            } else if (amount >= 50.0) {
                List<Item> equipmentList = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.getGroup() != null && (item.getGroup().equals(ItemGroup.TOOLS) || item.getGroup().equals(ItemGroup.COMBAT))).collect(Collectors.toList());
                ItemStack equipmentItem = GiveRandomItemEvent.getRandomItemStack(player.getEntityWorld(), equipmentList.toArray(new Item[0]), 1, 1);
                GiveRandomItemEvent.giveItemRandomEnchantment(player, equipmentItem);
                int count = 0;
                while (true) {
                    assert equipmentItem != null;
                    if (!(!equipmentItem.isEnchanted() && count < 100)) break;
                    count++;
                    equipmentItem = GiveRandomItemEvent.getRandomItemStack(player.getEntityWorld(), equipmentList.toArray(new Item[0]), 1, 1);
                    GiveRandomItemEvent.giveItemRandomEnchantment(player, equipmentItem);
                }

                GiveRandomItemEvent.giveItem(player, equipmentItem);
                player.sendMessage(new StringTextComponent("You got a donation that granted you an item!"));
            } else if (amount >= 25.0) {
                PlayerList playerList = Objects.requireNonNull(player.getServer()).getPlayerList();
                playerList.sendMessage(new StringTextComponent("You have a new Feast or Famine Target"), true);
                for(ServerPlayerEntity serverPlayer: playerList.getPlayers()) {
                    ItemStack book = new ItemStack(Items.WRITTEN_BOOK, 1);
                    CompoundNBT bookTag = book.getOrCreateTag();
                    bookTag.putString("author", serverPlayer.getGameProfile().getName());
                    bookTag.putString("title", "Feast or Famine Target");
                    ListNBT pages = new ListNBT();
                    pages.add(0, StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent("On the next page will be your target. Follow the instructions before you die to get the points."))));

                    int playerIndex = -1;
                    while(playerIndex == -1 || (playerList.getPlayers().get(playerIndex) == serverPlayer && playerList.getCurrentPlayerCount() > 1)) {
                        playerIndex = serverPlayer.getEntityWorld().getRandom().nextInt(playerList.getCurrentPlayerCount());
                    }
                    ServerPlayerEntity target = playerList.getPlayers().get(playerIndex);
                    boolean isFeast = serverPlayer.getServerWorld().getRandom().nextBoolean();
                    String feastOrFamineLine;
                    if(isFeast) {
                        ItemStack targetItem = GiveRandomItemEvent.getRandomItemStack(serverPlayer.getServerWorld(), ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.getGroup() != null && (item.getGroup().equals(ItemGroup.BUILDING_BLOCKS) || item.getGroup().equals(ItemGroup.DECORATIONS) || item.getGroup().equals(ItemGroup.TOOLS) || item.getGroup().equals(ItemGroup.COMBAT))).toArray(Item[]::new), 1, 64);
                        assert targetItem != null;
                        feastOrFamineLine = "Give " + target.getGameProfile().getName() + " " + targetItem.getCount() + " " + targetItem.getDisplayName().getFormattedText();
                    } else {
                        feastOrFamineLine = "Kill " + target.getGameProfile().getName();
                    }
                    pages.add(1, StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent(feastOrFamineLine))));
                    bookTag.put("pages", pages);
                    GiveRandomItemEvent.giveItem(serverPlayer, book);
                }

            } else if (amount >= 15.0) {

                ItemStack book = new ItemStack(Items.WRITTEN_BOOK, 1);
                CompoundNBT bookTag = book.getOrCreateTag();
                bookTag.putString("author", player.getGameProfile().getName());
                bookTag.putString("title", "Do this thing, get some Points");
                ListNBT pages = new ListNBT();
                LinkedList<String> optionsList = new LinkedList<>();
                optionsList.add("Build a tower to the sky. 10 points");
                optionsList.add("Lose an arena match. 25 points");
                optionsList.add("Donate to another player's page. 100 points");
                optionsList.add("Complete a full beacon tower with Gold blocks. 100 points");
                optionsList.add("Swim in lava for 30 seconds. 35 points");
                optionsList.add("Hit someone with a snowball. 5 points");
                optionsList.add("Create an underwater base. 100 points");
                optionsList.add("Spend 6 Minecraft days underground. 50 points");
                optionsList.add("Stay off of non-plant blocks for 6 Minecraft days. 50 points");
                int pickedIndex = player.getEntityWorld().getRandom().nextInt(optionsList.size());
                pages.add(0, StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent("On the next page will see a task to perform and how many points it is worth. These tasks are optional you must destroy the book before you receive the points."))));
                pages.add(1, StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent(optionsList.get(pickedIndex)))));
                bookTag.put("pages", pages);
                GiveRandomItemEvent.giveItem(player, book);

            } else if (amount >= 10.0) {
                Item[] items = new Item[] {Items.GOLD_INGOT, Items.IRON_INGOT};
                GiveRandomItemEvent.giveRandomItem(player, items, 32, 64);
                GiveRandomItemEvent.giveRandomItem(player, items, 32, 64);
                GiveRandomItemEvent.giveRandomItem(player, items, 32, 64);
                GiveRandomItemEvent.giveRandomItem(player, items, 32, 64);
            } else if (amount == 6.66) {
                double playerx = player.chasingPosX;
                double playery = player.chasingPosY;
                double playerz = player.chasingPosZ;
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                ServerWorld world = serverPlayer.getServerWorld();
                LightningBoltEntity no = new LightningBoltEntity(world, playerx, playery, playerz, false);
                world.addLightningBolt(no);
            } else if (amount >= 5.0) {
                World world = player.getEntityWorld();
                ItemStack materialItem = GiveRandomItemEvent.getRandomItemStack(world, getCraftableItemDonation(world), 64, 64);
                for (int i = 0; i < 6; i++) {
                    assert materialItem != null;
                    GiveRandomItemEvent.giveItem(player, materialItem.copy());
                }
            }
        } else {
            if(chosenIncentiveId.equals("A85C9A9C-EDA2-2ECC-F952F2E458322C57")) {
                Item[] items = {Items.GRAVEL};
                GiveRandomItemEvent.giveRandomItem(player, items, 1, 64);
            }
        }


    }
}
