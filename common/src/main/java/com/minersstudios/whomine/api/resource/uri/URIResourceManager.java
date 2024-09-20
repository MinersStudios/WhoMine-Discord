package com.minersstudios.whomine.api.resource.uri;

import com.minersstudios.whomine.api.resource.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface URIResourceManager extends ResourceManager {

    /**
     * Returns the URI of the resource
     *
     * @return The future containing the URI of the resource
     */
    @NotNull CompletableFuture<URI> getUri();
}
