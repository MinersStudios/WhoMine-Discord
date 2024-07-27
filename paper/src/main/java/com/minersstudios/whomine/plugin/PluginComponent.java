package com.minersstudios.whomine.plugin;

import com.minersstudios.whomine.WhoMine;
import org.jetbrains.annotations.NotNull;

/**
 * Plugin component interface
 */
public interface PluginComponent {

    /**
     * Returns the plugin that this component is associated with
     *
     * @return The plugin that this component is associated with
     */
    @NotNull WhoMine getPlugin();

    /**
     * Returns a string representation of this component
     *
     * @return A string representation of this component
     */
    @Override
    @NotNull String toString();

    /**
     * Registers the component with the {@link #getPlugin() plugin}
     *
     * @throws IllegalStateException If the component is already registered
     * @see #onRegister()
     */
    void register() throws IllegalStateException;

    /**
     * Called when the component is registered
     */
    default void onRegister() {
        // Some registration magic
    }
}
