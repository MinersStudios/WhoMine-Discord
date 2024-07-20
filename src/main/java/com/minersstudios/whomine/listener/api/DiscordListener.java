package com.minersstudios.whomine.listener.api;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.plugin.PluginComponent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordListener extends ListenerAdapter implements PluginComponent {
    private final WhoMine plugin;

    /**
     * Discord listener constructor
     *
     * @param plugin The plugin instance
     */
    protected DiscordListener(final @NotNull WhoMine plugin) {
        this.plugin = plugin;
    }

    @Override
    public final @NotNull WhoMine getPlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + "{plugin=" + this.plugin + '}';
    }

    @Override
    public void register() throws IllegalStateException {
        this.getPlugin().getListenerManager().registerDiscord(this);
        this.onRegister();
    }
}
