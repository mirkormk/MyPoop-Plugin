package me.spighetto.mypoopv1_19_4;

import me.spighetto.mypoopversionsinterfaces.IPoop;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class MyPoop_v1_19_4 implements IPoop {

    private final Item poopItem;

    public MyPoop_v1_19_4(Player player) {
        poopItem = getCocoaBeansItem(player.getWorld(), player.getLocation());

        poopItem.setPickupDelay(Integer.MAX_VALUE);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_HURT, (float) 1.0, (float) 1.5);
    }

    public Item getCocoaBeansItem(World world, Location location) {
        ItemStack cocoa = new ItemStack(Material.COCOA_BEANS);
        ItemMeta itemMeta = cocoa.getItemMeta();
        if(itemMeta != null) {
            itemMeta.setCustomModelData(1);
            cocoa.setItemMeta(itemMeta);
        }
        return world.dropItem(location, cocoa);
    }

    @Override
    public void setName(String name) {
        poopItem.setCustomNameVisible(true);
        poopItem.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
    }

    @Override
    public void setName(String name, String colorCode) {
        poopItem.setCustomNameVisible(true);
        poopItem.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
    }

    @Override
    public Item getPoopItem() {
        return poopItem;
    }

    @Override
    public void delete() {
        poopItem.remove();
    }
}

