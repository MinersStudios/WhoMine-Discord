package com.minersstudios.whomine;

import com.minersstudios.whomine.command.api.CommandManager;
import com.minersstudios.whomine.discord.DiscordManager;
import com.minersstudios.whomine.inventory.holder.AbstractInventoryHolder;
import com.minersstudios.whomine.listener.api.ListenerManager;
import com.minersstudios.whomine.scheduler.TaskExecutor;
import com.minersstudios.whomine.status.FailureStatus;
import com.minersstudios.whomine.status.StatusHandler;
import com.minersstudios.whomine.status.SuccessStatus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import static com.minersstudios.whomine.status.Status.*;

/**
 * Represents a WhoMine plugin
 */
public interface WhoMine extends Plugin, TaskExecutor {
    //<editor-fold desc="Plugin statuses" defaultstate="collapsed">
    /** High-priority plugin loading status */
    SuccessStatus LOADING = successHigh("LOADING");
    /** Low-priority plugin loaded status */
    SuccessStatus LOADED = successLow("LOADED");

    /** High-priority plugin enabling status */
    SuccessStatus ENABLING = successHigh("ENABLING");
    /** High-priority plugin enabled status */
    SuccessStatus ENABLED = successHigh("ENABLED");

    /** High-priority plugin disabling status */
    SuccessStatus DISABLING = successHigh("DISABLING");
    /** High-priority plugin disabled status */
    SuccessStatus DISABLED = successHigh("DISABLED");

    /** Failed low-priority status for loading cache */
    FailureStatus FAILED_LOAD_CACHE = failureLow("FAILED_LOAD_CACHE");
    /** Low-priority plugin loading cache status */
    SuccessStatus LOADING_CACHE = successLow("LOADING_CACHE");
    /** Low-priority plugin loaded cache status */
    SuccessStatus LOADED_CACHE = successLow("LOADED_CACHE", FAILED_LOAD_CACHE);

    /** Failed low-priority status for loading config */
    FailureStatus FAILED_LOAD_CONFIG = failureLow("FAILED_LOAD_CONFIG");
    /** Low-priority plugin loading config status */
    SuccessStatus LOADING_CONFIG = successLow("LOADING_CONFIG");
    /** Low-priority plugin loaded config status */
    SuccessStatus LOADED_CONFIG = successLow("LOADED_CONFIG", FAILED_LOAD_CONFIG);

    /** Low-priority plugin loading languages.yml status */
    SuccessStatus LOADING_LANGUAGES = successLow("LOADING_LANGUAGES");
    /** Low-priority plugin loaded languages.yml status */
    SuccessStatus LOADED_LANGUAGES = successLow("LOADED_LANGUAGES");

    /** Low-priority plugin decorations loading status */
    SuccessStatus LOADING_DECORATIONS = successLow("LOADING_DECORATIONS");
    /** Low-priority plugin decorations loaded status */
    SuccessStatus LOADED_DECORATIONS = successLow("LOADED_DECORATIONS");

    /** Failed low-priority status for loading blocks */
    FailureStatus FAILED_LOAD_BLOCKS = failureLow("FAILED_LOAD_BLOCKS");
    /** Low-priority plugin loading blocks status */
    SuccessStatus LOADING_BLOCKS = successLow("LOADING_BLOCKS");
    /** Low-priority plugin loaded blocks status */
    SuccessStatus LOADED_BLOCKS = successLow("LOADED_BLOCKS", FAILED_LOAD_BLOCKS);

    /** Failed low-priority status for loading items */
    SuccessStatus LOADING_ITEMS = successLow("LOADING_ITEMS");
    /** Low-priority plugin loaded items status */
    SuccessStatus LOADED_ITEMS = successLow("LOADED_ITEMS");

    /** Failed low-priority status for loading decor */
    FailureStatus FAILED_LOAD_RENAMEABLES = failureLow("FAILED_LOAD_RENAMEABLES");
    /** Low-priority plugin loading decor status */
    SuccessStatus LOADING_RENAMEABLES = successLow("LOADING_RENAMEABLES");
    /** Low-priority plugin loaded decor status */
    SuccessStatus LOADED_RENAMEABLES = successLow("LOADED_RENAMEABLES", FAILED_LOAD_RENAMEABLES);

    /** Failed low-priority status for loading resource packs */
    FailureStatus FAILED_LOAD_RESOURCE_PACKS = failureLow("FAILED_LOAD_RESOURCE_PACKS");
    /** Low-priority plugin loading resource packs status */
    SuccessStatus LOADING_RESOURCE_PACKS = successLow("LOADING_RESOURCE_PACKS");
    /** Low-priority plugin loaded resource packs status */
    SuccessStatus LOADED_RESOURCE_PACKS = successLow("LOADED_RESOURCE_PACKS", FAILED_LOAD_RESOURCE_PACKS);

