package com.minersstudios.wholib.module.components;

import com.minersstudios.wholib.module.Module;
import com.minersstudios.wholib.module.ModuleComponent;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface Configuration<M extends Module> extends ModuleComponent<M> {

    @NotNull File getFile();
}
