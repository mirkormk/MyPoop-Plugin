package me.spighetto.mypoop.poopmodel.versions;

import me.spighetto.mypoopversionsinterfaces.IPoop;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PoopUpTo1_13_Rx implements IPoop {
    private Item poopItem;

    public PoopUpTo1_13_Rx(Player player) {
        poopItem = getCocoaBeansItem(player.getWorld(), player.getLocation());

        poopItem.setPickupDelay(Integer.MAX_VALUE);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_HURT, (float) 1.0, (float) 1.5);
    }

    private Item getCocoaBeansItem(World world, Location location) {
        ItemStack cocoaItemStack = new ItemStack(Material.COCOA_BEANS);
        cocoaItemStack.getItemMeta().setLore(Arrays.asList("Poop"));

        return world.dropItem(location, cocoaItemStack);
    }

    @Override
    public void setName(String name) {
        poopItem.setCustomNameVisible(true);
        poopItem.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
    }
    @Override
    public void setName(String name, String colorCode) {
        poopItem.setCustomNameVisible(true);
        poopItem.setCustomName(ChatColor.translateAlternateColorCodes('&', colorCode + name + "'s Poop"));
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
