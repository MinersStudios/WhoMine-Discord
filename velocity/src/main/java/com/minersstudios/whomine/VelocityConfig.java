package com.minersstudios.whomine;

import com.minersstudios.whomine.api.module.AbstractModuleComponent;
import com.minersstudios.whomine.api.module.components.Configuration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class VelocityConfig extends AbstractModuleComponent<WhoMine> implements Configuration<WhoMine> {

    /**
     * Module component constructor
     *
     * @param module The module instance
     */
    VelocityConfig(final @NotNull WhoMine module) {
        super(module);
    }

    @Override
    public @NotNull File getFile() {
        return null;
    }
}
