package me.spighetto.mypoop.factory;

import me.spighetto.mypoop.Constants;
import me.spighetto.mypoopversionsinterfaces.IPoop;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for creating version-specific Poop implementations.
 * Uses cached reflection for better performance than repeated Class.forName() calls.
 */
public class PoopFactory {

    private static final Map<Integer, Constructor<? extends IPoop>> CONSTRUCTOR_CACHE = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(PoopFactory.class.getName());

    /**
     * Creates a version-specific Poop instance for the given player.
     * Uses cached constructors for better performance.
     *
     * @param player the player dropping the poop
     * @param serverVersion the Minecraft server minor version (e.g., 8 for 1.8, 19 for 1.19)
     * @return a new IPoop instance appropriate for the server version, or null if creation fails
     */
    public static IPoop createPoop(Player player, int serverVersion) {
        try {
            Constructor<? extends IPoop> constructor = getOrCacheConstructor(serverVersion);
            return constructor.newInstance(player);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                "Failed to create poop for Minecraft 1." + serverVersion +
                " and player " + player.getName(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Constructor<? extends IPoop> getOrCacheConstructor(int serverVersion) throws Exception {
        // Check cache first
        if (CONSTRUCTOR_CACHE.containsKey(serverVersion)) {
            return CONSTRUCTOR_CACHE.get(serverVersion);
        }

        // Determine class name based on version
        final String className;
        if (serverVersion >= Constants.MIN_VERSION_NMS_1_8
                && serverVersion <= Constants.MAX_VERSION_NMS_1_11) {
            className = "me.spighetto.mypoopv1_8.Poop_v1_8";

        } else if (serverVersion >= Constants.MIN_VERSION_MODERN_API_1_12
                && serverVersion <= Constants.MAX_VERSION_MODERN_API_1_18) {
            className = "me.spighetto.mypoopv1_13.Poop_v1_13";

        } else if (serverVersion == Constants.VERSION_1_19) {
            className = "me.spighetto.mypoopv1_19_4.MyPoop_v1_19_4";

        } else {
            throw new UnsupportedVersionException(
                "Minecraft version 1." + serverVersion + " is not supported. " +
                "Supported versions: 1.8-1.19"
            );
        }

        // Load class and get constructor
        Class<?> clazz = Class.forName(className);
        Constructor<? extends IPoop> constructor =
            (Constructor<? extends IPoop>) clazz.getConstructor(Player.class);

        // Cache for future use
        CONSTRUCTOR_CACHE.put(serverVersion, constructor);

        return constructor;
    }

    /**
     * Exception thrown when attempting to create a Poop for an unsupported Minecraft version.
     */
    public static class UnsupportedVersionException extends RuntimeException {
        public UnsupportedVersionException(String message) {
            super(message);
        }
    }
}
