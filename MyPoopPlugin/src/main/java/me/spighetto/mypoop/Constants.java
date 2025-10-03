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
    
    /** Minimum Minecraft minor version for NMS 1.8-1.11 (legacy) */
    public static final int MIN_VERSION_NMS_1_8 = 8;
    
    /** Maximum Minecraft minor version for NMS 1.8-1.11 (legacy) */
    public static final int MAX_VERSION_NMS_1_11 = 11;
    
    /** Minimum Minecraft minor version for modern Bukkit API (1.12-1.18) */
    public static final int MIN_VERSION_MODERN_API_1_12 = 12;
    
    /** Maximum Minecraft minor version for Bukkit API before 1.19 changes */
    public static final int MAX_VERSION_MODERN_API_1_18 = 18;
    
    /** Minecraft 1.19.x minor version */
    public static final int VERSION_1_19 = 19;
    
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
