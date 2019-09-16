// GPLv3 + custom | Copyright theo546 - github.com/theo546

package com.theo546.randomdrop;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.theo546.randomdrop.Listener;

public class Main extends JavaPlugin {
	public static boolean RANDOMIZE_DURABILITY;
	public static String CLAIMED_LORE_TEXT;
	public static int SEED;
	public static boolean KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM;
	public static boolean KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE;
	
	@Override
	public void onEnable() {
		getConfig().addDefault("RANDOMIZE_DURABILITY", false);
		getConfig().addDefault("CLAIMED_LORE_TEXT", "§r§7§lCLAIMED");
		getConfig().addDefault("SEED", System.currentTimeMillis());
		getConfig().addDefault("KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM", false);
		getConfig().addDefault("KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE", false);
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		RANDOMIZE_DURABILITY = getConfig().getBoolean("RANDOMIZE_DURABILITY");
		CLAIMED_LORE_TEXT = getConfig().getString("CLAIMED_LORE_TEXT");
		SEED = getConfig().getInt("SEED");
		KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM = getConfig().getBoolean("KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM");
		KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE = getConfig().getBoolean("KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE");
		
		getServer().getPluginManager().registerEvents(new Listener(), this);
	}
	
	public static Material getRandomItemFromItemWithSeed(Material material, int fallback) {
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
		int random_int = pseudoRandom(SEED, count, block_nbr, fallback);
		
		return names.get(random_int);
	}
	public static int pseudoRandom(int seed, int i, int add, int fallback) {
		Random randnum = new Random();
		randnum.setSeed(seed*add+fallback);
		int result = randnum.nextInt(i);
		return result;
	}
}
