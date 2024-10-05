package com.minersstudios.whomine.discord;

import com.minersstudios.whomine.api.discord.DiscordModule;
import com.minersstudios.whomine.api.status.StatusHandler;
import org.jetbrains.annotations.NotNull;

public class DiscordModuleImpl implements DiscordModule<DiscordModuleImpl> {

    private final StatusHandler statusHandler;

    public DiscordModuleImpl() {
        this.statusHandler = new StatusHandler();
    }

    @Override
    public @NotNull StatusHandler getStatusHandler() {
        return this.statusHandler;
    }
}
