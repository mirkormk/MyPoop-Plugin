package me.spighetto.commands;

import me.spighetto.mypoop.MyPoop;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.validation.constraints.NotNull;
import java.util.logging.Level;

public class Reload implements CommandExecutor{

    private final MyPoop plugin;

    public Reload(MyPoop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String nome, @NotNull String[] args) {
        
        if(!cmd.getName().equalsIgnoreCase("mypoop")) {
            return false;
        }
        
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /mypoop reload");
            return true;
        }
        
        if(!args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.RED + "Unknown subcommand: " + args[0]);
            sender.sendMessage(ChatColor.YELLOW + "Usage: /mypoop reload");
            return true;
        }
        
        // Permission check (if configured in plugin.yml)
        if(!sender.hasPermission("mypoop.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to reload MyPoop");
            return true;
        }
        
        // Execute safe reload
        try {
            safeReload();
            sender.sendMessage(ChatColor.GREEN + "MyPoop configuration reloaded successfully");
            plugin.getLogger().info("Configuration reloaded by " + sender.getName());
        } catch(Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to reload MyPoop: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Failed to reload plugin configuration", e);
        }
        
        return true;
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
