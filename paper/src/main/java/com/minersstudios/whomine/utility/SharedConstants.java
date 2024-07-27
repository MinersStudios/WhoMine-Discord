package com.minersstudios.whomine.utility;

import com.minersstudios.whomine.api.annotation.Namespace;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.Contract;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import static io.papermc.paper.configuration.PaperConfigurations.CONFIG_DIR;

/**
 * Shared constants
 */
public final class SharedConstants {
    public static final String GLOBAL_PACKAGE =                        "com.minersstudios";
    public static final String GLOBAL_FOLDER_PATH =                    CONFIG_DIR + "/minersstudios/";
    public static final String LANGUAGE_FOLDER_PATH =                  GLOBAL_FOLDER_PATH + "language/";
    public static final String PAPER_GLOBAL_CONFIG_FILE_NAME =         "paper-global.yml";
    public static final String PAPER_WORLD_DEFAULTS_CONFIG_FILE_NAME = "paper-world-defaults.yml";
    public static final String PAPER_WORLD_CONFIG_FILE_NAME =          "paper-world.yml";
    public static final String PAPER_GLOBAL_CONFIG_PATH =              CONFIG_DIR + '/' + PAPER_GLOBAL_CONFIG_FILE_NAME;
    public static final String PAPER_WORLD_DEFAULTS_PATH =             CONFIG_DIR + '/' + PAPER_WORLD_DEFAULTS_CONFIG_FILE_NAME;
    public static final String DATE_FORMAT =                           "EEE, yyyy-MM-dd HH:mm z";
    public static final String DEFAULT_LANGUAGE_CODE =                 "en_us";
    public static final String DISCORD_LINK =                          "https://discord.whomine.net";
    public static final String CONSOLE_NICKNAME =                      "$Console";
    public static final String INVISIBLE_ITEM_FRAME_TAG =              "invisibleItemFrame";
    public static final String HIDE_TAGS_TEAM_NAME =                   "hide_tags";
    public static final @Namespace String WHOMINE_NAMESPACE =          "whomine";
    public static final @Namespace String MSBLOCK_NAMESPACE =          "msblock";
    public static final @Namespace String MSITEMS_NAMESPACE =          "msitems";
    public static final @Namespace String MSDECOR_NAMESPACE =          "msdecor";
    public static final Locale DEFAULT_LOCALE =                        Objects.requireNonNull(Translator.parseLocale(DEFAULT_LANGUAGE_CODE), "Not found default locale for " + DEFAULT_LANGUAGE_CODE);
    public static final DateTimeFormatter DATE_TIME_FORMATTER =        DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static final int SIT_RANGE =                                9;
    public static final int FINAL_DESTROY_STAGE =                      9;
    public static final int LEATHER_HORSE_ARMOR_MAX_STACK_SIZE =       8;

    @Contract(" -> fail")
    private SharedConstants() throws AssertionError {
        throw new AssertionError("Utility class");
    }
}
