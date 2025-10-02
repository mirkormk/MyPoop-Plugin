package me.spighetto.mypoop.core.port;

/**
 * Outbound port for logging from core/services.
 */
public interface LoggingPort {
    void info(String message);
    void warn(String message);
    void error(String message, Throwable t);
    default void error(String message) { error(message, null); }
}

