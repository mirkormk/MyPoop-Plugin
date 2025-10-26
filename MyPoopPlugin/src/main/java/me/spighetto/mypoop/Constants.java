package me.spighetto.mypoop;

/**
 * Constants for MyPoop plugin.
 * Centralizes magic numbers and version-specific values.
 */
public final class Constants {
    
    // Prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // === Server Version Ranges ===
    
    /** Minimum supported Minecraft minor version (1.19.4). */
    public static final int MIN_SUPPORTED_MINOR_VERSION = 19;

    /** Maximum supported Minecraft minor version (1.21.x). */
    public static final int MAX_SUPPORTED_MINOR_VERSION = 21;
    
    // === Message Display Locations ===
    
    /** Display message in chat */
    public static final int MESSAGE_LOCATION_CHAT = 1;
    
    /** Display message as title */
    public static final int MESSAGE_LOCATION_TITLE = 2;
    
    /** Display message as subtitle */
    public static final int MESSAGE_LOCATION_SUBTITLE = 3;
    
    /** Display message in action bar */
    public static final int MESSAGE_LOCATION_ACTIONBAR = 4;
}
