// GPLv3 + custom | Copyright theo546 - github.com/theo546

package com.theo546.randomdrop;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack itemstack = item.getItemStack();

        if (itemstack.hasItemMeta()) {
            ItemMeta meta = itemstack.getItemMeta();
            if (meta != null && meta.hasLore()) {
                for (String line : meta.getLore()) {
                    if (line.equals(Main.CLAIMED_LORE_TEXT)) {
                        return;
                    }
                }
            }
        }

        item.setItemStack(Main.randomizeItemStack(itemstack, true, Main.RANDOMIZE_DURABILITY));
    }
    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack result = inv.getResult();
        if (result == null) return;

        ItemMeta meta = result.hasItemMeta()
                ? result.getItemMeta()
                : Bukkit.getServer().getItemFactory().getItemMeta(result.getType());

        if (Main.CLAIM_CRAFTED_ITEMS && !Main.RANDOMIZE_CRAFT) {
            meta.setLore(Arrays.asList(Main.CLAIMED_LORE_TEXT));
            result.setItemMeta(meta);
        }
        if (Main.RANDOMIZE_CRAFT) {
            inv.setResult(Main.randomizeItemStack(result, Main.CLAIM_CRAFTED_ITEMS, Main.RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS));
        }
        if (Main.RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS && !Main.RANDOMIZE_CRAFT) {
            result.setItemMeta(Main.randomizeDurability(true, result, meta));
        }
    }
}
