package com.minersstudios.whomine.api.utility;

import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.Contract;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Shared constants
 */
@SuppressWarnings("unused")
public final class SharedConstants {
    //<editor-fold desc="Constants" defaultstate="collapsed">
    public static final String MINECRAFT_VERSION =               "1.21.1";
    public static final int PROTOCOL_VERSION =                   767;

    public static final String GLOBAL_FOLDER_PATH =              "config/minersstudios/";
    public static final String LANGUAGE_FOLDER_PATH =            GLOBAL_FOLDER_PATH + "language/";

    public static final String DATE_FORMAT =                     "EEE, yyyy-MM-dd HH:mm z";
    public static final DateTimeFormatter DATE_TIME_FORMATTER =  DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static final String DEFAULT_LANGUAGE_CODE =           "en_us";
    public static final Locale DEFAULT_LOCALE =                  Objects.requireNonNull(Translator.parseLocale(DEFAULT_LANGUAGE_CODE), "Not found default locale for " + DEFAULT_LANGUAGE_CODE);

    public static final String DISCORD_LINK =                    "https://discord.whomine.net";
    public static final String CONSOLE_NICKNAME =                "$Console";
    public static final String INVISIBLE_ITEM_FRAME_TAG =        "invisibleItemFrame";
    public static final String HIDE_TAGS_TEAM_NAME =             "hide_tags";

    public static final int SIT_RANGE =                          9;
    public static final int FINAL_DESTROY_STAGE =                9;
    public static final int DECOR_MAX_STACK_SIZE =               8;
    //</editor-fold>

    @Contract(" -> fail")
    private SharedConstants() throws AssertionError {
        throw new AssertionError("Utility class");
    }
}
