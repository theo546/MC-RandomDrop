// GPLv3 + custom | Copyright theo546 - github.com/theo546

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

import com.theo546.randomdrop.Listener;

public class Main extends JavaPlugin {
	public static boolean RANDOMIZE_DURABILITY;
	public static boolean RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS;
	public static String CLAIMED_LORE_TEXT;
	public static int SEED;
	public static boolean KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM;
	public static boolean KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE;
	public static boolean CLAIM_CRAFTED_ITEMS;
	public static boolean RANDOMIZE_CRAFT;
	public static boolean PAST_FLATTENING;
	
	@Override
	public void onEnable() {
		getConfig().addDefault("RANDOMIZE_DURABILITY", false);
		getConfig().addDefault("RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS", false);
		getConfig().addDefault("CLAIMED_LORE_TEXT", "§r§7§lCLAIMED");
		getConfig().addDefault("SEED", System.currentTimeMillis());
		getConfig().addDefault("KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM", false);
		getConfig().addDefault("KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE", false);
		getConfig().addDefault("CLAIM_CRAFTED_ITEMS", true);
		getConfig().addDefault("RANDOMIZE_CRAFT", false);
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
		
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].replaceAll("v", "").replaceAll("_", "").split("R")[0];
		if(version.endsWith(".")) version = version.substring(0, version.length() - 1);
		int version_int = Integer.valueOf(version);
		
		if(version_int < 113) {
			PAST_FLATTENING = false;
		}
		else {
			PAST_FLATTENING = true;
		}
		
		getServer().getPluginManager().registerEvents(new Listener(), this);
	}
	
	public static Material getRandomItemFromItemWithSeed(Material material, int fallback, int magic_id) {
		int count = 0;
		int block_nbr = 0;
		ArrayList<Material> names = new ArrayList<>();
		for (Material material_loop : Material.values()) {
			if(material_loop == material) {
				block_nbr = count;
			}
			names.add(material_loop);
			count++;
		}
		int random_int = pseudoRandom(SEED, count, block_nbr, fallback, magic_id);
		
		return names.get(random_int);
	}
	@SuppressWarnings("deprecation")
	public static ItemStack randomizeItemStack(ItemStack itemstack, boolean claim, boolean randomize_durability) {
		Material material = itemstack.getType();
		int itemstack_amount = itemstack.getAmount();
		
		byte magic_id = 0;
		if(Main.PAST_FLATTENING == false) {
			// PRE-FLATTENING SUPPORT
			ItemStack itemstack_check = itemstack;
			itemstack_check.setDurability((short) 0);
			magic_id = itemstack.getData().getData();
		}
		
		int loop = 0;
		Material item_to_drop;
		while (true) {
			item_to_drop = Main.getRandomItemFromItemWithSeed(material, loop, magic_id);
			try {
				ItemStack itemstack_drop = itemstack;
				itemstack_drop.setType(item_to_drop);
				
				ItemMeta itemmeta_drop;
				if(itemstack.hasItemMeta() == false) {
					itemmeta_drop = Bukkit.getServer().getItemFactory().getItemMeta(itemstack_drop.getType());
				}
				else {
					itemmeta_drop = itemstack.getItemMeta();
				}
				
				List<String> str = Arrays.asList(Main.CLAIMED_LORE_TEXT);
				
				if(claim == true) {
					itemmeta_drop.setLore(str);
				}

				if(Main.KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM == false) {
					if(itemmeta_drop.hasEnchants()) {
						for(Enchantment e: itemmeta_drop.getEnchants().keySet()) {
							itemmeta_drop.removeEnchant(e);
						}
					}
				}
				
				if(Main.KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE == false) {
					if(itemmeta_drop.hasDisplayName()) {
						itemmeta_drop.setDisplayName("");
					}
				}
				
				itemmeta_drop = randomizeDurability(randomize_durability, itemstack_drop);
				
				itemstack_drop.setAmount(itemstack_amount);
				itemstack_drop.setItemMeta(itemmeta_drop);
				
				return itemstack_drop;
			}
			catch (java.lang.IllegalArgumentException | java.lang.NullPointerException ex) {
				loop ++;
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static ItemMeta randomizeDurability(boolean randomize_durability, ItemStack itemstack) {
		int item_max_durability = itemstack.getType().getMaxDurability();
		ItemMeta itemmeta = itemstack.getItemMeta();
		
		if(item_max_durability != 0) {
			try {
				// 1.13+
				if(itemmeta instanceof Damageable) {
					if(randomize_durability == false) {
						((Damageable) itemmeta).setDamage(0);
					}
					else {
						int random = 0 + (int) (Math.random() * ((item_max_durability - 0) + 1));
						((Damageable) itemmeta).setDamage(random);
					}
				}
			}
			catch (java.lang.NoClassDefFoundError e) {
				// 1.8 support
				if(randomize_durability == false) {
					itemstack.setDurability((short) 0);
				}
				else {
					short random2 = (short) (0 + (int) (Math.random() * ((item_max_durability - 0) + 1)));
					itemstack.setDurability(random2);
				}
			}
		}
		return itemmeta;
	}
	public static int pseudoRandom(int seed, int i, int add, int fallback, int magic_id) {
		Random randnum = new Random();
		randnum.setSeed(seed*add+fallback+magic_id);
		int result = randnum.nextInt(i);
		return result;
	}
}
