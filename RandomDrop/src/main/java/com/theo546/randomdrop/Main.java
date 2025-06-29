package com.theo546.randomdrop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static boolean RANDOMIZE_DURABILITY;
    public static boolean RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS;
    public static String CLAIMED_LORE_TEXT;
    public static int SEED;
    public static boolean KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM;
    public static boolean KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE;
    public static boolean CLAIM_CRAFTED_ITEMS;
    public static boolean RANDOMIZE_CRAFT;

    @Override
    public void onEnable() {
        getConfig().addDefault("RANDOMIZE_DURABILITY", false);
        getConfig().addDefault("RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS", false);
        getConfig().addDefault("CLAIMED_LORE_TEXT", "§r§7§lCLAIMED");
        getConfig().addDefault("SEED", System.currentTimeMillis());
        getConfig().addDefault("KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM", false);
        getConfig().addDefault("KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE", false);
        getConfig().addDefault("CLAIM_CRAFTED_ITEMS", true);
        getConfig().addDefault("RANDOMIZE_CRAFT", true);
        getConfig().options().copyDefaults(true);
        saveConfig();

        RANDOMIZE_DURABILITY = getConfig().getBoolean("RANDOMIZE_DURABILITY");
        RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS = getConfig().getBoolean("RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS");
        CLAIMED_LORE_TEXT = getConfig().getString("CLAIMED_LORE_TEXT");
        SEED = getConfig().getInt("SEED");
        KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM = getConfig().getBoolean("KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM");
        KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE = getConfig().getBoolean("KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE");
        CLAIM_CRAFTED_ITEMS = getConfig().getBoolean("CLAIM_CRAFTED_ITEMS");
        RANDOMIZE_CRAFT = getConfig().getBoolean("RANDOMIZE_CRAFT");

        getServer().getPluginManager().registerEvents(new Listener(), this);
    }

    public static Material getRandomItemFromItemWithSeed(Material material, int fallback) {
        int count = 0;
        int blockNbr = 0;
        List<Material> names = new ArrayList<>();
        for (Material m : Material.values()) {
            if (m == material) {
                blockNbr = count;
            }
            names.add(m);
            count++;
        }
        int randomInt = pseudoRandom(SEED, count, blockNbr, fallback, 0);
        return names.get(randomInt);
    }

    public static ItemStack randomizeItemStack(ItemStack itemstack, boolean claim, boolean randomizeDurability) {
        Material material = itemstack.getType();
        int itemstackAmount = itemstack.getAmount();

        int loop = 0;
        while (true) {
            Material itemToDrop = getRandomItemFromItemWithSeed(material, loop);
            try {
                ItemStack itemstackDrop = itemstack.clone();
                itemstackDrop.setType(itemToDrop);

                ItemMeta itemmetaDrop = itemstack.hasItemMeta()
                        ? itemstack.getItemMeta()
                        : Bukkit.getServer().getItemFactory().getItemMeta(itemstackDrop.getType());

                if (claim) {
                    itemmetaDrop.setLore(Arrays.asList(CLAIMED_LORE_TEXT));
                }

                if (!KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM && itemmetaDrop.hasEnchants()) {
                    for (Enchantment e : itemmetaDrop.getEnchants().keySet()) {
                        itemmetaDrop.removeEnchant(e);
                    }
                }

                if (!KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE && itemmetaDrop.hasDisplayName()) {
                    itemmetaDrop.setDisplayName("");
                }

                itemmetaDrop = randomizeDurability(randomizeDurability, itemstackDrop, itemmetaDrop);

                itemstackDrop.setAmount(itemstackAmount);
                itemstackDrop.setItemMeta(itemmetaDrop);
                return itemstackDrop;
            } catch (IllegalArgumentException | NullPointerException ex) {
                loop++;
            }
        }
    }

    public static ItemMeta randomizeDurability(boolean randomizeDurability, ItemStack itemstack, ItemMeta itemmeta) {
        int max = itemstack.getType().getMaxDurability();
        if (max != 0 && itemmeta instanceof Damageable damageable) {
            if (!randomizeDurability) {
                damageable.setDamage(0);
            } else {
                int random = (int) (Math.random() * (max + 1));
                damageable.setDamage(random);
            }
        }
        return itemmeta;
    }

    public static int pseudoRandom(int seed, int i, int add, int fallback, int magicId) {
        Random randnum = new Random();
        randnum.setSeed(seed * add + fallback + magicId);
        int result = randnum.nextInt(i);
        return result;
    }
}
