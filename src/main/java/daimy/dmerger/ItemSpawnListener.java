package daimy.dmerger;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemSpawnListener implements Listener {

    private final Dmerger plugin;
    private final NamespacedKey totalAmountKey;

    private int mergeDelay;
    private double mergeRadius;
    private int maxMergeItems;
    private boolean debug;

    public ItemSpawnListener(Dmerger plugin) {
        this.plugin = plugin;
        this.totalAmountKey = new NamespacedKey(plugin, "dmerger_total_amount");
        this.reloadConfigValues();
    }

    public void reloadConfigValues() {
        plugin.reloadConfig();
        this.mergeDelay = plugin.getConfig().getInt("merge-delay");
        this.mergeRadius = plugin.getConfig().getDouble("merge-radius");
        this.maxMergeItems = plugin.getConfig().getInt("max-merge-items");
        this.debug = plugin.getConfig().getBoolean("debug");
        if (debug) {
            plugin.getLogger().info("Dmerger configuration values have been loaded/reloaded.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        Item pickedUpItem = event.getItem();
        PersistentDataContainer container = pickedUpItem.getPersistentDataContainer();

        if (!container.has(totalAmountKey, PersistentDataType.INTEGER)) {
            return;
        }

        event.setCancelled(true);

        int totalAmount = container.getOrDefault(totalAmountKey, PersistentDataType.INTEGER, 0);
        if (totalAmount <= 0) {
            pickedUpItem.remove();
            return;
        }

        Player player = event.getPlayer();
        ItemStack baseItemStack = pickedUpItem.getItemStack().clone();
        baseItemStack.setAmount(1);

        int maxStackSize = baseItemStack.getMaxStackSize();

        while (totalAmount >= maxStackSize) {
            ItemStack fullStack = baseItemStack.clone();
            fullStack.setAmount(maxStackSize);
            giveOrDropItem(player, fullStack);
            totalAmount -= maxStackSize;
        }

        if (totalAmount > 0) {
            ItemStack remainingStack = baseItemStack.clone();
            remainingStack.setAmount(totalAmount);
            giveOrDropItem(player, remainingStack);
        }

        pickedUpItem.remove();
    }

    private void giveOrDropItem(Player player, ItemStack item) {
        Map<Integer, ItemStack> leftover = player.getInventory().addItem(item);
        if (!leftover.isEmpty()) {
            for (ItemStack drop : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        final Item spawnedItem = event.getEntity();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!spawnedItem.isValid()) {
                    return;
                }

                List<Item> similarItems = spawnedItem.getNearbyEntities(mergeRadius, mergeRadius, mergeRadius).stream()
                        .filter(entity -> entity instanceof Item && entity.isValid() && !entity.getUniqueId().equals(spawnedItem.getUniqueId()))
                        .map(entity -> (Item) entity)
                        .filter(item -> item.getItemStack().isSimilar(spawnedItem.getItemStack()))
                        .limit(maxMergeItems)
                        .collect(Collectors.toList());

                if (similarItems.isEmpty()) {
                    return;
                }

                int totalAmount = spawnedItem.getPersistentDataContainer().getOrDefault(totalAmountKey, PersistentDataType.INTEGER, spawnedItem.getItemStack().getAmount());

                for (Item sourceItem : similarItems) {
                    totalAmount += sourceItem.getPersistentDataContainer().getOrDefault(totalAmountKey, PersistentDataType.INTEGER, sourceItem.getItemStack().getAmount());
                    sourceItem.remove();
                }

                if (debug) {
                    plugin.getLogger().info("Merging " + similarItems.size() + " stacks into one. Total: " + totalAmount + " of " + spawnedItem.getItemStack().getType().name());
                }

                spawnedItem.getItemStack().setAmount(1);
                spawnedItem.setCustomName(ChatColor.AQUA + spawnedItem.getItemStack().getType().name().replace("_", " ") + ChatColor.GRAY + " x" + ChatColor.WHITE + totalAmount);
                spawnedItem.setCustomNameVisible(true);

                spawnedItem.getPersistentDataContainer().set(totalAmountKey, PersistentDataType.INTEGER, totalAmount);
            }
        }.runTaskLater(plugin, mergeDelay);
    }
}
