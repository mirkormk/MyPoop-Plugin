package me.spighetto.mypoop.core.port;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for configuration access.
 * Implementations provide values from plugin config or other sources.
 */
public interface ConfigPort {
    Optional<String> getString(String path);
    Optional<Boolean> getBoolean(String path);
    Optional<Integer> getInt(String path);
    Optional<Long> getLong(String path);
    Optional<Double> getDouble(String path);
    default String getString(String path, String def) { return getString(path).orElse(def); }
    default boolean getBoolean(String path, boolean def) { return getBoolean(path).orElse(def); }
    default int getInt(String path, int def) { return getInt(path).orElse(def); }
    default long getLong(String path, long def) { return getLong(path).orElse(def); }
    default double getDouble(String path, double def) { return getDouble(path).orElse(def); }
}

