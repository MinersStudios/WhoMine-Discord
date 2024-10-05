package com.minersstudios.whomine.utility;

import com.minersstudios.whomine.WhoMine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A custom logging utility designed to send formatted log messages with
 * different levels and colors to various targets. Supports logging to the
 * console, players, and command senders using AdventureAPI for text formatting.
 * <br>
 * Available levels, and their corresponding colors:
 * <ul>
 *     <li>{@link Level#SEVERE} - Red</li>
 *     <li>{@link Level#WARNING} - Yellow</li>
 *     <li>{@link Level#INFO} - Default</li>
 *     <li>{@link Level#FINE} - Green</li>
 * </ul>
 */
@SuppressWarnings("unused")
public final class MSLogger {
    private static final ANSIComponentSerializer ANSI_SERIALIZER = ANSIComponentSerializer.builder().build();
    private static final Logger LOGGER = Logger.getLogger(WhoMine.class.getSimpleName());

    private static final String ANSI_LIME =  "\u001B[92m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static final int SEVERE =  1000;
    private static final int WARNING = 900;
    private static final int FINE =    500;

    @Contract(" -> fail")
    private MSLogger() throws AssertionError {
        throw new AssertionError("This class cannot be instantiated!");
    }

    /**
     * Logs a message with the specified severity level. If the level is 
     * {@link Level#FINE}, the message will be logged with the {@link Level#INFO}
     * level and colored lime green.
     *
     * @param level   One of the message level identifiers
     * @param message The component message
     * @see #log(Level, String)
     */
    public static void log(
            final @NotNull Level level,
            final @NotNull Component message
    ) {
        log(level, serialize(message));
    }

    /**
     * Logs a message with the specified severity level. If the level is 
     * {@link Level#FINE}, the message will be logged with the {@link Level#INFO}
     * level and colored lime green.
     *
     * @param level   One of the message level identifiers
     * @param message The string message
     * @see Logger#log(Level, String)
     */
    public static void log(
            final @NotNull Level level,
            final @NotNull String message
    ) {
        if (level.intValue() == FINE) {
            LOGGER.log(Level.INFO, ANSI_LIME + message + ANSI_RESET);
        } else {
            LOGGER.log(level, message);
        }
    }

    /**
     * Log a message with the specified severity level and associated array of 
     * parameters. If the level is {@link Level#FINE}, the message will be 
     * logged with the {@link Level#INFO} level and colored lime green.
     *
     * @param level   One of the message level identifiers
     * @param message The component message
     * @param params  Array of parameters to the message
     * @see #log(Level, String, Object...)
     */
    public static void log(
            final @NotNull Level level,
            final @NotNull Component message,
            final Object @NotNull ... params
    ) {
        log(level, serialize(message), params);
    }

    /**
     * Log a message with the specified severity level and associated array of 
     * parameters. If the level is {@link Level#FINE}, the message will be 
     * logged with the {@link Level#INFO} level and colored lime green.
     *
     * @param level   One of the message level identifiers
     * @param message The string message
     * @param params  Array of parameters to the message
     * @see Logger#log(Level, String, Object...)
     */
    public static void log(
            final @NotNull Level level,
            final @NotNull String message,
            final @Nullable Object @NotNull ... params
    ) {
        if (level.intValue() == FINE) {
            LOGGER.log(Level.INFO, ANSI_LIME + message + ANSI_RESET, params);
        } else {
            LOGGER.log(level, message, params);
        }
    }

    /**
     * Logs a message with the specified severity level and associated throwable.
     * If the level is {@link Level#FINE}, the message will be logged with the
     * {@link Level#INFO} level and colored lime green.
     *
     * @param level     One of the message level identifiers
     * @param message   The component message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, String, Throwable)
     */
    public static void log(
            final @NotNull Level level,
            final @NotNull Component message,
            final @NotNull Throwable throwable
    ) {
        log(level, serialize(message), throwable);
    }

    /**
     * Logs a message with the specified severity level and associated throwable.
     * If the level is {@link Level#FINE}, the message will be logged with the 
     * {@link Level#INFO} level and colored lime green.
     *
     * @param level     One of the message level identifiers
     * @param message   The string message
     * @param throwable Throwable associated with a log message
     * @see Logger#log(Level, String, Throwable)
     */
    public static void log(
            final @NotNull Level level,
            final @NotNull String message,
            final @NotNull Throwable throwable
    ) {
        if (level.intValue() == FINE) {
            LOGGER.log(Level.INFO, ANSI_LIME + message + ANSI_RESET, throwable);
        } else {
            LOGGER.log(level, message, throwable);
        }
    }

    /**
     * Logs a message with the {@link Level#SEVERE} severity level
     *
     * @param message The string message
     * @see #log(Level, String)
     */
    public static void severe(final @NotNull String message) {
        log(Level.SEVERE, message);
    }

    /**
     * Logs a message with the {@link Level#SEVERE} severity level
     *
     * @param message The component message
     * @see #log(Level, Component)
     */
    public static void severe(final @NotNull Component message) {
        log(Level.SEVERE, message);
    }

    /**
     * Logs a message with the {@link Level#SEVERE} severity level and 
     * associated array of parameters
     *
     * @param message The string message
     * @param params  Array of parameters to the message
     * @see #log(Level, String, Object...)
     */
    public static void severe(
            final @NotNull String message,
            final Object @NotNull ... params
    ) {
        log(Level.SEVERE, message, params);
    }

    /**
     * Logs a message with the {@link Level#SEVERE} severity level and 
     * associated array of parameters
     *
     * @param message The component message
     * @param params  Array of parameters to the message
     * @see #log(Level, Component, Object...)
     */
    public static void severe(
            final @NotNull Component message,
            final Object @NotNull ... params
    ) {
        log(Level.SEVERE, message, params);
    }

    /**
     * Logs a message with the {@link Level#SEVERE} severity level and
     * associated throwable
     *
     * @param message   The string message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, String, Throwable)
     */
    public static void severe(
            final @NotNull String message,
            final @NotNull Throwable throwable
    ) {
        log(Level.SEVERE, message, throwable);
    }

    /**
     * Logs a message with the {@link Level#SEVERE} severity level and 
     * associated throwable
     *
     * @param message   The component message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, Component, Throwable)
     */
    public static void severe(
            final @NotNull Component message,
            final @NotNull Throwable throwable
    ) {
        log(Level.SEVERE, message, throwable);
    }

    /**
     * Logs a message with the {@link Level#WARNING} severity level
     *
     * @param message The string message
     * @see #log(Level, String)
     */
    public static void warning(final @NotNull String message) {
        log(Level.WARNING, message);
    }

    /**
     * Logs a message with the {@link Level#WARNING} severity level
     *
     * @param message The component message
     * @see #log(Level, Component)
     */
    public static void warning(final @NotNull Component message) {
        log(Level.WARNING, message);
    }

    /**
     * Logs a message with the {@link Level#WARNING} severity level and 
     * associated array of parameters
     *
     * @param message The string message
     * @param params  Array of parameters to the message
     * @see #log(Level, String, Object...)
     */
    public static void warning(
            final @NotNull String message,
            final Object @NotNull ... params
    ) {
        log(Level.WARNING, message, params);
    }

    /**
     * Logs a message with the {@link Level#WARNING} severity level and
     * associated array of parameters
     *
     * @param message The component message
     * @param params  Array of parameters to the message
     * @see #log(Level, Component, Object...)
     */
    public static void warning(
            final @NotNull Component message,
            final Object @NotNull ... params
    ) {
        log(Level.WARNING, message, params);
    }

    /**
     * Logs a message with the {@link Level#WARNING} severity level and 
     * associated throwable
     *
     * @param message   The string message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, String, Throwable)
     */
    public static void warning(
            final @NotNull String message,
            final @NotNull Throwable throwable
    ) {
        log(Level.WARNING, message, throwable);
    }

    /**
     * Logs a message with the {@link Level#WARNING} severity level and 
     * associated throwable
     *
     * @param message   The component message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, Component, Throwable)
     */
    public static void warning(
            final @NotNull Component message,
            final @NotNull Throwable throwable
    ) {
        log(Level.WARNING, message, throwable);
    }

    /**
     * Logs a message with the {@link Level#INFO} severity level
     *
     * @param message The string message
     * @see #log(Level, String)
     */
    public static void info(final @NotNull String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message with the {@link Level#INFO} severity level
     *
     * @param message The component message
     * @see #log(Level, Component)
     */
    public static void info(final @NotNull Component message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message with the {@link Level#INFO} severity level and associated 
     * array of parameters
     *
     * @param message The string message
     * @param params  Array of parameters to the message
     * @see #log(Level, String, Object...)
     */
    public static void info(
            final @NotNull String message,
            final Object @NotNull ... params
    ) {
        log(Level.INFO, message, params);
    }

    /**
     * Logs a message with the {@link Level#INFO}  severity level and associated 
     * array of parameters
     *
     * @param message The component message
     * @param params  Array of parameters to the message
     * @see #log(Level, Component, Object...)
     */
    public static void info(
            final @NotNull Component message,
            final Object @NotNull ... params
    ) {
        log(Level.INFO, message, params);
    }

    /**
     * Logs a message with the {@link Level#INFO} severity level and associated 
     * throwable
     *
     * @param message   The string message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, String, Throwable)
     */
    public static void info(
            final @NotNull String message,
            final @NotNull Throwable throwable
    ) {
        log(Level.INFO, message, throwable);
    }

    /**
     * Logs a message with the {@link Level#INFO}  severity level and associated 
     * throwable
     *
     * @param message   The component message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, Component, Throwable)
     */
    public static void info(
            final @NotNull Component message,
            final @NotNull Throwable throwable
    ) {
        log(Level.INFO, message, throwable);
    }

    /**
     * Logs a message with the {@link Level#FINE} severity level
     *
     * @param message The string message
     * @see #log(Level, String)
     */
    public static void fine(final @NotNull String message) {
        log(Level.FINE, message);
    }

    /**
     * Logs a message with the {@link Level#FINE} severity level
     *
     * @param message The component message
     * @see #log(Level, Component)
     */
    public static void fine(final @NotNull Component message) {
        log(Level.FINE, message);
    }

    /**
     * Logs a message with the {@link Level#FINE}  severity level and associated 
     * array of parameters
     *
     * @param message The string message
     * @param params  Array of parameters to the message
     * @see #log(Level, String, Object...)
     */
    public static void fine(
            final @NotNull String message,
            final Object @NotNull ... params
    ) {
        log(Level.FINE, message, params);
    }

    /**
     * Logs a message with the {@link Level#FINE} severity level and associated 
     * array of parameters
     *
     * @param message The component message
     * @param params  Array of parameters to the message
     * @see #log(Level, Component, Object...)
     */
    public static void fine(
            final @NotNull Component message,
            final Object @NotNull ... params
    ) {
        log(Level.FINE, message, params);
    }

    /**
     * Logs a message with the {@link Level#FINE} severity level and associated 
     * throwable
     *
     * @param message   The string message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, String, Throwable)
     */
    public static void fine(
            final @NotNull String message,
            final @NotNull Throwable throwable
    ) {
        log(Level.FINE, message, throwable);
    }

    /**
     * Logs a message with the {@link Level#FINE} severity level and associated 
     * throwable
     *
     * @param message   The component message
     * @param throwable Throwable associated with a log message
     * @see #log(Level, Component, Throwable)
     */
    public static void fine(
            final @NotNull Component message,
            final @NotNull Throwable throwable
    ) {
        log(Level.FINE, message, throwable);
    }

    /**
     * Serializes a component message to a string
     *
     * @param message The component message
     * @return The serialized string
     */
    private static @NotNull String serialize(final @NotNull Component message) {
        return ANSI_SERIALIZER.serialize(
                GlobalTranslator.render(
                        message,
                        Locale.getDefault()
                )
        );
    }
}
