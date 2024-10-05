package com.minersstudios.whomine;

import com.minersstudios.whomine.api.annotation.Resource;
import com.minersstudios.whomine.api.status.StatusHandler;
import com.minersstudios.whomine.discord.DiscordModuleImpl;
import com.minersstudios.whomine.gui.VelocityGuiManager;
import com.minersstudios.whomine.listener.api.VelocityListenerManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.logging.Logger;

import static com.minersstudios.whomine.api.utility.ProjectInfo.*;

@Plugin(
        id = Resource.WHOMINE,
        name = NAME,
        version = VERSION,
        description = DESCRIPTION,
        url = WEBSITE,
        authors = { AUTHOR }
)
public final class WhoMineImpl implements WhoMine {

    private final ProxyServer server;
    private final Logger logger;
    private final @DataDirectory Path dataDirectory;

    private final StatusHandler statusHandler;
    private final VelocityCache cache;
    private final VelocityConfig config;
    private final VelocityListenerManager listenerManager;
    private final VelocityGuiManager guiManager;
    private final DiscordModuleImpl discordModule;

    @Inject
    public WhoMineImpl(
            final @NotNull ProxyServer server,
            final @NotNull Logger logger,
            final @NotNull @DataDirectory Path dataDirectory
    ) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        this.statusHandler = new StatusHandler();
        this.cache = new VelocityCache(this);
        this.config = new VelocityConfig(this);
        this.listenerManager = new VelocityListenerManager(this);
        this.guiManager = new VelocityGuiManager(this);
        this.discordModule = new DiscordModuleImpl();
    }

    @Override
    public @NotNull ProxyServer getServer() {
        return this.server;
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.logger;
    }

    @Override
    public @NotNull Path getDataDirectory() {
        return this.dataDirectory;
    }

    @Override
    public @NotNull StatusHandler getStatusHandler() {
        return this.statusHandler;
    }

    @Override
    public @NotNull VelocityCache getCache() {
        return this.cache;
    }

    @Override
    public @NotNull VelocityConfig getConfiguration() {
        return this.config;
    }

    @Override
    public @NotNull VelocityListenerManager getListenerManager() {
        return this.listenerManager;
    }

    @Override
    public @NotNull VelocityGuiManager getGuiManager() {
        return this.guiManager;
    }

    @Override
    public @NotNull DiscordModuleImpl getDiscordModule() {
        return this.discordModule;
    }

    @Override
    public boolean isFullyLoaded() {
        return true;
    }

    @Subscribe
    public void onProxyInitialize(final @NotNull ProxyInitializeEvent event) {
        this.listenerManager.bootstrap();
    }
}
