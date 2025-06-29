# RandomDrop
is a plugin to randomize the items you are normally supposed to get when an item entity drop.

This plugin now targets Paper 1.21.6.

This plugin has been inspired by a French Minecraft video series, but since the maker of the plugin wasn't willing to share it I just decided to make an identical one to share for the public.

# How to build?
Clone the repository using git and run `mvn package` inside the `RandomDrop` directory to build the plugin JAR. The resulting file can be dropped directly into your server's `plugins` folder.
If you are running in a Codex workspace you may need to configure Maven to use
the built-in proxy before running the build command:

```sh
mkdir -p ~/.m2
cat > ~/.m2/settings.xml <<EOF
<settings>
  <proxies>
    <proxy>
      <id>codexProxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy</host>
      <port>8080</port>
    </proxy>
    <proxy>
      <id>codexProxyHttps</id>
      <active>true</active>
      <protocol>https</protocol>
      <host>proxy</host>
      <port>8080</port>
    </proxy>
  </proxies>
</settings>
EOF
```
When the `main` branch is updated, GitHub Actions automatically builds the plugin and publishes a release. The tag is derived from the `version` field in `plugin.yml`.

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
will randomize the result item from a crafting table. However, it will not randomize the recipe.
