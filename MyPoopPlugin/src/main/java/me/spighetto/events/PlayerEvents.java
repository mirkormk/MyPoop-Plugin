package me.spighetto.events;

import me.spighetto.mypoop.MyPoop;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    public static Map<UUID, Integer> playersLevelFood = new HashMap<>();
    public static int trigger;
    public static int limit;
    public static ItemStack itemCocoa;
    public static long delay;
    public static String message;
    public static String messageAtLimit;
    public static int wherePrint;
    public static ItemMeta metaCocoa;
    public static Boolean namedPoop;
    public static String poopDisplayName;
    public static String colorPoopName;

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (playersLevelFood.containsKey(player.getUniqueId())) {
            if (playersLevelFood.get(player.getUniqueId()) >= trigger) {

                if (player.isSneaking()) {

                    Item i = player.getWorld().dropItem(player.getLocation(), itemCocoa);

                    if (namedPoop) {
                        i.setCustomName(ChatColor.translateAlternateColorCodes('&', colorPoopName + player.getName().toString() + "'s Poop"));
                    } else {
                        i.setCustomName(ChatColor.translateAlternateColorCodes('&', poopDisplayName));
                    }
                    i.setCustomNameVisible(true);

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_HURT, (float) 1.0, (float) 1.5);

                    i.setPickupDelay(Integer.MAX_VALUE);
                    MyPoop.listPoops.add(i.getUniqueId());

                    Bukkit.getScheduler().scheduleSyncDelayedTask(MyPoop.getInstance(), new Runnable() {

                        @Override
                        public void run() {
                            MyPoop.listPoops.remove(i.getUniqueId());
                            i.remove();
                        }

                    }, delay);

                    playersLevelFood.remove(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!playersLevelFood.containsKey(player.getUniqueId()))
                playersLevelFood.put(player.getUniqueId(), 0);

            if (!(playersLevelFood.get(player.getUniqueId()) >= limit)) {
                if (event.getFoodLevel() - player.getFoodLevel() > 0) {

                    playersLevelFood.put(player.getUniqueId(), playersLevelFood.get(player.getUniqueId()) + event.getFoodLevel() - player.getFoodLevel());

                    if (playersLevelFood.get(player.getUniqueId()) >= trigger) {
                        printMessage(player, message);
                    }
                }
            }
        }
    }


    @EventHandler
    public void checkCanEat(PlayerItemConsumeEvent event) {
        if (playersLevelFood.containsKey(event.getPlayer().getUniqueId())) {
            if (playersLevelFood.get(event.getPlayer().getUniqueId()) >= limit) {
                event.setCancelled(true);
                printMessage(event.getPlayer(), messageAtLimit);
            }
        }
    }

    @EventHandler
    public void checkCanEatCake(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            if (event.getClickedBlock().getType().equals(Material.CAKE))
                if (playersLevelFood.get(event.getPlayer().getUniqueId()) >= limit)
                    event.setCancelled(true);

    }

    public void printMessage(Player player, String msg) {
        switch (wherePrint) {
            case 1:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                break;
            case 2:
                MyPoop.wrapper.sendTitle(player, msg);
                break;
            case 3:
                MyPoop.wrapper.sendSubtitle(player, msg);
                break;
            case 4:
                //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
                MyPoop.wrapper.printActionBar(player, msg);
                break;
            default:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                break;
        }
    }
}
