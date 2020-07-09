package me.spighetto.mypoopv1_12_r0;

import me.spighetto.mypoopapi.Wrapper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class NMSHandler implements Wrapper {

    @Override
    public ItemStack getCocoaBeans() {
        ItemStack cocoa = new ItemStack(Material.INK_SACK);
        cocoa.setDurability((short) 3);
        return cocoa;
    }

    @Override
    public void printActionBar(Player p, String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
    }

    @Override
    public void sendTitle(Player player, String text) {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', text), "", 10, 30, 10);
    }

    @Override
    public void sendSubtitle(Player player, String text) {
        player.sendTitle("", ChatColor.translateAlternateColorCodes('&', text), 10, 30, 10);
    }
}
