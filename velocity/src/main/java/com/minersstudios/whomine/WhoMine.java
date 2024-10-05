package com.minersstudios.whomine;

import com.minersstudios.whomine.api.module.MainModule;
import com.minersstudios.whomine.discord.DiscordModuleImpl;
import com.minersstudios.whomine.gui.VelocityGuiManager;
import com.minersstudios.whomine.listener.api.VelocityListenerManager;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Represents a WhoMine module
 */
public interface WhoMine extends MainModule<WhoMine> {

    /**
     * Returns the server of the module
     *
     * @return The server of the module
     */
    @NotNull ProxyServer getServer();

    /**
     * Returns the logger of the module
     *
     * @return The logger of the module
     */
    @NotNull Logger getLogger();

    /**
     * Returns the data directory of the module
     *
     * @return The data directory of the module
     */
    @NotNull Path getDataDirectory();

    @Override
    @NotNull VelocityCache getCache();

    @Override
    @NotNull VelocityConfig getConfiguration();

    /**
     * Returns the listener manager of the module
     *
     * @return The listener manager of the module
     */
    @NotNull VelocityListenerManager getListenerManager();

    /**
     * Returns the gui manager of the module
     *
     * @return The gui manager of the module
     */
    @NotNull VelocityGuiManager getGuiManager();

    /**
     * Returns the discord module
     *
     * @return The discord module
     */
    @NotNull DiscordModuleImpl getDiscordModule();
}
