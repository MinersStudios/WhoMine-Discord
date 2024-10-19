package com.minersstudios.wholib.velocity;

import com.minersstudios.wholib.module.AbstractModuleComponent;
import com.minersstudios.wholib.module.components.Configuration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class VelocityConfig extends AbstractModuleComponent<WhoMine> implements Configuration<WhoMine> {

    /**
     * Module component constructor
     *
     * @param module The module instance
     */
    public VelocityConfig(final @NotNull WhoMine module) {
        super(module);
    }

    @Override
    public @NotNull File getFile() {
        return null;
    }
}
