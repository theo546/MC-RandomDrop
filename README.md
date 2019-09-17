# RandomDrop
is a plugin to randomize the items you are normally supposed to get when an item entity drop.

This plugin has been tested under 1.14.4 (Paper) and 1.8.8 (Spigot).

This plugin has been inspired by a French Minecraft video series, but since the maker of the plugin wasn't willing to share it I just decided to make an identical one to share for the public.

# How to build?
Clone the repository using git, then run `sh build-tools.sh` inside the cloned repository to build the Spigot 1.14.4 jar.

Change the path of the auto-build inside the file `build.xml` in the `RandomDrop` directory to the plugins directory of your Minecraft server.

Import the project inside Eclipse then you're good to go!

## Configuration
The configuration file is located in the `RandomDrop` folder inside the `plugins` folder of your Minecraft server, the file you are supposed to edit is `config.yml`.

### RANDOMIZE_DURABILITY
    (default: false)
is to randomize the durability of a randomized item IF the item is damageable.

### RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS
    (default: false)
is to randomize the durability of a crafted item IF the item is damageable.

### CLAIMED_LORE_TEXT
    (default: §r§7§lCLAIMED)
is to change the randomized item lore.

### SEED
    (default: random_int)
is to make the randomized items different for each seed, ex: seed 1234, if sand is broken, will loot iron - seed 1233, if sand is broken, will loot gold. To regenerate a random seed, delete the line. The seed is generated using the `System.currentTimeMillis();` function.

### KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM
    (default: false)
will, as its name imply, once the unclaimed item is drop, keep the enchant of the unclaimed item to the randomized item.

### KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE
    (default: false)
will, as its name imply, once the unclaimed item is drop, keep the custom name of the unclaimed item to the randomized item.

### CLAIM_CRAFTED_ITEMS
    (default: true)
will, as its name imply, claim the item that is gonna be crafted.

### RANDOMIZE_CRAFT
    (default: false)
will, randomize the result item from a crafting table. However, it will not randomize the recipe.