package com.minersstudios.whomine;

import com.google.common.base.Joiner;
import com.minersstudios.whomine.custom.anomaly.Anomaly;
import com.minersstudios.whomine.custom.anomaly.task.AnomalyParticleTask;
import com.minersstudios.whomine.custom.anomaly.task.MainAnomalyActionTask;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.custom.item.renameable.RenameableItem;
import com.minersstudios.whomine.custom.item.renameable.RenameableItemRegistry;
import com.minersstudios.whomine.locale.LangFileFabric;
import com.minersstudios.whomine.api.locale.TranslationRegistry;
import com.minersstudios.whomine.menu.CraftsMenu;
import com.minersstudios.whomine.menu.RenamesMenu;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.resourcepack.ResourcePack;
import com.minersstudios.whomine.resourcepack.throwable.FatalPackLoadException;
import com.minersstudios.whomine.api.status.StatusHandler;
import com.minersstudios.whomine.api.status.StatusWatcher;
import com.minersstudios.whomine.api.throwable.ConfigurationException;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.api.utility.SharedConstants;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.translation.Translator;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.kyori.adventure.text.Component.text;

public final class Config {
    private final WhoMine plugin;
    private final File file;
    private final YamlConfiguration yaml;
    private final Logger logger;

    private long dosimeterCheckRate;
    private String woodSoundPlace;
    private String woodSoundBreak;
    private String woodSoundStep;
    private String woodSoundHit;
    private boolean developerMode;
    private long anomalyCheckRate;
    private long anomalyParticlesCheckRate;
    private long discordServerId;
    private long memberRoleId;
    private long discordGlobalChannelId;
    private long discordLocalChannelId;
    private double localChatRadius;
    private String mineSkinApiKey;
    private Location spawnLocation;
    private String dateFormat;
    private boolean isChristmas;
    private boolean isHalloween;
    private String languageDefaultCode;

    private DateTimeFormatter dateFormatter;
    private Locale defaultLocale;
    private List<Locale> locales;

    //<editor-fold desc="File paths" defaultstate="collapsed">
    private static final String JSON_EXTENSION = ".json";
    private static final String YAML_EXTENSION = ".yml";

    /** The custom block configurations folder */
    public static final String BLOCKS_FOLDER = "blocks";

    /** The name of the renameable item configurations folder */
    public static final String ITEMS_FOLDER = "items";

    /** The player data folder */
    public static final String PLAYERS_FOLDER = "players";

    /** The anomaly configurations folder */
    public static final String ANOMALIES_FOLDER = "anomalies";

    /** The console player data file name */
    public static final String CONSOLE_FILE_NAME = "console" + YAML_EXTENSION;

    /** The path in the plugin folder to the console player data file */
    public static final String CONSOLE_FILE_PATH = PLAYERS_FOLDER + '/' + CONSOLE_FILE_NAME;
    //</editor-fold>

    //<editor-fold desc="Config keys" defaultstate="collapsed">
    public static final String KEY_DATE_FORMAT =      "date-format";
    public static final String KEY_IS_CHRISTMAS =     "is-christmas";
    public static final String KEY_IS_HALLOWEEN =     "is-halloween";

    public static final String KEY_LANGUAGE_SECTION = "language";
    public static final String KEY_DEFAULT_CODE =     "default-code";
    public static final String KEY_CODES =            "codes";

    public static final String KEY_DOSIMETER_CHECK_RATE =         "dosimeter-check-rate";

    public static final String KEY_WOOD_SOUND_SECTION =           "wood-sound";
    public static final String KEY_PLACE =                        "place";
    public static final String KEY_BREAK =                        "break";
    public static final String KEY_STEP =                         "step";
    public static final String KEY_HIT =                          "hit";

    public static final String KEY_DEVELOPER_MODE =               "developer-mode";
    public static final String KEY_ANOMALY_CHECK_RATE =           "anomaly-check-rate";
    public static final String KEY_ANOMALY_PARTICLES_CHECK_RATE = "anomaly-particles-check-rate";

    public static final String KEY_SKIN_SECTION =                 "skin";
    public static final String KEY_MINE_SKIN_API_KEY =            "mine-skin-api-key";