    /** Failed low-priority status for loading anomalies */
    FailureStatus FAILED_LOAD_ANOMALIES = failureLow("FAILED_LOAD_ANOMALIES");
    /** Low-priority plugin loading anomalies status */
    SuccessStatus LOADING_ANOMALIES = successLow("LOADING_ANOMALIES");
    /** Low-priority plugin loaded anomalies status */
    SuccessStatus LOADED_ANOMALIES = successLow("LOADED_ANOMALIES", FAILED_LOAD_ANOMALIES);

    /** Failed low-priority status for loading discord */
    FailureStatus FAILED_LOAD_DISCORD = failureLow("FAILED_LOAD_DISCORD");
    /** Low-priority plugin loading discord status */
    SuccessStatus LOADING_DISCORD = successLow("LOADING_DISCORD");
    /** Low-priority plugin loaded discord status */
    SuccessStatus LOADED_DISCORD = successLow("LOADED_DISCORD", FAILED_LOAD_DISCORD);
    //</editor-fold>

    /**
     * Returns the singleton instance of the plugin
     *
     * @return The singleton instance of the plugin
     */
    static WhoMine singleton() {
        return WhoMineImpl.singleton;
    }

    /**
     * Returns the plugin's configuration file
     *
     * @return The plugin's configuration file
     */
    @NotNull File getConfigFile();

    /**
     * Returns the cache of the plugin
     *
     * @return The cache of the plugin
     */
    @NotNull Cache getCache();

    /**
     * Returns the configuration of the plugin
     *
     * @return The configuration of the plugin
     */
    @NotNull Config getConfiguration();

    /**
     * Returns the status handler of the plugin
     *
     * @return The status handler of the plugin
     */
    @NotNull StatusHandler getStatusHandler();

    /**
     * Returns the listener manager of the plugin
     *
     * @return The listener manager of the plugin
     */
    @NotNull ListenerManager getListenerManager();

    /**
     * Returns the command manager of the plugin
     *
     * @return The command manager of the plugin
     */
    @NotNull CommandManager getCommandManager();

    /**
     * Returns the discord manager of the plugin
     *
     * @return The discord manager of the plugin
     */
    @NotNull DiscordManager getDiscordManager();

    /**
     * Returns an unmodifiable view of the inventory holder map
     *
     * @return An unmodifiable view of the inventory holder map
     */
    @NotNull @UnmodifiableView Map<Class<? extends AbstractInventoryHolder>, AbstractInventoryHolder> getInventoryHolderMap();

    /**
     * Gets the inventory holder of the specified class if present, otherwise an
     * empty optional will be returned
     *
     * @param clazz Class of the inventory holder
     * @return An optional containing the inventory holder if present, otherwise
     *         an empty optional
     */
    @NotNull Optional<AbstractInventoryHolder> getInventoryHolder(final @NotNull Class<? extends AbstractInventoryHolder> clazz);

    /**
     * Returns scoreboard for hiding tags
     *
     * @return Scoreboard for hiding tags
     */
    @UnknownNullability Scoreboard getScoreboardHideTags();

    /**
     * Returns team for hiding tags
     *
     * @return Team for hiding tags
     */
    @UnknownNullability Team getScoreboardHideTagsTeam();

    /**
     * Gets a {@link FileConfiguration} for this plugin, read through
     * "config.yml"
     * <br>
     * If there is a default config.yml embedded in this plugin, it will be
     * provided as a default for this Configuration.
     *
     * @return Plugin configuration
     */
    @Override
    @NotNull FileConfiguration getConfig();

    /**
     * Returns whether the plugin is fully loaded
     *
     * @return True if the plugin is fully loaded
     */
    boolean isFullyLoaded();

    /**
     * Discards any data in {@link #getConfig()} and reloads from disk.
     */
    @Override
    void reloadConfig();

    /**
     * Saves the {@link FileConfiguration} retrievable by {@link #getConfig()}
     */
    @Override
    void saveConfig();

    /**
     * Saves the raw contents of any resource embedded with a plugin's .jar file
     * assuming it can be found using {@link #getResource(String)}.
     * <br>
     * The resource is saved into the plugin's data folder using the same
     * hierarchy as the .jar file (subdirectories are preserved).
     *
     * @param resourcePath The embedded resource path to look for within the
     *                     plugin's .jar file. (No preceding slash).
     * @param replace If true, the embedded resource will overwrite the contents
     *                of an existing file.
     * @throws IllegalArgumentException if the resource path is null, empty,
     *                                  or points to a nonexistent resource.
     */
    @Override
    void saveResource(
            final @NotNull String resourcePath,
            final boolean replace
    ) throws IllegalArgumentException;

    /**
     * Saves the raw contents of the default config.yml file to the location
     * retrievable by {@link #getConfig()}.
     * <br>
     * This should fail silently if the config.yml already exists.
     */
    @Override
    void saveDefaultConfig();

    /**
     * Opens a custom inventory for the given player if the custom inventory is
     * registered to the plugin
     *
     * @param clazz  Class of the custom inventory holder
     * @param player Player to open the custom inventory
     * @see AbstractInventoryHolder
     */
    void openCustomInventory(
            final @NotNull Class<? extends AbstractInventoryHolder> clazz,
            final @NotNull Player player
    );
}
