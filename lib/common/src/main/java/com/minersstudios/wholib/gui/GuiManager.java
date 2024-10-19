package com.minersstudios.wholib.gui;

import com.minersstudios.wholib.module.AbstractModuleComponent;
import com.minersstudios.wholib.module.Module;
import com.minersstudios.wholib.module.ModuleComponent;
import org.jetbrains.annotations.NotNull;

public class GuiManager<M extends Module> extends AbstractModuleComponent<M> implements ModuleComponent<M> {

    public GuiManager(final @NotNull M module) {
        super(module);
    }
}
