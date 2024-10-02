package com.minersstudios.whomine.api.module.components;

import com.minersstudios.whomine.api.module.Module;
import com.minersstudios.whomine.api.module.ModuleComponent;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface Configuration<M extends Module> extends ModuleComponent<M> {

    @NotNull File getFile();
}
