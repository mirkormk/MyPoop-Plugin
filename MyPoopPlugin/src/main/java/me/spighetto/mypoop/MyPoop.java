package me.spighetto.mypoop;

import me.spighetto.commands.Reload;
import me.spighetto.events.PlayerEvents;
import me.spighetto.mypoopapi.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class MyPoop extends JavaPlugin {

    public static MyPoop plugin;

    public static Wrapper wrapper;
    public static ArrayList<UUID> listPoops;
    public static String serverVersion;

    @Override
    public void onEnable() {
        plugin = this;

        getCommand("mypoop").setExecutor(new Reload());

        saveDefaultConfig();

        /*

        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        checkVersion(version);

        */

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        serverVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1];
        checkVersion(serverVersion);

        setVariables();

        if (getInstance().getConfig().getBoolean("updateChecker"))
            sendCheckUpdates();
    }

    @Override
    public void onDisable() {
        Reload.deletePoops();
    }

    public static MyPoop getInstance() {
        return plugin;
    }

    public void onReload() {
        plugin.reloadConfig();
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        plugin.getServer().getPluginManager().enablePlugin(plugin);
    }

    public void checkVersion(String version) {

        try {
            final Class<?> clazz = Class.forName("me.spighetto.mypoopv1_" + version + "_r0.NMSHandler");
            // Check if we have a NMSHandler class at that location.
            if (Wrapper.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                this.wrapper = (Wrapper) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Could not find support for this version.");
            this.setEnabled(false);
            return;
        }

    }

    public void setVariables() {

        String poopDisplayName = "";
        Boolean namedPoop = null;

        try {
            PlayerEvents.trigger = getInstance().getConfig().getInt("trigger");
            PlayerEvents.limit = getInstance().getConfig().getInt("limit");
            int delay = getInstance().getConfig().getInt("delay");
            PlayerEvents.delay = (long) delay * 20;
            PlayerEvents.message = getInstance().getConfig().getString("message");
            PlayerEvents.messageAtLimit = getInstance().getConfig().getString("messageAtLimit");
            PlayerEvents.wherePrint = getInstance().getConfig().getInt("wherePrint");
            poopDisplayName = getInstance().getConfig().getString("poopDisplayName");
            namedPoop = getInstance().getConfig().getBoolean("namedPoop");
            PlayerEvents.colorPoopName = getInstance().getConfig().getString("colorPoopName");
        } catch (Exception e) {
            System.out.println(e);
        }

        ItemStack item = wrapper.getCocoaBeans();

        ItemMeta meta = item.getItemMeta();
        PlayerEvents.metaCocoa = meta;
        PlayerEvents.itemCocoa = item;
        PlayerEvents.namedPoop = namedPoop;
        PlayerEvents.poopDisplayName = poopDisplayName;
        PlayerEvents.metaCocoa.setLore(Arrays.asList("Poop"));
        PlayerEvents.itemCocoa.setItemMeta(PlayerEvents.metaCocoa);

        listPoops = new ArrayList<UUID>();

    }

    public void sendCheckUpdates() {
        // --- Check Update --- //

        ConsoleCommandSender console = Bukkit.getConsoleSender();

        int pluginId = 77372;

        new UpdateChecker(this, pluginId).getVersion(version -> {
            console.sendMessage(ChatColor.DARK_AQUA + "-------------------- " + ChatColor.AQUA + "MyPoop" + ChatColor.DARK_AQUA + " --------------------");

            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.sendMessage(ChatColor.DARK_AQUA + "        There is not a new update available");
            } else {
                console.sendMessage(ChatColor.DARK_AQUA + "        There is a new update available.");
                console.sendMessage(ChatColor.DARK_AQUA + "   Your version: " + ChatColor.AQUA + this.getDescription().getVersion().toString() + ChatColor.DARK_AQUA + "  Latest version: " + ChatColor.AQUA + version.toString());
                console.sendMessage(ChatColor.DARK_AQUA + " You can download the latest version from here:");
                console.sendMessage(ChatColor.AQUA + "    https://www.spigotmc.org/resources/" + pluginId);
            }

            console.sendMessage("   ");
            console.sendMessage(ChatColor.DARK_AQUA + "                  by Spighetto");
            console.sendMessage(ChatColor.DARK_AQUA + "-------------------- " + ChatColor.AQUA + "MyPoop" + ChatColor.DARK_AQUA + " --------------------");
        });

        // --- Check Update --- //
    }
}
