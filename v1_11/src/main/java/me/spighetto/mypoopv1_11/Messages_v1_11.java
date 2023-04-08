package me.spighetto.mypoopv1_11;

import me.spighetto.mypoopversionsinterfaces.IMessages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages_v1_11 implements IMessages {
    private final Player player;
    private final String message;

    public Messages_v1_11(Player p, String msg) {
        this.player = p;
        this.message = msg;
    }

    @Override
    public void printActionBar() {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }

    @Override
    public void sendTitle() {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', message), "", 10, 30, 10);
    }

    @Override
    public void sendSubtitle() {
        player.sendTitle(" ", ChatColor.translateAlternateColorCodes('&', message), 10, 30, 10);
    }
}