    public static final String KEY_DISCORD_SECTION =              "discord";
    public static final String KEY_SERVER_ID =                    "server-id";
    public static final String KEY_MEMBER_ROLE_ID =               "member-role-id";
    public static final String KEY_BOT_TOKEN =                    "bot-token";

    public static final String KEY_CHAT_SECTION =                 "chat";
    public static final String KEY_LOCAL_SECTION =                "local";
    public static final String KEY_GLOBAL_SECTION =               "global";
    public static final String KEY_RADIUS =                       "radius";
    public static final String KEY_DISCORD_CHANNEL_ID =           "discord-channel-id";

    public static final String KEY_SPAWN_LOCATION_SECTION =       "spawn-location";
    public static final String KEY_WORLD =                        "world";
    public static final String KEY_X =                            "x";
    public static final String KEY_Y =                            "y";
    public static final String KEY_Z =                            "z";
    public static final String KEY_YAW =                          "yaw";
    public static final String KEY_PITCH =                        "pitch";

    public static final String KEY_RESOURCE_PACKS_SECTION =       "resource-packs";
    //</editor-fold>

    //<editor-fold desc="Config default values" defaultstate="collapsed">
    public static final long DEFAULT_DOSIMETER_CHECK_RATE =         100;
    public static final String DEFAULT_WOOD_SOUND_PLACE =           "custom.block.wood.place";
    public static final String DEFAULT_WOOD_SOUND_BREAK =           "custom.block.wood.break";
    public static final String DEFAULT_WOOD_SOUND_STEP =            "custom.block.wood.step";
    public static final String DEFAULT_WOOD_SOUND_HIT =             "custom.block.wood.hit";
    public static final boolean DEFAULT_DEVELOPER_MODE =            false;
    public static final long DEFAULT_ANOMALY_CHECK_RATE =           100L;
    public static final long DEFAULT_ANOMALY_PARTICLES_CHECK_RATE = 10L;
    public static final double DEFAULT_LOCAL_CHAT_RADIUS =          25.0d;
    public static final long DEFAULT_DISCORD_CHANNEL_ID =           -1;
    public static final char DEFAULT_BOT_TOKEN =                    ' ';
    public static final long DEFAULT_SERVER_ID =                    -1;
    public static final long DEFAULT_MEMBER_ROLE_ID =               -1;
    public static final char DEFAULT_MINE_SKIN_API_KEY =            ' ';
    //</editor-fold>

    Config(final @NotNull WhoMine plugin) {
        this.plugin = plugin;
        this.file = plugin.getConfigFile();
        this.yaml = new YamlConfiguration();
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
    }

    public @NotNull File getFile() {
        return this.file;
    }

    public @NotNull YamlConfiguration getYaml() {
        return this.yaml;
    }

    public @NotNull Logger getLogger() {
        return this.logger;
    }

    public void setIfNotExists(
            final @NotNull String path,
            final @Nullable Object value
    ) {
        if (!this.yaml.isSet(path)) {
            this.yaml.set(path, value);
        }
    }

    public boolean reload() {
        final StatusHandler statusHandler = this.plugin.getStatusHandler();

        statusHandler.assignStatus(WhoMine.LOADING_CONFIG);

        try {
            this.saveDefaultConfig();
            this.reloadVariables();
            statusHandler.assignStatus(WhoMine.LOADED_CONFIG);

            return true;
        } catch (final ConfigurationException e) {
            statusHandler.assignStatus(WhoMine.FAILED_LOAD_CONFIG);
            MSLogger.severe("An error occurred while loading the config!", e);

            return false;
        }
    }

