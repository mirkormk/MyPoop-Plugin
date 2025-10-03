package me.spighetto.commands;

import me.spighetto.mypoop.MyPoop;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

/**
 * Command implementation for reloading the plugin configuration.
 * Performs a safe reload without restarting the plugin.
 */
public class ReloadCommand implements Command {

    private final MyPoop plugin;

    public ReloadCommand(MyPoop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try {
            safeReload();
            sender.sendMessage(ChatColor.GREEN + "MyPoop configuration reloaded successfully");
            plugin.getLogger().info("Configuration reloaded by " + sender.getName());
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to reload MyPoop: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Failed to reload plugin configuration", e);
        }
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "mypoop.reload";
    }

    @Override
    public String getUsage() {
        return "reload - Reloads the plugin configuration";
    }

    /**
     * Performs a safe reload without disabling/enabling the plugin.
     * Only reloads configuration and clears runtime caches.
     *
     * CRITICAL: Never use disablePlugin()/enablePlugin() as it causes:
     * - Memory leaks
     * - ClassLoader issues
     * - Corrupted plugin state
     * - Server crashes
     */
    private void safeReload() {
        // 1. Clear existing poops
        plugin.deletePoops();

        // 2. Clear player food levels cache
        plugin.playersLevelFood.clear();

        // 3. Reload configuration from disk
        plugin.reloadConfig();

        // 4. Re-parse config values into PoopConfig wrapper
        plugin.readConfigs();
    }
}

