package me.spighetto.mypoopapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Wrapper {
    ItemStack getCocoaBeans();
    void printActionBar(Player p, String msg);
    void sendTitle(Player player, String text);
    void sendSubtitle(Player player, String text);
}
