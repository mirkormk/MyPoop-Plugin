package me.spighetto.mypoop.adapter.messaging;

import java.util.UUID;
import me.spighetto.mypoop.core.port.PlayerMessagingPort;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Bukkit adapter implementing PlayerMessagingPort.
 */
public final class BukkitPlayerMessagingAdapter implements PlayerMessagingPort {
    @Override
    public void sendTo(UUID playerId, String message) {
        if (playerId == null || message == null || message.isEmpty()) {
            return;
        }
        final Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            player.sendMessage(message);
        }
    }
}

