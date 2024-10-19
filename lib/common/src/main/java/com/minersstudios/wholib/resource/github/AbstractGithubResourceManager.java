package com.minersstudios.wholib.resource.github;

import com.google.gson.Gson;
import com.minersstudios.wholib.resource.file.AbstractFileResourceManager;
import com.minersstudios.wholib.utility.ChatUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static java.net.HttpURLConnection.*;

public abstract class AbstractGithubResourceManager extends AbstractFileResourceManager implements GithubResourceManager {
    private static final Logger LOGGER = Logger.getLogger("ResourceManager");
    private static final Gson GSON = new Gson();
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_VALUE =  "Bearer %s";

    private final String user;
    private final String repo;
    private final AtomicReference<Tag[]> tags;
    private final String currentTag;
    private transient final String token;

    protected AbstractGithubResourceManager(
            final @NotNull File file,
            final @NotNull String user,
            final @NotNull String repo,
            final @Nullable String currentTag,
            final @Nullable String token
    ) {
        super(file);

        this.user = user;
        this.repo = repo;
        this.tags = new AtomicReference<>(null);
        this.currentTag = currentTag;
        this.token = token;
    }

    @Override
    public @NotNull String getUser() {
        return this.user;
    }

    @Override
    public @NotNull String getRepo() {
        return this.repo;
    }

    @Override
    public @NotNull CompletableFuture<Tag> getLatestTag() {
        return this.getTags()
                .thenApplyAsync(tags -> {
                    if (tags.length == 0) {
                        throw new IllegalStateException("Provided repository has no tags");
                    }

                    return tags[0];
                });
    }

    @Override
    public @NotNull CompletableFuture<Tag[]> getTags() {
        final Tag[] tags = this.getTagsNow();

        return tags != null
                && tags.length != 0
                ? CompletableFuture.completedFuture(tags)
                : this.updateTags();
    }

    @Override
    public @Nullable Tag getLatestTagNow() {
        final Tag[] tags = this.getTagsNow();

        return tags != null
               && tags.length != 0
               ? tags[0]
               : null;
    }

    @Override
    public Tag @Nullable [] getTagsNow() {
        return this.tags.get();
    }

    @Override
    public @NotNull CompletableFuture<URI> getUri() {
        return this.getLatestTag()
                .thenApplyAsync(tag -> {
                    final String tagName = tag.getName();

                    if (ChatUtils.isBlank(tagName)) {
                        throw new IllegalStateException("For some reason, the latest tag has no name");
                    }

                    return this.getFileUri(tagName);
                });
    }

    @Contract(" -> new")
    @Override
    public @NotNull URI getTagsUri() {
        return URI.create(TAGS_URL_FORMAT.formatted(this.user, this.repo));
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{' +
                "file=" + this.getFile() +
                ", user=" + this.user +
                ", repo=" + this.repo +
                ", tags=" + Arrays.toString(this.getTagsNow()) +
                '}';
    }

    @Override
    public @NotNull CompletableFuture<File> updateFile(final boolean force) {
        return force
               ? this.getUri()
                     .thenApplyAsync(
                             uri -> {
                                 final File file = this.getFile();
                                 final File directory = file.getParentFile();

                                 if (
                                         !directory.exists()
                                         && !directory.mkdirs()
                                 ) {
                                     LOGGER.warning("Failed to create a new directory: " + directory.getAbsolutePath());
                                 }

                                 try (
                                         final var client = HttpClient.newBuilder()
                                                           .followRedirects(HttpClient.Redirect.ALWAYS)
                                                           .build()
                                 ) {
                                     final int statusCode =
                                             client.send(
                                                     HttpRequest.newBuilder(uri).build(),
                                                     HttpResponse.BodyHandlers.ofFile(file.toPath())
                                             ).statusCode();

                                     if (statusCode != HTTP_OK) {
                                         throw new IllegalStateException(
                                                 "Failed to update file: " + file + " (status code: " + statusCode + ')'
                                         );
                                     }
                                 } catch (final IOException | InterruptedException e) {
                                     throw new IllegalStateException("Failed to update file: " + file, e);
                                 }

                                 return file;
                             }
                     )
               : this.getLatestTag()
                     .thenCompose(
                             tag -> tag.getName().equals(this.currentTag) && this.getFile().exists()
                                    ? CompletableFuture.completedFuture(this.getFile())
                                    : this.updateFile(true)
                     );
    }

    protected @NotNull CompletableFuture<Tag[]> updateTags() {
        return CompletableFuture.supplyAsync(() -> {
            final HttpRequest.Builder builder = HttpRequest.newBuilder(this.getTagsUri()).GET();
            final HttpResponse<String> response;

            if (ChatUtils.isNotBlank(this.token)) {
                builder.setHeader(
                        AUTHORIZATION_HEADER,
                        AUTHORIZATION_VALUE.formatted(this.token)
                );
            }

            try (final var client = HttpClient.newHttpClient()) {
                response = client.send(
                        builder.build(),
                        HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
                );
            } catch (final IOException | InterruptedException e) {
                throw new IllegalStateException("Failed to get latest tag for " + this.user + '/' + this.repo, e);
            }

            final int statusCode = response.statusCode();

            switch (statusCode) {
                case HTTP_OK -> {
                    final Tag[] newTags = GSON.fromJson(response.body(), Tag[].class);

                    this.tags.set(newTags);

                    return newTags;
                }
                case HTTP_FORBIDDEN -> throw new IllegalStateException("GitHub API rate limit exceeded");
                case HTTP_NOT_FOUND -> throw new IllegalStateException("GitHub repository not found");
                default -> throw new IllegalStateException(
                        "Failed to get latest tag for " + this.user + '/' + this.repo +
                        "(Status code: " + statusCode + ')'
                );
            }
        });
    }
}
