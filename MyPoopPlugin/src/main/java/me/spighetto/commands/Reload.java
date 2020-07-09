package me.spighetto.commands;

import java.util.ArrayList;
import java.util.UUID;

import me.spighetto.mypoop.MyPoop;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import net.md_5.bungee.api.ChatColor;

public class Reload implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String nome, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("mypoop")) {
			if(args.length == 0) {
				
			} else if(args[0].equalsIgnoreCase("reload")) {
				
				deletePoops();
				
				try {
					MyPoop.getInstance().onReload();
				} catch(Exception e) {
					System.out.println(e);
				}
				sender.sendMessage(ChatColor.GREEN + "MyPoop: Reload complete");				
			} else {
				sender.sendMessage(ChatColor.RED + "MyPoop: Unknown command");
			}
		}
		return false;
	}
	
	public static void deletePoops() {
		ArrayList<Entity> en = new ArrayList<Entity>();
		
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
		
		en.forEach((e) -> {e.remove();});
	}

}
