package me.spighetto.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Central dispatcher for MyPoop plugin commands.
 * Implements the Command pattern for better separation of concerns.
 */
public class CommandDispatcher implements CommandExecutor {

    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Registers a command handler.
     *
     * @param command the command to register
     */
    public void registerCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd,
                            @NotNull String label, @NotNull String[] args) {

        if (!cmd.getName().equalsIgnoreCase("mypoop")) {
            return false;
        }

        // No arguments provided
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /mypoop <subcommand>");
            sender.sendMessage(ChatColor.YELLOW + "Available subcommands:");
            for (Command command : commands.values()) {
                sender.sendMessage(ChatColor.YELLOW + "  - " + command.getUsage());
            }
            return true;
        }

        // Find and execute the subcommand
        String subcommand = args[0].toLowerCase();
        Command command = commands.get(subcommand);

        if (command == null) {
            sender.sendMessage(ChatColor.RED + "Unknown subcommand: " + args[0]);
            sender.sendMessage(ChatColor.YELLOW + "Available subcommands:");
            for (Command cmd2 : commands.values()) {
                sender.sendMessage(ChatColor.YELLOW + "  - " + cmd2.getUsage());
            }
            return true;
        }

        // Check permission
        String permission = command.getPermission();
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command");
            return true;
        }

        // Execute the command (pass args without the subcommand name)
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);

        return command.execute(sender, subArgs);
    }
}

