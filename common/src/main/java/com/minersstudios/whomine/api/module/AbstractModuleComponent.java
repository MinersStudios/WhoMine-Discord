package com.minersstudios.whomine.api.module;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractModuleComponent<M extends Module> implements ModuleComponent<M> {

    private final M module;

    /**
     * Module component constructor
     *
     * @param module The module instance
     */
    protected AbstractModuleComponent(final @NotNull M module) {
        this.module = module;
    }

    @Override
    public final @NotNull M getModule() {
        return this.module;
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + "{module=" + this.module + "}";
    }
}