    public void reloadVariables() {
        this.createDirectories();
        this.parseLanguages();
        this.parseSounds();
        this.parseChat();
        this.parseDiscord();
        this.parseSkins();

        final YamlConfiguration yaml = this.getYaml();
        this.isChristmas = yaml.getBoolean(KEY_IS_CHRISTMAS);
        this.isHalloween = yaml.getBoolean(KEY_IS_HALLOWEEN);
        this.dateFormat = yaml.getString(KEY_DATE_FORMAT, SharedConstants.DATE_FORMAT);
        this.dateFormatter = DateTimeFormatter.ofPattern(this.dateFormat);
        this.dosimeterCheckRate = yaml.getLong(KEY_DOSIMETER_CHECK_RATE, DEFAULT_DOSIMETER_CHECK_RATE);
        this.developerMode = yaml.getBoolean(KEY_DEVELOPER_MODE);
        this.anomalyCheckRate = yaml.getLong(KEY_ANOMALY_CHECK_RATE);
        this.anomalyParticlesCheckRate = yaml.getLong(KEY_ANOMALY_PARTICLES_CHECK_RATE);

        final Cache cache = this.plugin.getCache();

        if (cache.isLoaded()) {
            for (final var task : cache.getBukkitTasks()) {
                task.cancel();
            }

            cache.getBukkitTasks().clear();
            cache.getPlayerAnomalyActionMap().clear();
            cache.getAnomalies().clear();
        }

        this.plugin.getStatusHandler().addWatcher(
                StatusWatcher.builder()
                .successStatuses(
                        WhoMine.LOADED_BLOCKS,
                        WhoMine.LOADED_ITEMS,
                        WhoMine.LOADED_DECORATIONS
                )
                .successRunnable(
                        () -> {
                            this.plugin.runTaskAsync(this::loadRenames);
                            this.plugin.runTask(() -> {
                                final var list = this.plugin.getCache().getBlockDataRecipes();

                                for (final var entry : list) {
                                    entry.getKey().registerRecipes(
                                            this.plugin,
                                            entry.getValue()
                                    );
                                }

                                list.clear();
                                CraftsMenu.putCrafts(
                                        CraftsMenu.Type.BLOCKS,
                                        this.plugin.getCache().customBlockRecipes
                                );
                            });
                        }
                )
                .build()
        );
    }

    public void reloadDefaultVariables() {
        this.setIfNotExists(KEY_DATE_FORMAT, SharedConstants.DATE_FORMAT);
        this.setIfNotExists(KEY_IS_CHRISTMAS, false);
        this.setIfNotExists(KEY_IS_HALLOWEEN, false);
        this.setIfNotExists(KEY_LANGUAGE_SECTION + '.' + KEY_DEFAULT_CODE, SharedConstants.DEFAULT_LANGUAGE_CODE);

        this.setIfNotExists(KEY_DOSIMETER_CHECK_RATE, DEFAULT_DOSIMETER_CHECK_RATE);

        this.setIfNotExists(KEY_WOOD_SOUND_SECTION + '.' + KEY_PLACE, DEFAULT_WOOD_SOUND_PLACE);
        this.setIfNotExists(KEY_WOOD_SOUND_SECTION + '.' + KEY_BREAK, DEFAULT_WOOD_SOUND_BREAK);
        this.setIfNotExists(KEY_WOOD_SOUND_SECTION + '.' + KEY_STEP, DEFAULT_WOOD_SOUND_STEP);
        this.setIfNotExists(KEY_WOOD_SOUND_SECTION + '.' + KEY_HIT, DEFAULT_WOOD_SOUND_HIT);

        this.setIfNotExists(KEY_DEVELOPER_MODE, DEFAULT_DEVELOPER_MODE);
        this.setIfNotExists(KEY_ANOMALY_CHECK_RATE, DEFAULT_ANOMALY_CHECK_RATE);
        this.setIfNotExists(KEY_ANOMALY_PARTICLES_CHECK_RATE, DEFAULT_ANOMALY_PARTICLES_CHECK_RATE);

        this.setIfNotExists(KEY_CHAT_SECTION + '.' + KEY_LOCAL_SECTION + '.' + KEY_RADIUS, DEFAULT_LOCAL_CHAT_RADIUS);
        this.setIfNotExists(KEY_CHAT_SECTION + '.' + KEY_LOCAL_SECTION + '.' + KEY_DISCORD_CHANNEL_ID, DEFAULT_DISCORD_CHANNEL_ID);
        this.setIfNotExists(KEY_CHAT_SECTION + '.' + KEY_GLOBAL_SECTION + '.' + KEY_DISCORD_CHANNEL_ID, DEFAULT_DISCORD_CHANNEL_ID);

        this.setIfNotExists(KEY_DISCORD_SECTION + '.' + KEY_BOT_TOKEN, DEFAULT_BOT_TOKEN);
        this.setIfNotExists(KEY_DISCORD_SECTION + '.' + KEY_SERVER_ID, DEFAULT_SERVER_ID);
        this.setIfNotExists(KEY_DISCORD_SECTION + '.' + KEY_MEMBER_ROLE_ID, DEFAULT_MEMBER_ROLE_ID);

        this.setIfNotExists(KEY_SKIN_SECTION + '.' + KEY_MINE_SKIN_API_KEY, DEFAULT_MINE_SKIN_API_KEY);
    }

