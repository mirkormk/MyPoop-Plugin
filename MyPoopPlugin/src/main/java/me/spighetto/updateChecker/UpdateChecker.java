package me.spighetto.updateChecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    private final Plugin plugin;
    private final int resourceId;
    private final String pluginName;

    public UpdateChecker(Plugin plugin, int resourceId, String pluginName) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.pluginName = pluginName;

        logCheckUpdates();
    }

    public void logCheckUpdates() {
        // --- Check Update --- //

        ConsoleCommandSender console = Bukkit.getConsoleSender();

        this.getVersion(version -> {
            String message = "";
            String whiteSpaces = "";

            console.sendMessage(ChatColor.DARK_AQUA + "-------------------- " + ChatColor.AQUA + pluginName + ChatColor.DARK_AQUA + " --------------------");

            if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                message = ChatColor.DARK_AQUA + "Latest version already installed";
                whiteSpaces = printWhiteSpaces(pluginName.length() + 42, message.length());
                console.sendMessage(whiteSpaces + message + whiteSpaces);
            } else {
                message = ChatColor.DARK_AQUA + "There is a new update available.";
                whiteSpaces = printWhiteSpaces(pluginName.length() + 42, message.length());
                console.sendMessage(whiteSpaces + message + whiteSpaces);

                message = ChatColor.DARK_AQUA + "Your version: " + ChatColor.AQUA + plugin.getDescription().getVersion().toString() + ChatColor.DARK_AQUA + "  Latest version: " + ChatColor.AQUA + version.toString();
                whiteSpaces = printWhiteSpaces(pluginName.length() + 42, message.length());
                console.sendMessage(whiteSpaces + message + whiteSpaces);

                message = ChatColor.DARK_AQUA + "You can download the latest version from here:";
                whiteSpaces = printWhiteSpaces(pluginName.length() + 42, message.length());
                console.sendMessage(whiteSpaces + message + whiteSpaces);

                message = ChatColor.AQUA + "https://www.spigotmc.org/resources/" + resourceId;
                whiteSpaces = printWhiteSpaces(pluginName.length() + 42, message.length());
                console.sendMessage(whiteSpaces + message + whiteSpaces);
            }

            console.sendMessage("   ");

            message = ChatColor.DARK_AQUA + "by Spighetto";
            whiteSpaces = printWhiteSpaces(pluginName.length() + 42, message.length());
            console.sendMessage(whiteSpaces + message + whiteSpaces);

            console.sendMessage(ChatColor.DARK_AQUA + "-------------------- " + ChatColor.AQUA + pluginName + ChatColor.DARK_AQUA + " --------------------");
        });

        // --- Check Update --- //
    }

    protected void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

    private String printWhiteSpaces(int totLength, int substringLength) {
        StringBuilder space = new StringBuilder();

        for(int i = 0; i <= (totLength - substringLength) / 2; i++){
            space.append(" ");
        }

        return space.toString();
    }
}
