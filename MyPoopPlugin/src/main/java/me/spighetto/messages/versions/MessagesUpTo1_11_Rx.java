package me.spighetto.messages.versions;

import me.spighetto.mypoopversionsinterfaces.IMessages;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MessagesUpTo1_11_Rx implements IMessages {
    private Player player;
    private String message;

    public MessagesUpTo1_11_Rx(Player p, String msg) {
        this.player = p;
        this.message = msg;
    }

    @Override
    public void printActionBar() {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }

    @Override
    public void sendTitle() {
        player.sendTitle(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message), "", 10, 30, 10);
    }

    @Override
    public void sendSubtitle() {
        player.sendTitle("", ChatColor.translateAlternateColorCodes('&', message), 10, 30, 10);
    }
}