    public void onEnable() {
        final Cache cache = this.plugin.getCache();
        final Location mainWorldSpawn = this.plugin.getServer().getWorlds().get(0).getSpawnLocation();

        this.setIfNotExists(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_WORLD, mainWorldSpawn.getWorld().getName());
        this.setIfNotExists(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_X, mainWorldSpawn.x());
        this.setIfNotExists(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_Y, mainWorldSpawn.y());
        this.setIfNotExists(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_Z, mainWorldSpawn.z());
        this.setIfNotExists(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_YAW, mainWorldSpawn.getYaw());
        this.setIfNotExists(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_PITCH, mainWorldSpawn.getPitch());

        this.parseSpawnLocation();

        cache.getBukkitTasks().add(this.plugin.runTaskTimer(new MainAnomalyActionTask(this.plugin), 0L, this.anomalyCheckRate));
        cache.getBukkitTasks().add(this.plugin.runTaskTimer(new AnomalyParticleTask(this.plugin), 0L, this.anomalyParticlesCheckRate));

        this.plugin.runTaskAsync(this::loadResourcePacks);
        this.plugin.runTaskAsync(this::loadAnomalies);
        this.plugin.runTaskAsync(this::loadBlocks);
    }

    public void reloadYaml() throws ConfigurationException {
        try {
            this.yaml.load(this.file);
        } catch (final IOException e) {
            throw new ConfigurationException("The config file : " + this.file + " cannot be read", e);
        } catch (final InvalidConfigurationException e) {
            throw new ConfigurationException("The file : " + this.file + " is not a valid configuration", e);
        }
    }

    public boolean save() {
        try {
            this.yaml.save(this.file);

            return true;
        } catch (final IOException e) {
            MSLogger.severe("An error occurred while saving the config!", e);

            return false;
        }
    }

    public void saveDefaultConfig() throws ConfigurationException {
        try {
            if (!this.file.exists()) {
                if (this.file.getParentFile().mkdirs()) {
                    MSLogger.info("The config directory : " + this.file.getParentFile() + " was created");
                }

                if (this.file.createNewFile()) {
                    MSLogger.info("The config file : " + this.file + " was created");
                }
            }

            this.reloadYaml();
            this.reloadDefaultVariables();
            this.yaml.save(this.file);
        } catch (final IOException e) {
            throw new ConfigurationException("The config file : " + this.file + " cannot be written to", e);
        } catch (final ConfigurationException e) {
            throw new ConfigurationException("The config file : " + this.file + " cannot be read", e);
        }
    }

    @Override
    public @NotNull String toString() {
        final String path = this.file.getPath();
        final String configValues =
                Joiner.on(',')
                .withKeyValueSeparator('=')
                .join(this.yaml.getValues(true));

        return "Config{file=" + path +
                ", config=[" + configValues +
                "]}";
    }

    public @NotNull WhoMine getPlugin() {
        return this.plugin;
    }

    public @Nullable String getDateFormat() {
        return this.dateFormat;
    }

    public boolean isChristmas() {
        return this.isChristmas;
    }

    public boolean isHalloween() {
        return this.isHalloween;
    }

    public @UnknownNullability String getLanguageDefaultCode() {
        return this.languageDefaultCode;
    }

    public @UnknownNullability DateTimeFormatter getDateFormatter() {
        return this.dateFormatter;
    }

    public @UnknownNullability Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    public @UnknownNullability List<Locale> getLocales() {
        return this.locales;
    }

    public long getDosimeterCheckRate() {
        return this.dosimeterCheckRate;
    }

    public @UnknownNullability String getWoodSoundPlace() {
        return this.woodSoundPlace;
    }

    public @UnknownNullability String getWoodSoundBreak() {
        return this.woodSoundBreak;
    }

    public @UnknownNullability String getWoodSoundStep() {
        return this.woodSoundStep;
    }

