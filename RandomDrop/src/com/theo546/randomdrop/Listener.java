// GPLv3 + custom | Copyright theo546 - github.com/theo546

package com.theo546.randomdrop;

import java.util.Arrays;
import java.util.Iterator;
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
		
		if(itemstack.hasItemMeta() == true) {
			ItemMeta itemmeta = itemstack.getItemMeta();
			if(itemmeta.hasLore() == true) {
				List<String> str_list = itemmeta.getLore();
				Iterator<String> iterator = str_list.iterator();
				while(iterator.hasNext()) {
					if(iterator.next().equals(Main.CLAIMED_LORE_TEXT)) {
						return;
					}
				}
			}
		}
		item.setItemStack(Main.randomizeItemStack(itemstack, true, Main.RANDOMIZE_DURABILITY));
	}
	@EventHandler
	public void onItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory result = event.getInventory();
		ItemStack itemstack_result = result.getResult();
		if(itemstack_result == null) return;
		
		ItemMeta itemmeta_result;
		if(itemstack_result.hasItemMeta() == false) {
			itemmeta_result = Bukkit.getServer().getItemFactory().getItemMeta(itemstack_result.getType());
		}
		else {
			itemmeta_result = itemstack_result.getItemMeta();
		}
		if(Main.CLAIM_CRAFTED_ITEMS == true && Main.RANDOMIZE_CRAFT == false) {
			List<String> str = Arrays.asList(Main.CLAIMED_LORE_TEXT);
			itemmeta_result.setLore(str);
			itemstack_result.setItemMeta(itemmeta_result);
		}
		if(Main.RANDOMIZE_CRAFT == true) {
			result.setResult(Main.randomizeItemStack(itemstack_result, Main.CLAIM_CRAFTED_ITEMS, Main.RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS));
		}
		if(Main.RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS == true && Main.RANDOMIZE_CRAFT == false) {
			itemstack_result.setItemMeta(Main.randomizeDurability(true, itemstack_result));
		}
	}
}
