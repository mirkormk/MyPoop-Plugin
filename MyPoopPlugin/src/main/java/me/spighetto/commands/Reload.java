package me.spighetto.commands;

import me.spighetto.mypoop.MyPoop;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.validation.constraints.NotNull;

public class Reload implements CommandExecutor{

    private final MyPoop plugin;

    public Reload(MyPoop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String nome, @NotNull String[] args) {
        
        if(cmd.getName().equalsIgnoreCase("mypoop")) {
            if(args.length == 0) {
                return false;
            } else if(args[0].equalsIgnoreCase("reload")) {
                
                plugin.deletePoops();
                
                try {
                    onReload();
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
                sender.sendMessage(ChatColor.GREEN + "MyPoop: Reload complete");                
            } else {
                sender.sendMessage(ChatColor.RED + "MyPoop: Unknown command");
            }
        }

        return false;
    }

    public void onReload() {
        plugin.reloadConfig();
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        plugin.getServer().getPluginManager().enablePlugin(plugin);
    }

}