    public @UnknownNullability String getWoodSoundHit() {
        return this.woodSoundHit;
    }

    public boolean isDeveloperMode() {
        return this.developerMode;
    }

    public void setDeveloperMode(final boolean developerMode) {
        this.developerMode = developerMode;

        this.getYaml().set(KEY_DEVELOPER_MODE, developerMode);

        this.save();
    }

    public long getAnomalyCheckRate() {
        return this.anomalyCheckRate;
    }

    public void setAnomalyCheckRate(final long rate) {
        this.anomalyCheckRate = rate;

        this.getYaml().set(KEY_ANOMALY_CHECK_RATE, rate);

        this.save();
    }

    public long getAnomalyParticlesCheckRate() {
        return this.anomalyParticlesCheckRate;
    }

    public void setAnomalyParticlesCheckRate(final long rate) {
        this.anomalyParticlesCheckRate = rate;

        this.getYaml().set(KEY_ANOMALY_PARTICLES_CHECK_RATE, rate);

        this.save();
    }

    public long getDiscordServerId() {
        return this.discordServerId;
    }

    public void setDiscordServerId(final long id) {
        this.discordServerId = id;

        this.getYaml().set(KEY_DISCORD_SECTION + '.' + KEY_SERVER_ID, id);

        this.save();
    }

    public long getMemberRoleId() {
        return this.memberRoleId;
    }

    public void setMemberRoleId(final long id) {
        this.memberRoleId = id;

        this.getYaml().set(KEY_DISCORD_SECTION + '.' + KEY_MEMBER_ROLE_ID, id);

        this.save();
    }

    public long getDiscordGlobalChannelId() {
        return this.discordGlobalChannelId;
    }

    public void setDiscordGlobalChannelId(final long id) {
        this.discordGlobalChannelId = id;

        this.getYaml().set(KEY_CHAT_SECTION + '.' + KEY_GLOBAL_SECTION + '.' + KEY_DISCORD_CHANNEL_ID, id);

        this.save();
    }

    public long getDiscordLocalChannelId() {
        return this.discordLocalChannelId;
    }

    public void setDiscordLocalChannelId(final long id) {
        this.discordLocalChannelId = id;

        this.getYaml().set(KEY_CHAT_SECTION + '.' + KEY_LOCAL_SECTION + '.' + KEY_DISCORD_CHANNEL_ID, id);

        this.save();
    }

    public double getLocalChatRadius() {
        return this.localChatRadius;
    }

    public void setLocalChatRadius(final double radius) {
        this.localChatRadius = radius;

        this.getYaml().set(KEY_CHAT_SECTION + '.' + KEY_LOCAL_SECTION + '.' + KEY_RADIUS, radius);

        this.save();
    }

    public @Nullable String getMineSkinApiKey() {
        return this.mineSkinApiKey;
    }

    public void setMineSkinApiKey(final @Nullable String apiKey) {
        this.mineSkinApiKey = apiKey;

        this.getYaml().set(KEY_SKIN_SECTION + '.' + KEY_MINE_SKIN_API_KEY, apiKey);

        this.save();
    }

    public @NotNull Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public void setSpawnLocation(final @NotNull Location location) {
        final YamlConfiguration yaml = this.getYaml();
        this.spawnLocation = location;

        yaml.set(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_WORLD, location.getWorld().getName());
        yaml.set(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_X, location.x());
        yaml.set(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_Y, location.y());
        yaml.set(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_Z, location.z());
        yaml.set(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_YAW, location.getYaw());
        yaml.set(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_PITCH, location.getPitch());

        this.save();
    }

    private void createDirectories() {
        this.createDirectory(BLOCKS_FOLDER);
        this.createDirectory(ITEMS_FOLDER);
        this.createDirectory(PLAYERS_FOLDER);
        this.createDirectory(ANOMALIES_FOLDER);
    }

    private void createDirectory(final @NotNull String directoryPath) {
        final File directory = new File(this.plugin.getDataFolder(), directoryPath);

        if (
                !directory.exists()
                && !directory.mkdirs()
        ) {
            this.plugin.getLogger().warning("Failed to create directory: " + directoryPath);
        }
    }

