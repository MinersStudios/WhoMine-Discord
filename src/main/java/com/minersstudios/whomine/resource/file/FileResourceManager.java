package com.minersstudios.whomine.resource.file;

import com.minersstudios.whomine.resource.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface FileResourceManager extends ResourceManager {

    /**
     * Returns the file of the resource
     *
     * @return The file of the resource
     */
    @NotNull File getFile();
}
