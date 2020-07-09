package me.spighetto.mypoopv1_11_r0;

import me.spighetto.mypoopapi.Wrapper;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
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
    public void printActionBar(Player p, String text) {
        IChatBaseComponent actionBar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + org.bukkit.ChatColor.translateAlternateColorCodes('&', text) + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(actionBar, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
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
