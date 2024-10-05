package com.minersstudios.whomine;

import com.minersstudios.whomine.api.module.MainModule;
import com.minersstudios.whomine.discord.DiscordManager;
import com.minersstudios.whomine.gui.PaperGuiManager;
import com.minersstudios.whomine.listener.api.PaperListenerManager;
import com.minersstudios.whomine.scheduler.TaskExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents a WhoMine module
 */
public interface WhoMine extends MainModule<WhoMine>, Plugin, TaskExecutor {

    static @UnknownNullability WhoMine singleton() {
        return WhoMineImpl.singleton;
    }

    @Override
    @NotNull PaperCache getCache();

    @Override
    @NotNull PaperConfig getConfiguration();

    /**
     * Returns the listener manager of the module
     *
     * @return The listener manager of the module
     */
    @NotNull PaperListenerManager getListenerManager();

    /**
     * Returns the gui manager of the module
     *
     * @return The gui manager of the module
     */
    @NotNull PaperGuiManager getGuiManager();

    /**
     * Returns the discord module
     *
     * @return The discord module
     */
    @NotNull DiscordManager getDiscordModule();

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
}
