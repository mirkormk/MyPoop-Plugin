package me.spighetto.commands;

import me.spighetto.mypoop.MyPoop;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.UUID;

public class Reload implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String nome, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("mypoop")) {
			if(args.length == 0) {
				return false;
			} else if(args[0].equalsIgnoreCase("reload")) {
				
				deletePoops();
				
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
	
	public static void deletePoops() {
		ArrayList<Entity> en = new ArrayList<>();
		
		for(World world : MyPoop.getInstance().getServer().getWorlds()) {
			for(Entity entityInWorld : world.getEntities()) {
				for(UUID ii : MyPoop.listPoops) {
					if(ii.equals(entityInWorld.getUniqueId())) {
						en.add(entityInWorld);
					}
				}
			}
		}
		
		MyPoop.listPoops.clear();
		
		en.forEach(Entity::remove);		// Method reference technique
	}

	public void onReload() {
		MyPoop.getInstance().reloadConfig();
		MyPoop.getInstance().getServer().getPluginManager().disablePlugin(MyPoop.getInstance());
		MyPoop.getInstance().getServer().getPluginManager().enablePlugin(MyPoop.getInstance());
	}
}
