package com.minersstudios.wholib.paper;

import com.minersstudios.wholib.module.MainModule;
import com.minersstudios.wholib.paper.discord.DiscordManager;
import com.minersstudios.wholib.paper.gui.PaperGuiManager;
import com.minersstudios.wholib.paper.listener.PaperListenerManager;
import com.minersstudios.wholib.paper.scheduler.TaskExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a WhoMine module
 */
public interface WhoMine extends MainModule<WhoMine>, Plugin, TaskExecutor {
    Instance SINGLETON = new Instance();

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

    static @NotNull Optional<WhoMine> singleton() {
        return SINGLETON.get();
    }

    final class Instance {
        private volatile WhoMine instance;

        private Instance() {}

        public @NotNull Optional<WhoMine> get() {
            return Optional.ofNullable(this.instance);
        }

        public void set(final @NotNull WhoMine instance) {
            this.instance = instance;
        }
    }
}
