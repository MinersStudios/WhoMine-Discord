package com.minersstudios.whomine.api.locale.resource;

import com.minersstudios.whomine.api.resource.uri.AbstractURIResourceManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

public final class URITranslationResourceManager extends AbstractURIResourceManager implements TranslationResourceManager {

    URITranslationResourceManager(final @NotNull URI uri) {
        super(uri);
    }
}
