package me.spighetto.commands;

import org.bukkit.command.CommandSender;

/**
 * Interface for plugin commands following the Command pattern.
 * Each command implementation handles a specific subcommand.
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param sender the command sender
     * @param args command arguments (without the subcommand name)
     * @return true if the command was handled, false otherwise
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * Gets the subcommand name that this command handles.
     *
     * @return the subcommand name (e.g., "reload")
     */
    String getName();

    /**
     * Gets the permission required to execute this command.
     *
     * @return the permission node, or null if no permission is required
     */
    String getPermission();

    /**
     * Gets the usage message for this command.
     *
     * @return the usage message
     */
    String getUsage();
}

