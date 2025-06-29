<p align="center">
  <img src="logo.svg" width="120" alt="RandomDrop logo" />
</p>

# RandomDrop

RandomDrop is a Minecraft plugin that replaces every drop with a deterministic random item. The project targets **Paper 1.21.6** and is intended to be dropped straight into your server's `plugins` folder.

## Building from source

```bash
mvn -f RandomDrop/pom.xml package
```

The compiled JAR will appear in `RandomDrop/target/`. If you are working inside a Codex environment you may need to configure the Maven proxy as documented below.

### Using the Codex Maven proxy

```bash
mkdir -p ~/.m2
cat > ~/.m2/settings.xml <<'SETTINGS'
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
SETTINGS
```

## Continuous delivery

Whenever `main` is updated, GitHub Actions builds the plugin and publishes a release. If a release with the same tag already exists, it is removed before uploading the new artifact.

## Configuration options

The configuration file is generated inside `plugins/RandomDrop/config.yml`. Notable options include:

- **RANDOMIZE_DURABILITY** – randomize durability of dropped items (default: `false`)
- **RANDOMIZE_DURABILITY_OF_CRAFTED_ITEMS** – randomize durability of crafted items (default: `false`)
- **CLAIMED_LORE_TEXT** – lore line used to mark claimed items (default: `§r§7§lCLAIMED`)
- **SEED** – controls the drop mapping; leave blank to regenerate (default: random value)
- **KEEP_ENCHANT_ON_DROPPED_UNCLAIMED_ITEM** – keep enchantments when an item is randomized (default: `false`)
- **KEEP_ITEM_CUSTOMNAME_ON_RANDOMIZE** – keep custom names when an item is randomized (default: `false`)
- **CLAIM_CRAFTED_ITEMS** – mark crafted items as claimed (default: `true`)
- **RANDOMIZE_CRAFT** – randomize crafting outputs (default: `true`)