package me.spighetto.events.playerevents;

import me.spighetto.events.fertilizerevent.Fertilizer;
import me.spighetto.mypoop.Constants;
import me.spighetto.mypoop.MyPoop;
import me.spighetto.mypoop.core.port.PlayerMessagingPort;
import me.spighetto.mypoopversionsinterfaces.IPoop;
import me.spighetto.mypoop.version.VersionCapabilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerEvents implements Listener {
    private final MyPoop plugin;
    private final PlayerMessagingPort messagingPort;
    private final VersionCapabilities versionCapabilities;

    public PlayerEvents(MyPoop plugin, PlayerMessagingPort messagingPort, VersionCapabilities versionCapabilities){
        this.plugin = plugin;
        this.messagingPort = messagingPort;
        this.versionCapabilities = versionCapabilities;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (plugin.playersLevelFood.containsKey(player.getUniqueId())) {
            if (isPlayerFoodTrigger(player)) {
                if (player.isSneaking()) {
                    IPoop poop = createPoopForVersion(player);
                    if (poop == null) {
                        return;
                    }

                    // Trigger fertilization effect (executed in constructor)
                    new Fertilizer(plugin, player);

                    if (plugin.getPoopConfig().getNamedPoop()) {
                        poop.setName(player.getName(), plugin.getPoopConfig().getColorPoopName());
                    } else {
                        poop.setName(player.getName());
                    }

                    plugin.newPoop(poop);
                    plugin.playersLevelFood.remove(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {

            if (!plugin.playersLevelFood.containsKey(player.getUniqueId()))
                plugin.playersLevelFood.put(player.getUniqueId(), 0);

            if (!isPlayerFoodLimit(player)) {

                if (event.getFoodLevel() - player.getFoodLevel() > 0) {

                    plugin.playersLevelFood.put(player.getUniqueId(), plugin.playersLevelFood.get(player.getUniqueId()) + event.getFoodLevel() - player.getFoodLevel());

                    if (isPlayerFoodTrigger(player)) {
                        printMessage(player, plugin.getPoopConfig().getMessage());
                    }
                }
            }
        }
    }

    @EventHandler
    public void checkCanEat(PlayerItemConsumeEvent event) {
        if (plugin.playersLevelFood.containsKey(event.getPlayer().getUniqueId())) {
            if (isPlayerFoodLimit(event.getPlayer())) {
                event.setCancelled(true);
                printMessage(event.getPlayer(), plugin.getPoopConfig().getMessageAtLimit());
            }
        }
    }

    @EventHandler
    public void checkCanEatCake(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock() != null
                && event.getClickedBlock().getType().equals(Material.CAKE)
                && isPlayerFoodLimit(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    public void printMessage(Player player, String msg) {
        int where = plugin.getPoopConfig().getWherePrint();
        String colored = versionCapabilities.colorize(msg);

        if (where == Constants.MESSAGE_LOCATION_TITLE) {
            if (versionCapabilities.supportsTitles() && versionCapabilities.sendTitle(player, msg)) {
                return;
            }
            messagingPort.sendTo(player.getUniqueId(), colored);
            return;
        }

        if (where == Constants.MESSAGE_LOCATION_SUBTITLE) {
            if (versionCapabilities.supportsTitles() && versionCapabilities.sendSubtitle(player, msg)) {
                return;
            }
            messagingPort.sendTo(player.getUniqueId(), colored);
            return;
        }

        if (where == Constants.MESSAGE_LOCATION_ACTIONBAR) {
            if (versionCapabilities.supportsActionBar() && versionCapabilities.sendActionBar(player, msg)) {
                return;
            }
            messagingPort.sendTo(player.getUniqueId(), colored);
            return;
        }

        messagingPort.sendTo(player.getUniqueId(), colored);
    }

    private boolean isPlayerFoodTrigger(Player player){
        return plugin.playersLevelFood.get(player.getUniqueId()) >= plugin.getPoopConfig().getTrigger();
    }

    private boolean isPlayerFoodLimit(Player player){
        return plugin.playersLevelFood.get(player.getUniqueId()) >= plugin.getPoopConfig().getLimit();
    }

    private IPoop createPoopForVersion(Player player) {
        IPoop poop = versionCapabilities.spawnPoop(player);
        if (poop == null) {
            plugin.getLogger().severe("Failed to spawn poop entity for player " + player.getName());
        }
        return poop;
    }
}
