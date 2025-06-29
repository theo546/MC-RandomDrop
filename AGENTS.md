If you are working inside a Codex environment you may need to configure the Maven proxy before building. Use the following commands:

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
