package me.spighetto.mypoop.version;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Centralizza le differenze tra versioni Paper/Spigot per messaggistica base,
 * evitando NMS e riducendo l'uso di reflection. Nessuna dipendenza extra.
 */
public final class VersionCapabilities {
    private final int serverVersionMinor; // es: 19 per 1.19.x, 20 per 1.20.x, 21 per 1.21.x

    public VersionCapabilities(int serverVersionMinor) {
        this.serverVersionMinor = serverVersionMinor;
    }

    public boolean supportsTitles() {
        // Le API per i titles sono stabili da anni; consideriamo sicuro da 1.11+
        return serverVersionMinor >= 11;
    }

    public boolean supportsActionBar() {
        // Evitiamo per ora: senza Adventure o bungeecord-chat dip, restiamo su fallback.
        return false;
    }

    public void sendTitle(Player player, String title) {
        // Durate conservative
        player.sendTitle(colorize(title), "", 10, 40, 10);
    }

    public void sendSubtitle(Player player, String subtitle) {
        player.sendTitle("", colorize(subtitle), 10, 40, 10);
    }

    public String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