    private void parseSounds() {
        final ConfigurationSection woodSoundSection = this.getYaml().getConfigurationSection(KEY_WOOD_SOUND_SECTION);

        if (woodSoundSection != null) {
            this.woodSoundPlace = woodSoundSection.getString(KEY_PLACE);
            this.woodSoundBreak = woodSoundSection.getString(KEY_BREAK);
            this.woodSoundStep = woodSoundSection.getString(KEY_STEP);
            this.woodSoundHit = woodSoundSection.getString(KEY_HIT);
        }

        if (ChatUtils.isBlank(this.woodSoundPlace)) {
            this.woodSoundPlace = DEFAULT_WOOD_SOUND_PLACE;
        }

        if (ChatUtils.isBlank(this.woodSoundBreak)) {
            this.woodSoundBreak = DEFAULT_WOOD_SOUND_BREAK;
        }

        if (ChatUtils.isBlank(this.woodSoundStep)) {
            this.woodSoundStep = DEFAULT_WOOD_SOUND_STEP;
        }

        if (ChatUtils.isBlank(this.woodSoundHit)) {
            this.woodSoundHit = DEFAULT_WOOD_SOUND_HIT;
        }
    }

    private void parseChat() {
        final ConfigurationSection chatSection = this.getYaml().getConfigurationSection(KEY_CHAT_SECTION);

        if (chatSection != null) {
            final ConfigurationSection localSection = chatSection.getConfigurationSection(KEY_LOCAL_SECTION);

            if (localSection != null) {
                this.localChatRadius = localSection.getDouble(KEY_RADIUS);
                this.discordLocalChannelId = localSection.getLong(KEY_DISCORD_CHANNEL_ID);
            }

            final ConfigurationSection globalSection = chatSection.getConfigurationSection(KEY_GLOBAL_SECTION);

            if (globalSection != null) {
                this.discordGlobalChannelId = globalSection.getLong(KEY_DISCORD_CHANNEL_ID);
            }
        }
    }

    private void parseDiscord() {
        final ConfigurationSection discordSection = this.getYaml().getConfigurationSection(KEY_DISCORD_SECTION);

        if (discordSection != null) {
            this.discordServerId = discordSection.getLong(KEY_SERVER_ID);
            this.memberRoleId = discordSection.getLong(KEY_MEMBER_ROLE_ID);
        }
    }

    private void parseSkins() {
        final ConfigurationSection skinSection = this.getYaml().getConfigurationSection(KEY_SKIN_SECTION);

        if (skinSection != null) {
            this.mineSkinApiKey = skinSection.getString(KEY_MINE_SKIN_API_KEY);
        }
    }

    private void parseSpawnLocation() {
        final YamlConfiguration yaml = this.getYaml();
        final Server server = this.plugin.getServer();
        final String spawnLocationWorldName = yaml.getString(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_WORLD, "");
        final World spawnLocationWorld = server.getWorld(spawnLocationWorldName);
        final double spawnLocationX = yaml.getDouble(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_X);
        final double spawnLocationY = yaml.getDouble(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_Y);
        final double spawnLocationZ = yaml.getDouble(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_Z);
        final float spawnLocationYaw = (float) yaml.getDouble(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_YAW);
        final float spawnLocationPitch = (float) yaml.getDouble(KEY_SPAWN_LOCATION_SECTION + '.' + KEY_PITCH);

        if (spawnLocationWorld == null) {
            this.plugin.getLogger().warning("World \"" + spawnLocationWorldName + "\" not found!\nUsing default spawn location!");

            this.spawnLocation = server.getWorlds().get(0).getSpawnLocation();
        } else {
            this.spawnLocation = new Location(
                    spawnLocationWorld,
                    spawnLocationX,
                    spawnLocationY,
                    spawnLocationZ,
                    spawnLocationYaw,
                    spawnLocationPitch
            );
        }
    }

    private void parseConsoleInfo() {
        final File consoleDataFile = new File(this.plugin.getDataFolder(), CONSOLE_FILE_PATH);

        if (!consoleDataFile.exists()) {
            this.plugin.saveResource(CONSOLE_FILE_PATH, false);
        }

        this.plugin.getCache().consolePlayerInfo = new PlayerInfo(this.plugin, UUID.randomUUID(), SharedConstants.CONSOLE_NICKNAME);
    }

