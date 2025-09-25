package me.spighetto.mypoop.core.port;

import java.util.UUID;

/**
 * Outbound port for player messaging from the domain/core layer.
 *
 * No Bukkit/Paper types here: adapters in the plugin implement this port.
 */
public interface PlayerMessagingPort {
    /**
     * Sends a plain text message to the given player, if online.
     * Implementations should be null-safe and no-op if the player is offline.
     */
    void sendTo(UUID playerId, String message);
}

