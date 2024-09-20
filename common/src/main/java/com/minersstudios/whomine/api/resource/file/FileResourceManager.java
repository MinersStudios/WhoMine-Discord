package com.minersstudios.whomine.api.resource.file;

import com.minersstudios.whomine.api.resource.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@SuppressWarnings("unused")
public interface FileResourceManager extends ResourceManager {

    /**
     * Returns the file of the resource
     *
     * @return The file of the resource
     */
    @NotNull File getFile();
}
