package com.minersstudios.whomine.plugin;

import com.minersstudios.whomine.WhoMine;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPluginComponent implements PluginComponent {
    private final WhoMine plugin;

    /**
     * Plugin component constructor
     *
     * @param plugin The plugin instance
     */
    protected AbstractPluginComponent(final @NotNull WhoMine plugin) {
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
}
