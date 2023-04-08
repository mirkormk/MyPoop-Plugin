package me.spighetto.messages.versions;

import me.spighetto.mypoopversionsinterfaces.IMessages;
import me.spighetto.mypoopversionsinterfaces.Title;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MessagesFrom1_8_Rx_To1_10_R1 implements IMessages {
    private Player player;
    private String message;

    public MessagesFrom1_8_Rx_To1_10_R1(Player p, String msg) {
        this.player = p;
        this.message = msg;
    }

    @Override
    public void printActionBar() {
        IChatBaseComponent actionBar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(actionBar, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
    }

    @Override
    public void sendTitle()
    {
        Title title = new Title(message, "", 10, 30, 10);
        title.send(player);
    }

    @Override
    public void sendSubtitle()
    {
        Title subTitle = new Title("", message, 10, 30, 10);
        subTitle.send(player);
    }
}
