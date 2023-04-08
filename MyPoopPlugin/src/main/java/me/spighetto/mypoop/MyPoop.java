package me.spighetto.mypoop;

import me.spighetto.commands.Reload;
import me.spighetto.events.playerevents.PlayerEvents;
import me.spighetto.updateChecker.UpdateChecker;
import me.spighetto.stats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class MyPoop extends JavaPlugin {

    public static MyPoop plugin;
    public static ArrayList<UUID> listPoops;
    public static int serverVersion;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getVersion();

        if(!isCompatibleVersion()){
            Bukkit.getConsoleSender().sendMessage("MyPoop: Error: incompatible server version");
        }

        Metrics metrics = new Metrics(this, 8159);

        getCommand("mypoop").setExecutor(new Reload());

        saveDefaultConfig();

        /*
        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        checkVersion(version);
        */

        setVariables();

        if (getInstance().getConfig().getBoolean("updateChecker")) {
            new UpdateChecker(this, 77372, "MyPoop");
        }
    }

    @Override
    public void onDisable() {
        Reload.deletePoops();
    }

    public static MyPoop getInstance() {
        return plugin;
    }

    private void getVersion() {
        try {
            serverVersion = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
        } catch (Exception e){
            Bukkit.getConsoleSender().sendMessage("MyPoop: Error: the server version could not be verified or incorrect server version");
            serverVersion = -1;
        }

    }

    private boolean isCompatibleVersion() {
        return serverVersion >= 8 && serverVersion <= 19;
    }

    private void setVariables() {

        String poopDisplayName = "";
        Boolean namedPoop = null;

        try {
            int delay = getInstance().getConfig().getInt("poopSettings.delay");
            PlayerEvents.delay = (long) delay * 20;

            PlayerEvents.trigger = getInstance().getConfig().getInt("poopSettings.trigger");
            PlayerEvents.limit = getInstance().getConfig().getInt("poopSettings.limit");
            if(PlayerEvents.limit < PlayerEvents.trigger){
                PlayerEvents.limit = PlayerEvents.trigger;
            }
            PlayerEvents.colorPoopName = getInstance().getConfig().getString("poopSettings.colorPoopName");
            PlayerEvents.message = getInstance().getConfig().getString("alerts.message");
            PlayerEvents.messageAtLimit = getInstance().getConfig().getString("alerts.messageAtLimit");
            PlayerEvents.wherePrint = getInstance().getConfig().getInt("alerts.wherePrint");

            poopDisplayName = getInstance().getConfig().getString("poopSettings.poopDisplayName");
            namedPoop = getInstance().getConfig().getBoolean("poopSettings.namedPoop");

            // Next to add
//            FertilizerEvent.allCropsNearby = getInstance().getConfig().getBoolean("growingSettings.allCropsNearby");
//            FertilizerEvent.randomGrow = getInstance().getConfig().getBoolean("growingSettings.randomGrow");
//            FertilizerEvent.radius = getInstance().getConfig().getDouble("growingSettings.radius");

        } catch (Exception e) {
            System.out.println(e);
        }

//        ItemStack item = wrapper.getCocoaBeans();
//
//        ItemMeta meta = item.getItemMeta();
//        PlayerEvents.metaCocoa = meta;
//        PlayerEvents.itemCocoa = item;
        PlayerEvents.namedPoop = namedPoop;
        PlayerEvents.poopDisplayName = poopDisplayName;
//        PlayerEvents.metaCocoa.setLore(Arrays.asList("Poop"));
//        PlayerEvents.itemCocoa.setItemMeta(PlayerEvents.metaCocoa);

        listPoops = new ArrayList<UUID>();

    }

}
