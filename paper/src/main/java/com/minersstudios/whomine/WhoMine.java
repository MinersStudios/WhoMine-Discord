package com.minersstudios.whomine;

import com.minersstudios.whomine.api.gui.GuiManager;
import com.minersstudios.whomine.api.module.MainModule;
import com.minersstudios.whomine.discord.DiscordManager;
import com.minersstudios.whomine.inventory.holder.AbstractInventoryHolder;
import com.minersstudios.whomine.listener.api.PaperListenerManager;
import com.minersstudios.whomine.scheduler.TaskExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;
import java.util.Optional;

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
    @NotNull GuiManager<WhoMine> getGuiManager();

    /**
     * Returns the discord module
     *
     * @return The discord module
     */
    @NotNull DiscordManager getDiscordModule();


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
