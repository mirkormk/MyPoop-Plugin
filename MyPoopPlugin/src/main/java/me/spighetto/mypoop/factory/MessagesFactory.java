package me.spighetto.mypoop.factory;

import me.spighetto.mypoop.Constants;
import me.spighetto.mypoopversionsinterfaces.IMessages;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for creating version-specific Messages implementations.
 * Uses cached reflection for better performance than repeated Class.forName() calls.
 */
public class MessagesFactory {

    private static final Map<Integer, Constructor<? extends IMessages>> CONSTRUCTOR_CACHE = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(MessagesFactory.class.getName());

    /**
     * Creates a version-specific Messages instance for the given player.
     * Uses cached constructors for better performance.
     *
     * @param player the player receiving the message
     * @param message the message text to send
     * @param serverVersion the Minecraft server minor version (e.g., 8 for 1.8, 19 for 1.19)
     * @return a new IMessages instance appropriate for the server version, or null if creation fails
     */
    public static IMessages createMessages(Player player, String message, int serverVersion) {
        try {
            Constructor<? extends IMessages> constructor = getOrCacheConstructor(serverVersion);
            return constructor.newInstance(player, message);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,
                "Failed to create messages for Minecraft 1." + serverVersion +
                " and player " + player.getName(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Constructor<? extends IMessages> getOrCacheConstructor(int serverVersion) throws Exception {
        // Check cache first
        if (CONSTRUCTOR_CACHE.containsKey(serverVersion)) {
            return CONSTRUCTOR_CACHE.get(serverVersion);
        }

        // Determine class name based on version
        final String className;
        if (serverVersion >= Constants.MIN_VERSION_NMS_1_8
                && serverVersion <= Constants.MAX_VERSION_NMS_1_11) {
            className = "me.spighetto.mypoopv1_8.Messages_v1_8";

        } else if (serverVersion >= Constants.MAX_VERSION_NMS_1_11
                && serverVersion <= Constants.VERSION_1_19) {
            className = "me.spighetto.mypoopv1_11.Messages_v1_11";

        } else {
            throw new UnsupportedVersionException(
                "Minecraft version 1." + serverVersion + " is not supported for legacy messaging. " +
                "Use VersionCapabilities for modern versions."
            );
        }

        // Load class and get constructor
        Class<?> clazz = Class.forName(className);
        Constructor<? extends IMessages> constructor =
            (Constructor<? extends IMessages>) clazz.getConstructor(Player.class, String.class);

        // Cache for future use
        CONSTRUCTOR_CACHE.put(serverVersion, constructor);

        return constructor;
    }

    /**
     * Exception thrown when attempting to create Messages for an unsupported Minecraft version.
     */
    public static class UnsupportedVersionException extends RuntimeException {
        public UnsupportedVersionException(String message) {
            super(message);
        }
    }
}