    private void parseLanguages() throws IllegalStateException {
        final ConfigurationSection languageSection = this.getYaml().getConfigurationSection(KEY_LANGUAGE_SECTION);

        if (languageSection == null) {
            throw new IllegalStateException("Language section cannot be null");
        }

        this.languageDefaultCode = languageSection.getString(KEY_DEFAULT_CODE, SharedConstants.DEFAULT_LANGUAGE_CODE);
        this.defaultLocale = Translator.parseLocale(this.languageDefaultCode);

        if (this.defaultLocale == null) {
            this.defaultLocale = SharedConstants.DEFAULT_LOCALE;
        }

        this.locales = new ObjectArrayList<>();

        this.locales.add(this.defaultLocale);

        for (final var tag : languageSection.getStringList(KEY_CODES)) {
            final Locale locale = Translator.parseLocale(tag);

            if (locale == null) {
                this.plugin.getLogger().warning("Invalid language tag: " + tag);
            } else {
                this.locales.add(locale);
            }
        }

        this.loadLanguages();
    }

    private void loadLanguages() {
        final StatusHandler statusHandler = this.plugin.getStatusHandler();
        final YamlConfiguration yaml = this.getYaml();
        final ConfigurationSection languageSection =
                yaml.getConfigurationSection(KEY_LANGUAGE_SECTION + '.' + KEY_CODES);

        statusHandler.assignStatus(WhoMine.LOADING_LANGUAGES);

        if (languageSection == null) {
            statusHandler.assignStatus(WhoMine.LOADED_LANGUAGES);

            return;
        }

        final long start = System.currentTimeMillis();

        CompletableFuture
        .allOf(
                LangFileFabric
                .allFromSection(
                        this.getFile(),
                        yaml,
                        languageSection,
                        languageFile -> {
                            TranslationRegistry.registry().registerAll(languageFile);
                            MSLogger.fine(
                                    text("Loaded language : ")
                                    .append(text(languageFile.getLocale().getDisplayName()))
                                    .append(text(" with "))
                                    .append(text(String.valueOf(languageFile.size())))
                                    .append(text(" translations in "))
                                    .append(text(System.currentTimeMillis() - start))
                                    .append(text("ms"))
                            );
                        },
                        (localeTag, throwable) ->
                                MSLogger.warning(
                                        text("Failed to load language \"")
                                        .append(text(localeTag))
                                        .append(text('"')),
                                        throwable
                                )
                )
                .values()
                .toArray(CompletableFuture[]::new)
        )
        .thenRun(() -> {
            TranslationRegistry.registerGlobal();
            statusHandler.assignStatus(WhoMine.LOADED_LANGUAGES);
        });
    }

    private void loadResourcePacks() {
        final StatusHandler statusHandler = this.plugin.getStatusHandler();
        final YamlConfiguration yaml = this.getYaml();
        final ConfigurationSection resourcePacksSection = yaml.getConfigurationSection(KEY_RESOURCE_PACKS_SECTION);

        statusHandler.assignStatus(WhoMine.LOADING_RESOURCE_PACKS);

        if (resourcePacksSection == null) {
            statusHandler.assignStatus(WhoMine.LOADED_RESOURCE_PACKS);

            return;
        }

        final ComponentLogger logger = this.plugin.getComponentLogger();
        final long start = System.currentTimeMillis();
        final Map<String, CompletableFuture<ResourcePack>> futureMap;

        try {
            futureMap = ResourcePack.loadAll(
                    this.getFile(),
                    yaml,
                    resourcePacksSection,
                    entry -> {
                        logger.info(
                                text("Loaded resource pack \"")
                                .append(text(entry.getKey()))
                                .append(text("\" in "))
                                .append(text(System.currentTimeMillis() - start))
                                .append(text("ms"))
                        );

                        return entry;
                    },
                    (entry, throwable) -> {
                        logger.warn(
                                text("Failed to load resource pack \"")
                                .append(text(entry.getKey()))
                                .append(text('"'))
                                .append(text(" this resource pack will be disabled!")),
                                throwable
                        );

                        return entry;
                    }
            );
        } catch (final FatalPackLoadException e) {
            statusHandler.assignStatus(WhoMine.FAILED_LOAD_RESOURCE_PACKS);
            logger.error(
                    "Failed to load resource packs due to a fatal error!",
                    e
            );

            return;
        }

        CompletableFuture
        .allOf(
                futureMap
                .values()
                .toArray(CompletableFuture[]::new)
        )
        .thenRun(() -> statusHandler.assignStatus(WhoMine.LOADED_RESOURCE_PACKS));
    }

