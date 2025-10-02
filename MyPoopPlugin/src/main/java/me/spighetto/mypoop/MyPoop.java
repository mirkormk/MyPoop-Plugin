package me.spighetto.mypoop;

import me.spighetto.commands.Reload;
import me.spighetto.events.playerevents.PlayerEvents;
import me.spighetto.mypoopversionsinterfaces.IPoop;
import me.spighetto.stats.Metrics;
import me.spighetto.updateChecker.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import me.spighetto.mypoop.adapter.messaging.BukkitPlayerMessagingAdapter;
import me.spighetto.mypoop.adapter.logging.BukkitLoggingAdapter;
import me.spighetto.mypoop.adapter.config.BukkitConfigAdapter;
import me.spighetto.mypoop.core.port.PlayerMessagingPort;
import me.spighetto.mypoop.core.port.LoggingPort;
import me.spighetto.mypoop.core.port.ConfigPort;
import me.spighetto.mypoop.version.VersionCapabilities;

public final class MyPoop extends JavaPlugin {
    public Map<UUID, Integer> playersLevelFood = new HashMap<>();
    private PoopConfig config;
    public ArrayList<UUID> listPoops = new ArrayList<>();
    public int serverVersion;

    // Porte/adapters
    private PlayerMessagingPort messagingPort;
    private LoggingPort loggingPort;
    private ConfigPort configPort;

    // Version capabilities (infrastructure)
    private VersionCapabilities versionCapabilities;

    @Override
    public void onEnable() {
        serverVersion = parseVersion();

        // Wiring adapters
        this.messagingPort = new BukkitPlayerMessagingAdapter();
        this.loggingPort = new BukkitLoggingAdapter(getLogger());
        this.configPort = new BukkitConfigAdapter(getConfig());
        this.versionCapabilities = new VersionCapabilities(serverVersion);

        if(!isCompatibleVersion()){
            Bukkit.getConsoleSender().sendMessage("MyPoop: Error: incompatible server version");
        }

        saveDefaultConfig();
        readConfigs();
        super.getServer().getPluginManager().registerEvents(new PlayerEvents(this, messagingPort, versionCapabilities), this);
        new Metrics(this, 8159);

        Objects.requireNonNull(getCommand("mypoop")).setExecutor(new Reload(this));

        if (this.getConfig().getBoolean("updateChecker")) {
            new UpdateChecker(this, 77372, "MyPoop");
        }
    }

    @Override
    public void onDisable() {
        deletePoops();
    }

    public void deletePoops() {
        if(listPoops.size() > 0) {
            ArrayList<Entity> en = new ArrayList<>();

            for(World world : this.getServer().getWorlds()) {
                for(Entity entityInWorld : world.getEntities()) {
                    for(UUID ii : this.listPoops) {
                        if(ii.equals(entityInWorld.getUniqueId())) {
                            en.add(entityInWorld);
                        }
                    }
                }
            }

            this.listPoops.clear();

            en.forEach(Entity::remove);		// Method reference technique
        }
    }

    private int parseVersion() {
        try {
            return Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
        } catch (Exception e){
            Log("Error: the server version could not be verified or incorrect server version");
            return -1;
        }
    }

    public void newPoop(IPoop poop) {
        listPoops.add(poop.getPoopItem().getUniqueId());
        Chunk poopChunk = poop.getPoopItem().getLocation().getChunk();

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            listPoops.remove(poop.getPoopItem().getUniqueId());

            boolean needToLoad = poopChunk.isLoaded();
            if(!needToLoad)
                poopChunk.load();

            poop.delete();

            if(!needToLoad)
                poopChunk.unload();

        }, this.getPoopConfig().getDelay());
    }

    private boolean isCompatibleVersion() {
        return serverVersion >= 8 && serverVersion <= 19;
    }

    private void readConfigs() {

        try {
            config = new PoopConfig(
                    this.getConfig().getInt("poopSettings.trigger"),
                    this.getConfig().getInt("poopSettings.limit"),
                    this.getConfig().getLong("poopSettings.delay") * 20,
                    this.getConfig().getBoolean("poopSettings.namedPoop"),
                    this.getConfig().getString("poopSettings.colorPoopName"),
                    this.getConfig().getString("poopSettings.poopDisplayName"),
                    this.getConfig().getString("alerts.message"),
                    this.getConfig().getString("alerts.messageAtLimit"),
                    this.getConfig().getInt("alerts.wherePrint"),
                    this.getConfig().getBoolean("growingSettings.allCropsNearby"),
                    this.getConfig().getDouble("growingSettings.radius"),
                    this.getConfig().getBoolean("growingSettings.randomGrow")
            );

            if(config.getLimit() < config.getTrigger()){
                config.setLimit(config.getTrigger());
            }

        } catch (Exception e) {
            Log("Error: some value inside the config.yml has not been configured correctly. " +
                    "\n\t\tTip: save a copy of your current config.yml, delete from MyPoop's folder and reload the plugin to regenerate it as default. " +
                    "Finally pay attention to recopy your saved values to the new config.yml");
        }
    }

    public PoopConfig getPoopConfig(){
        return config;
    }

    public void Log(String text) {
        Bukkit.getConsoleSender().sendMessage("MyPoop: " + text);
    }
}
