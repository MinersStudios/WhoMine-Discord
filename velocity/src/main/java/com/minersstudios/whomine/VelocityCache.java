package com.minersstudios.whomine;

import com.minersstudios.whomine.api.module.AbstractModuleComponent;
import com.minersstudios.whomine.api.module.components.Cache;
import org.jetbrains.annotations.NotNull;

public final class VelocityCache extends AbstractModuleComponent<WhoMine> implements Cache<WhoMine> {

    /**
     * Module component constructor
     *
     * @param module The module instance
     */
    VelocityCache(final @NotNull WhoMine module) {
        super(module);
    }
}
