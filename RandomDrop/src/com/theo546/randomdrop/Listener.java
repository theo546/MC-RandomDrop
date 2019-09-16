// GPLv3 + custom | Copyright theo546 - github.com/theo546

package com.theo546.randomdrop;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Listener implements org.bukkit.event.Listener {
    @SuppressWarnings("deprecation")
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
    	
    	Material material = itemstack.getType();
    	int itemstack_amount = itemstack.getAmount();
    	
    	int loop = 0;
    	Material item_to_drop;
    	while (true) {
    		item_to_drop = Main.getRandomItemFromItemWithSeed(material, loop);
    		try {
    			ItemStack itemstack_drop = itemstack;
    			itemstack_drop.setType(item_to_drop);
    			ItemMeta itemmeta_drop = itemstack.getItemMeta();
    			
            	List<String> str = Arrays.asList(Main.CLAIMED_LORE_TEXT);
            	
        		itemmeta_drop.setLore(str);

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

    			
            	int item_max_durability = item_to_drop.getMaxDurability();
            	
            	if(item_max_durability != 0) {
            		try {
            			// 1.13+
	                    if(itemmeta_drop instanceof Damageable) {
	                    	if(Main.RANDOMIZE_DURABILITY == false) {
	                    		((Damageable) itemmeta_drop).setDamage(0);
	                    	}
	                    	else {
	                    		int nombreAleatoire = 0 + (int)(Math.random() * ((item_max_durability - 0) + 1));
	                    		((Damageable) itemmeta_drop).setDamage(nombreAleatoire);
	                    	}
	                    }
            		}
	                catch (java.lang.NoClassDefFoundError e) {
	                	// 1.8 support
	                	if(Main.RANDOMIZE_DURABILITY == false) {
	                		short durability = 0;
	                		itemstack_drop.setDurability(durability);
	                	}
	                	else {
	                		short nombreAleatoire2 = (short) (0 + (int)(Math.random() * ((item_max_durability - 0) + 1)));
	                		itemstack_drop.setDurability(nombreAleatoire2);
	                	}
            		}
            	}
            	
            	itemstack_drop.setAmount(itemstack_amount);
            	itemstack_drop.setItemMeta(itemmeta_drop);
            	
            	item.setItemStack(itemstack_drop);
            	
            	break;
    		}
    		catch (java.lang.IllegalArgumentException | java.lang.NullPointerException ex) {
        		loop ++;
    		}
    	}
	}
}
