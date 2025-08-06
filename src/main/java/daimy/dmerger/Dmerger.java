package daimy.dmerger;

import org.bukkit.plugin.java.JavaPlugin;

public final class Dmerger extends JavaPlugin {

    private ItemSpawnListener itemSpawnListener;

    @Override
    public void onEnable() {
        getLogger().info("Dmerger has been enabled!");

        setupConfig();

        this.itemSpawnListener = new ItemSpawnListener(this);
        getServer().getPluginManager().registerEvents(this.itemSpawnListener, this);

        DmergerCommand dmergerCommand = new DmergerCommand(this);
        this.getCommand("dmerger").setExecutor(dmergerCommand);
        this.getCommand("dmerger").setTabCompleter(dmergerCommand);
    }

    @Override
    public void onDisable() {
        getLogger().info("Dmerger has been disabled.");
    }

    private void setupConfig() {
        saveDefaultConfig();

        reloadConfig();
    }

    public void reloadPluginConfig() {
        reloadConfig();

        if (itemSpawnListener != null) {
            itemSpawnListener.reloadConfigValues();
        }
    }
}
