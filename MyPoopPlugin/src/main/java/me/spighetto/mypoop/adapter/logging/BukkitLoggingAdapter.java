package me.spighetto.mypoop.adapter.logging;

import java.util.logging.Logger;
import me.spighetto.mypoop.core.port.LoggingPort;

public final class BukkitLoggingAdapter implements LoggingPort {
    private final Logger logger;

    public BukkitLoggingAdapter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message, Throwable t) {
        if (t != null) {
            logger.severe(message + " :: " + t.getMessage());
        } else {
            logger.severe(message);
        }
    }
}

