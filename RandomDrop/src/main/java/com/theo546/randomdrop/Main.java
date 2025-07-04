// RandomDrop License | Copyright theo546 - github.com/theo546
package com.theo546.randomdrop;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
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
    public static boolean PERSIST_LOOT_TABLE;
    public static Map<Material, Material> LOOT_TABLE;

    private File getLootTableFile() {
        String hash = sha256(String.valueOf(SEED)).substring(0, 16);
        return new File(getDataFolder(), "loot_table_" + hash + ".yml");
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : encoded) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

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
        getConfig().addDefault("PERSIST_LOOT_TABLE", true);
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
        PERSIST_LOOT_TABLE = getConfig().getBoolean("PERSIST_LOOT_TABLE");

        initLootTable();
        getServer().getPluginManager().registerEvents(new Listener(), this);
    }

    private void initLootTable() {
        LOOT_TABLE = new HashMap<>();
        File file = getLootTableFile();
        if (PERSIST_LOOT_TABLE && !getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        World world = Bukkit.getWorlds().get(0);
        Location spawn = world.getSpawnLocation().clone();
        spawn.setY(255);

        List<Material> valid = new ArrayList<>();
        for (Material m : Material.values()) {
            try {
                Item it = world.dropItem(spawn, new ItemStack(m));
                if (it != null && !it.isDead()) {
                    valid.add(m);
                    it.remove();
                }
            } catch (Exception ignored) {
            }
        }

        YamlConfiguration yaml = new YamlConfiguration();
        boolean updated = false;
        if (PERSIST_LOOT_TABLE && file.exists()) {
            try {
                yaml.load(file);
                for (String key : yaml.getKeys(false)) {
                    Material k = Material.getMaterial(key);
                    Material v = Material.getMaterial(yaml.getString(key));
                    if (k != null && v != null && k != v) {
                        LOOT_TABLE.put(k, v);
                    } else {
                        updated = true;
                    }
                }
            } catch (Exception e) {
                getLogger().warning("Failed to load loot table: " + e.getMessage());
            }
        }

        List<Material> shuffled = new ArrayList<>(valid);
        Collections.shuffle(shuffled, new Random(SEED));
        shuffled.removeAll(LOOT_TABLE.values());

        for (Material m : valid) {
            if (!LOOT_TABLE.containsKey(m)) {
                Material candidate = null;
                while (!shuffled.isEmpty()) {
                    Material choice = shuffled.remove(0);
                    if (choice != m) {
                        candidate = choice;
                        break;
                    }
                }
                if (candidate != null) {
                    LOOT_TABLE.put(m, candidate);
                    updated = true;
                }
            }
        }

        if (PERSIST_LOOT_TABLE && (updated || !file.exists())) {
            YamlConfiguration out = new YamlConfiguration();
            for (Map.Entry<Material, Material> entry : LOOT_TABLE.entrySet()) {
                out.set(entry.getKey().name(), entry.getValue().name());
            }
            try {
                out.save(file);
            } catch (IOException e) {
                getLogger().warning("Failed to save loot table: " + e.getMessage());
            }
        }
    }

    public static ItemStack randomizeItemStack(ItemStack itemstack, boolean claim, boolean randomizeDurability) {
        Material material = itemstack.getType();
        Material itemToDrop = LOOT_TABLE.getOrDefault(material, material);
        int itemstackAmount = itemstack.getAmount();

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

}
