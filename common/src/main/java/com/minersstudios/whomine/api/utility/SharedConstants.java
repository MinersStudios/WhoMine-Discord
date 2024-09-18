package com.minersstudios.whomine.api.utility;

import com.minersstudios.whomine.api.annotation.Resource;
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
    public static final String MINECRAFT_VERSION =               "1.21.1";
    public static final int PROTOCOL_VERSION =                   767;
    public static final String GLOBAL_PACKAGE =                  "com.minersstudios.whomine";
    public static final String GLOBAL_FOLDER_PATH =              "config/minersstudios/";
    public static final String LANGUAGE_FOLDER_PATH =            GLOBAL_FOLDER_PATH + "language/";
    public static final String DATE_FORMAT =                     "EEE, yyyy-MM-dd HH:mm z";
    public static final String DEFAULT_LANGUAGE_CODE =           "en_us";
    public static final String DISCORD_LINK =                    "https://discord.whomine.net";
    public static final String CONSOLE_NICKNAME =                "$Console";
    public static final String INVISIBLE_ITEM_FRAME_TAG =        "invisibleItemFrame";
    public static final String HIDE_TAGS_TEAM_NAME =             "hide_tags";
    public static final @Resource String WHOMINE_NAMESPACE =     "whomine";
    public static final @Resource String MSBLOCK_NAMESPACE =     "msblock";
    public static final @Resource String MSITEMS_NAMESPACE =     "msitems";
    public static final @Resource String MSDECOR_NAMESPACE =     "msdecor";
    public static final Locale DEFAULT_LOCALE =                  Objects.requireNonNull(Translator.parseLocale(DEFAULT_LANGUAGE_CODE), "Not found default locale for " + DEFAULT_LANGUAGE_CODE);
    public static final DateTimeFormatter DATE_TIME_FORMATTER =  DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final int SIT_RANGE =                          9;
    public static final int FINAL_DESTROY_STAGE =                9;
    public static final int LEATHER_HORSE_ARMOR_MAX_STACK_SIZE = 8;

    @Contract(" -> fail")
    private SharedConstants() throws AssertionError {
        throw new AssertionError("Utility class");
    }
}
