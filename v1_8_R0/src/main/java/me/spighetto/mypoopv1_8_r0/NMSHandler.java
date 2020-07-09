package me.spighetto.mypoopv1_8_r0;

import me.spighetto.mypoopapi.Title;
import me.spighetto.mypoopapi.Wrapper;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
        IChatBaseComponent actionBar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', text) + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(actionBar, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }

    @Override
    public void sendTitle(Player player, String text)
    {
        Title title = new Title(text, "", 10, 30, 10);
        title.send(player);
    }

    @Override
    public void sendSubtitle(Player player, String text)
    {
        Title subTitle = new Title("", text, 10, 30, 10);
        subTitle.send(player);
    }
}