    private void loadAnomalies() {
        final StatusHandler statusHandler = this.plugin.getStatusHandler();
        final Cache cache = this.plugin.getCache();
        final Logger logger = this.plugin.getLogger();

        statusHandler.assignStatus(WhoMine.LOADING_ANOMALIES);

        try (final var path = Files.walk(Paths.get(this.getFile().getParent() + '/' + ANOMALIES_FOLDER))) {
            path.parallel()
            .filter(file -> file.getFileName().toString().endsWith(YAML_EXTENSION))
            .map(Path::toFile)
            .forEach(file -> {
                try {
                    final Anomaly anomaly = Anomaly.fromConfig(this.plugin, file);

                    cache.getAnomalies().put(anomaly.getNamespacedKey(), anomaly);
                } catch (final IllegalArgumentException e) {
                    logger.log(
                            Level.SEVERE,
                            "An error occurred while loading anomaly \"" + file.getName() + "\"!",
                            e
                    );
                }
            });

            statusHandler.assignStatus(WhoMine.LOADED_ANOMALIES);
        } catch (final IOException e) {
            statusHandler.assignStatus(WhoMine.FAILED_LOAD_ANOMALIES);
            logger.log(
                    Level.SEVERE,
                    "An error occurred while loading anomalies!",
                    e
            );
        }
    }

    private void loadBlocks() {
        final long start = System.currentTimeMillis();
        final StatusHandler statusHandler = this.plugin.getStatusHandler();

        statusHandler.assignStatus(WhoMine.LOADING_BLOCKS);

        try (final var pathStream = Files.walk(Paths.get(this.getFile().getParent() + '/' + BLOCKS_FOLDER))) {
            pathStream.parallel()
            .filter(file -> file.getFileName().toString().endsWith(JSON_EXTENSION))
            .map(path -> CustomBlockData.fromFile(this.plugin, path.toFile()))
            .filter(Objects::nonNull)
            .forEach(CustomBlockRegistry::register);

            statusHandler.assignStatus(WhoMine.LOADED_BLOCKS);
            this.plugin.getComponentLogger().info(
                    Component.text(
                            "Loaded " + CustomBlockRegistry.size() + " custom blocks in " + (System.currentTimeMillis() - start) + "ms",
                            NamedTextColor.GREEN
                    )
            );
        } catch (final IOException e) {
            statusHandler.assignStatus(WhoMine.FAILED_LOAD_BLOCKS);
            this.plugin.getLogger().log(
                    Level.SEVERE,
                    "An error occurred while loading blocks",
                    e
            );
        }
    }

    private void loadRenames() {
        final long start = System.currentTimeMillis();
        final StatusHandler statusHandler = this.plugin.getStatusHandler();

        statusHandler.assignStatus(WhoMine.LOADING_RENAMEABLES);

        try (final var pathStream = Files.walk(Paths.get(this.getFile().getParent() + '/' + ITEMS_FOLDER))) {
            pathStream.parallel()
            .filter(file -> file.getFileName().toString().endsWith(YAML_EXTENSION))
            .map(path -> RenameableItem.fromFile(this.plugin, path.toFile()))
            .filter(Objects::nonNull)
            .forEach(RenameableItemRegistry::register);

            statusHandler.assignStatus(WhoMine.LOADED_RENAMEABLES);
            this.plugin.getComponentLogger().info(
                    Component.text(
                            "Loaded " + RenameableItemRegistry.keysSize() + " renameable items in " + (System.currentTimeMillis() - start) + "ms",
                            NamedTextColor.GREEN
                    )
            );

            RenamesMenu.update(this.plugin);
        } catch (final IOException e) {
            statusHandler.assignStatus(WhoMine.FAILED_LOAD_RENAMEABLES);
            this.plugin.getLogger().log(
                    Level.SEVERE,
                    "An error occurred while loading renameable items",
                    e
            );
        }
    }
}
