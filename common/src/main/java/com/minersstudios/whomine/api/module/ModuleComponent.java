package com.minersstudios.whomine.api.module;

import org.jetbrains.annotations.NotNull;

/**
 * Module component interface
 *
 * @param <M> The module type
 */
public interface ModuleComponent<M extends Module> {

    /**
     * Returns the module that this component is associated with
     *
     * @return The module that this component is associated with
     */
    @NotNull M getModule();
}
