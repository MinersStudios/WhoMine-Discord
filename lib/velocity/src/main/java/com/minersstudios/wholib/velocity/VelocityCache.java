package com.minersstudios.wholib.velocity;

import com.minersstudios.wholib.module.AbstractModuleComponent;
import com.minersstudios.wholib.module.components.Cache;
import org.jetbrains.annotations.NotNull;

public final class VelocityCache extends AbstractModuleComponent<WhoMine> implements Cache<WhoMine> {

    /**
     * Module component constructor
     *
     * @param module The module instance
     */
    public VelocityCache(final @NotNull WhoMine module) {
        super(module);
    }
}
