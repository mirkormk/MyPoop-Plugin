package me.spighetto.mypoopv1_8;

import me.spighetto.mypoopversionsinterfaces.IPoop;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class Poop_v1_8 implements IPoop {
    private final Item poopItem;

    public Poop_v1_8(Player player) {
        poopItem = getCocoaBeansItem(player.getWorld(), player.getLocation());

        poopItem.setPickupDelay(Integer.MAX_VALUE);
        player.getWorld().playSound(player.getLocation(), Sound.SLIME_WALK, (float) 1.0, (float) 1.5);
    }

    public Item getCocoaBeansItem(World world, Location location) {
        ItemStack cocoaItemStack = new ItemStack(Material.INK_SACK);
        cocoaItemStack.setDurability((short) 3);
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
