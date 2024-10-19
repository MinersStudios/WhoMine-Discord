package com.minersstudios.wholib.velocity.discord;

import com.minersstudios.wholib.discord.DiscordModule;
import com.minersstudios.wholib.status.StatusHandler;
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
