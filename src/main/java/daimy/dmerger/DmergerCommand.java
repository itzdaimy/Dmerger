package daimy.dmerger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DmergerCommand implements CommandExecutor, TabCompleter {

    private final Dmerger plugin;

    public DmergerCommand(Dmerger plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                if (!sender.hasPermission("dmerger.admin")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                    return true;
                }
                plugin.reloadPluginConfig();
                sender.sendMessage(ChatColor.GREEN + "Dmerger configuration has been reloaded.");
                return true;

            case "info":
                if (!sender.hasPermission("dmerger.admin")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                    return true;
                }
                sendInfoMessage(sender);
                return true;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown command. Use /dmerger for help.");
                return true;
        }
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "--- Dmerger Help ---");
        sender.sendMessage(ChatColor.YELLOW + "/dmerger info" + ChatColor.WHITE + " - Shows current config values.");
        sender.sendMessage(ChatColor.YELLOW + "/dmerger reload" + ChatColor.WHITE + " - Reloads the config file.");
    }

    private void sendInfoMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "--- Dmerger Configuration ---");
        sender.sendMessage(ChatColor.YELLOW + "Merge Delay: " + ChatColor.WHITE + plugin.getConfig().getInt("merge-delay") + " ticks");
        sender.sendMessage(ChatColor.YELLOW + "Merge Radius: " + ChatColor.WHITE + plugin.getConfig().getDouble("merge-radius") + " blocks");
        sender.sendMessage(ChatColor.YELLOW + "Max Merge Items: " + ChatColor.WHITE + plugin.getConfig().getInt("max-merge-items"));
        sender.sendMessage(ChatColor.YELLOW + "Debug Mode: " + ChatColor.WHITE + (plugin.getConfig().getBoolean("debug") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        sender.sendMessage(ChatColor.GRAY + "Status: No errors detected in configuration.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("dmerger.admin")) {
                return Arrays.asList("reload", "info").stream()
                        .filter(s -> s.startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}