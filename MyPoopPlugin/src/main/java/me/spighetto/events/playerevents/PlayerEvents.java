package me.spighetto.events.playerevents;

import me.spighetto.events.fertilizerevent.Fertilizer;
import me.spighetto.mypoop.MyPoop;
import me.spighetto.mypoopversionsinterfaces.IMessages;
import me.spighetto.mypoopversionsinterfaces.IPoop;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.lang.reflect.Constructor;

public class PlayerEvents implements Listener {
    private final MyPoop plugin;

    public PlayerEvents(MyPoop plugin){
        this.plugin = plugin;
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

                    Fertilizer fertilizer = new Fertilizer(plugin, player);

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
        if (event.getEntity() instanceof Player) {

            Player player = (Player) event.getEntity();

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
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            if (event.getClickedBlock().getType().equals(Material.CAKE))
                if (isPlayerFoodLimit(event.getPlayer()))
                    event.setCancelled(true);

    }

    public void printMessage(Player player, String msg) {
        IMessages message = createMessagesForVersion(player, msg);
        if (message == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return;
        }

        switch (plugin.getPoopConfig().getWherePrint()) {
            case 2:
                message.sendTitle();
                break;
            case 3:
                message.sendSubtitle();
                break;
            case 4:
                message.printActionBar();
                break;
            default:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                break;
        }
    }

    private boolean isPlayerFoodTrigger(Player player){
        return plugin.playersLevelFood.get(player.getUniqueId()) >= plugin.getPoopConfig().getTrigger();
    }
    private boolean isPlayerFoodLimit(Player player){
        return plugin.playersLevelFood.get(player.getUniqueId()) >= plugin.getPoopConfig().getLimit();
    }

    private IPoop createPoopForVersion(Player player) {
        try {
            final String className;
            if (plugin.serverVersion >= 8 && plugin.serverVersion <= 11) {
                className = "me.spighetto.mypoopv1_8.Poop_v1_8";
            } else if (plugin.serverVersion >= 12 && plugin.serverVersion <= 18) {
                className = "me.spighetto.mypoopv1_13.Poop_v1_13";
            } else if (plugin.serverVersion == 19) {
                className = "me.spighetto.mypoopv1_19_4.MyPoop_v1_19_4";
            } else {
                return null;
            }
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor(Player.class);
            Object instance = ctor.newInstance(player);
            return (IPoop) instance;
        } catch (Throwable t) {
            return null;
        }
    }

    private IMessages createMessagesForVersion(Player player, String msg) {
        try {
            final String className;
            if (plugin.serverVersion >= 8 && plugin.serverVersion <= 11) {
                className = "me.spighetto.mypoopv1_8.Messages_v1_8";
            } else if (plugin.serverVersion >= 11 && plugin.serverVersion <= 19) {
                className = "me.spighetto.mypoopv1_11.Messages_v1_11";
            } else {
                return null;
            }
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor(Player.class, String.class);
            Object instance = ctor.newInstance(player, msg);
            return (IMessages) instance;
        } catch (Throwable t) {
            return null;
        }
    }
}
