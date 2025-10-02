package me.spighetto.mypoop.adapter.config;

import java.util.Optional;
import me.spighetto.mypoop.core.port.ConfigPort;
import org.bukkit.configuration.file.FileConfiguration;

public final class BukkitConfigAdapter implements ConfigPort {
    private final FileConfiguration cfg;

    public BukkitConfigAdapter(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    @Override
    public Optional<String> getString(String path) {
        return Optional.ofNullable(cfg.getString(path));
    }

    @Override
    public Optional<Boolean> getBoolean(String path) {
        return Optional.ofNullable(cfg.get(path) instanceof Boolean ? cfg.getBoolean(path) : null);
    }

    @Override
    public Optional<Integer> getInt(String path) {
        return Optional.ofNullable(cfg.get(path) instanceof Number ? cfg.getInt(path) : null);
    }

    @Override
    public Optional<Long> getLong(String path) {
        return Optional.ofNullable(cfg.get(path) instanceof Number ? cfg.getLong(path) : null);
    }

    @Override
    public Optional<Double> getDouble(String path) {
        return Optional.ofNullable(cfg.get(path) instanceof Number ? cfg.getDouble(path) : null);
    }
}

