package me.spighetto.events.playerevents;

import me.spighetto.mypoop.MyPoop;
import me.spighetto.mypoopv1_11.Messages_v1_11;
import me.spighetto.mypoopv1_13.Poop_v1_13;
import me.spighetto.mypoopv1_8.Messages_v1_8;
import me.spighetto.mypoopv1_8.Poop_v1_8;
import me.spighetto.mypoopversionsinterfaces.IMessages;
import me.spighetto.mypoopversionsinterfaces.IPoop;
import org.bukkit.Bukkit;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    public static Map<UUID, Integer> playersLevelFood = new HashMap<>();

//    public static ItemStack itemCocoa;
//    public static ItemMeta metaCocoa;

    public static int trigger;
    public static int limit;
    public static long delay;
    public static Boolean namedPoop;
    public static String poopDisplayName;
    public static String colorPoopName;

    public static String message;
    public static String messageAtLimit;
    public static int wherePrint;

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (playersLevelFood.containsKey(player.getUniqueId())) {
            if (playersLevelFood.get(player.getUniqueId()) >= trigger) {
                if (player.isSneaking()) {

                    //Item i = player.getWorld().dropItem(player.getLocation(), itemCocoa);
                    IPoop poop;

                    if(MyPoop.serverVersion >= 8 && MyPoop.serverVersion <= 11) {
                        poop = new Poop_v1_8(player);
                    } else if (MyPoop.serverVersion >= 12 && MyPoop.serverVersion <= 19) {
                        poop = new Poop_v1_13(player);
                    } else {
                        return;
                    }
//                    poop = new PoopUpTo1_13_Rx(player);
//                    Poop_v1_8 = new PoopFrom1_8_Rx_To1_12_R1(player.getLocation().getBlock());

                    // Next to add
//                    if (MyPoop.getInstance().getConfig().getInt("growingSettings.radius") > -1 && MyPoop.getInstance().getConfig().getInt("growingSettings.radius") < 11) {
//                        FertilizerEvent fertilizer = new FertilizerEvent(player);
//                    }

                    if (namedPoop) {
                        //i.setCustomName(ChatColor.translateAlternateColorCodes('&', colorPoopName + player.getName().toString() + "'s Poop"));
                        poop.setName(player.getName(), colorPoopName);
                    } else {
                        //i.setCustomName(ChatColor.translateAlternateColorCodes('&', poopDisplayName));
                        poop.setName(player.getName());
                    }
                    //i.setCustomNameVisible(true);
                    //player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_HURT, (float) 1.0, (float) 1.5);
                    //i.setPickupDelay(Integer.MAX_VALUE);

                    MyPoop.listPoops.add(poop.getPoopItem().getUniqueId());

                    Bukkit.getScheduler().scheduleSyncDelayedTask(MyPoop.getInstance(), new Runnable() {

                        @Override
                        public void run() {
                            MyPoop.listPoops.remove(poop.getPoopItem().getUniqueId());
                            poop.delete();
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
        IMessages message;

        if(MyPoop.serverVersion >= 8 && MyPoop.serverVersion <= 11) {
             message = new Messages_v1_8(player, msg);
        } else if (MyPoop.serverVersion >= 11 && MyPoop.serverVersion <= 19) {
            message = new Messages_v1_11(player, msg);
        } else {
            return;
        }

        switch (wherePrint) {
            case 2:
                message.sendTitle();
                //MyPoop.wrapper.sendTitle(player, msg);
                break;
            case 3:
                message.sendSubtitle();
                //MyPoop.wrapper.sendSubtitle(player, msg);
                break;
            case 4:
                message.printActionBar();
                //MyPoop.wrapper.printActionBar(player, msg);
                break;
            case 1:
            default:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                break;
        }
    }
}
