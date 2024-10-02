package com.minersstudios.whomine.api.gui;

import com.minersstudios.whomine.api.module.AbstractModuleComponent;
import com.minersstudios.whomine.api.module.Module;
import com.minersstudios.whomine.api.module.ModuleComponent;
import org.jetbrains.annotations.NotNull;

public class GuiManager<M extends Module> extends AbstractModuleComponent<M> implements ModuleComponent<M> {

    public GuiManager(final @NotNull M module) {
        super(module);
    }
}
