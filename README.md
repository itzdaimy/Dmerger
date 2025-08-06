<h1 align="center">🧲 Dmerger</h1>
<p align="center">
  <strong>Reduce lag by automatically merging nearby dropped items in Minecraft.</strong><br>
  Spigot plugin for efficient item management & optimization.
</p>

<p align="center">
  <a href="https://github.com/itzdaimy/DmergerSpigot/stargazers"><img src="https://img.shields.io/github/stars/itzdaimy/DmergerSpigot?style=social" alt="Stars"></a>
  <a href="https://github.com/itzdaimy/DmergerSpigot/network/members"><img src="https://img.shields.io/github/forks/itzdaimy/DmergerSpigot?style=social" alt="Forks"></a>
  <a href="https://github.com/itzdaimy/DmergerSpigot/issues"><img src="https://img.shields.io/github/issues/itzdaimy/DmergerSpigot" alt="Issues"></a>
  <a href="https://github.com/itzdaimy/DmergerSpigot/blob/main/LICENSE"><img src="https://img.shields.io/github/license/itzdaimy/DmergerSpigot" alt="License"></a>
  <img src="https://komarev.com/ghpvc/?username=itzdaimy&label=Profile+Views&color=0e75b6&style=flat" alt="View Counter">
</p>

---

## 🚀 Features

- 🔁 **Smart Item Merging**: Automatically merges nearby dropped items.
- ⏱️ **Customizable Delay**: Control the delay before items attempt to merge.
- 📏 **Configurable Radius**: Set how far items should check for nearby stacks.
- 🧱 **Merge Limits**: Define how many items can merge in a single operation.
- 🛠️ **Hot Reload**: Reload config without restarting your server.
- 🧪 **Debug Mode**: Toggle detailed logs for development and testing.

---

## 🛠️ Tech Stack

| Tool         | Description                          |
|--------------|--------------------------------------|
| Java         | Programming Language                 |
| Spigot API   | Minecraft Plugin API                 |
| Gradle       | Dependency Management (optional)     |
| Bukkit       | Base Minecraft Server Implementation |

---

## 📂 Configuration

```yaml
# Dmerger Configuration File

merge-delay: 40         # Time in ticks (20 ticks = 1 second)
merge-radius: 2.5       # Radius in blocks to look for similar items
max-merge-items: 512    # Maximum number of items to merge in one go
debug: false            # Enable for detailed console logging
