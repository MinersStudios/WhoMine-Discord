package com.minersstudios.whomine.locale.resource;

import com.minersstudios.whomine.resource.file.AbstractFileResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class FileTranslationResourceManager extends AbstractFileResourceManager implements TranslationResourceManager {

    FileTranslationResourceManager(final @NotNull File file) {
        super(file);
    }
}
