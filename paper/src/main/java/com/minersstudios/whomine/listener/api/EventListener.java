package com.minersstudios.whomine.listener.api;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.plugin.AbstractPluginComponent;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class EventListener extends AbstractPluginComponent implements Listener {

    /**
     * Event listener constructor
     *
     * @param plugin The plugin instance
     */
    protected EventListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @Override
    public void register() throws IllegalStateException {
        this.getPlugin().getListenerManager().registerEvent(this);
        this.onRegister();
    }
}
