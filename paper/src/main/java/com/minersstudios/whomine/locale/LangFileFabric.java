package com.minersstudios.whomine.locale;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.minersstudios.whomine.api.locale.LanguageFile;
import com.minersstudios.whomine.api.locale.resource.GitHubTranslationResourceManager;
import com.minersstudios.whomine.api.locale.resource.TranslationResourceManager;
import com.minersstudios.whomine.api.locale.resource.URITranslationResourceManager;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.api.resource.github.Tag;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.api.utility.SharedConstants;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.kyori.adventure.translation.Translator;
import net.minecraft.locale.Language;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.*;

import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.util.concurrent.CompletableFuture.failedFuture;

/**
 * Represents a language file containing translations for a specific locale.
 * <br>
 * Language file can be created with a couple of static methods :
 * <ul>
 *     <li>{@link #fromResource(Locale, TranslationResourceManager)}</li>
 *     <li>{@link #fromStream(Locale, InputStream)}</li>
 * </ul>
 */
@Immutable
public final class LangFileFabric {
    //<editor-fold desc="Config keys">
    private static final String KEY_URL =         "url";
    private static final String KEY_TOKEN =       "token";
    private static final String KEY_USER =        "user";
    private static final String KEY_REPO =        "repo";
    private static final String KEY_TAG =         "tag";
    private static final String KEY_FOLDER_PATH = "folder-path";
    //</editor-fold>

    /**
     * Creates and loads all languages.yml from the given configuration section.
     * <br>
     * Completes exceptionally with :
     * <ul>
     *     <li>{@link IllegalStateException} - If the locale cannot be parsed
     *                                         or the URL or user and repository
     *                                         are not specified</li>
     *     <li>{@link JsonIOException}       - If the file cannot be read</li>
     *     <li>{@link JsonSyntaxException}   - If the file is not a valid JSON
     *                                         file</li>
     * </ul>
     *
     * Example of a configuration section :
     * <pre>
     * languages.yml:
     *   en_us: '' # Loads from the file
     *   en_gb:
     *     url: https:\\example.com\en_us.json
     *   uk_ua:
     *     user: ExampleUser
     *     repo: ConfigRepo
     *     tag: v1.0
     *     token: ~some-github-token~
     *     folder-path: lang
     * </pre>
     *
     * @param file     The configuration file
     * @param config   The yaml configuration
     * @param section  The configuration section
     * @param onLoaded The consumer to accept the loaded language file if the
     *                 language file was loaded successfully
     * @param onFailed The consumer to accept the key and the exception if the
     *                 language file failed to load
     * @return A map containing the language keys and their respective
     *         completable futures of language files
     * @see #fromResource(Locale, TranslationResourceManager)
     * @see #fromSection(File, YamlConfiguration, ConfigurationSection)
     */
    public static @NotNull Map<String, CompletableFuture<LanguageFile>> allFromSection(
            final @NotNull File file,
            final @NotNull YamlConfiguration config,
            final @NotNull ConfigurationSection section,
            final @Nullable Consumer<LanguageFile> onLoaded,
            final @Nullable BiConsumer<String, Throwable> onFailed
    ) {
        final var keySet = section.getKeys(false);
        final var futureMap = new Object2ObjectOpenHashMap<String, CompletableFuture<LanguageFile>>(keySet.size());

        for (final var key : keySet) {
            final ConfigurationSection langSection = section.getConfigurationSection(key);
            CompletableFuture<LanguageFile> future;

            if (langSection == null) {
                final Locale locale = Translator.parseLocale(key);

                if (locale == null) {
                    MSLogger.warning("Failed to parse locale: " + key);

                    continue;
                }

                try {
                    future = CompletableFuture.completedFuture(
                            fromResource(
                                    locale,
                                    TranslationResourceManager.file(getFile(key))
                            )
                    );
                } catch (final Throwable e) {
                    future = failedFuture(e);
                }
            } else {
                future = fromSection(file, config, langSection);
            }

            futureMap.put(
                    key,
                    future
                    .thenApply(
                            languageFile -> {
                                if (onLoaded != null) {
                                    onLoaded.accept(languageFile);
                                }

                                return languageFile;
                            }
                    )
                    .exceptionallyCompose(
                            throwable -> {
                                if (onFailed != null) {
                                    onFailed.accept(key, throwable);
                                }

                                return failedFuture(throwable);
                            }
                    )
            );
        }

        return futureMap;
    }

    /**
     * Creates and loads a new language file from the given locale and
     * configuration section.
     * <br>
     * Completes exceptionally with :
     * <ul>
     *     <li>{@link IllegalStateException} - If the locale cannot be parsed,
     *                                         or the URL or user and repository
     *                                         are not specified</li>
     *     <li>{@link JsonIOException}       - If the file cannot be read</li>
     *     <li>{@link JsonSyntaxException}   - If the file is not a valid JSON
     *                                         file</li>
     * </ul>
     *
     * Configuration sections example :
     * <pre>
     * en_us:
     *   url: https:\\example.com\en_us.json
     * uk_ua:
     *   user: ExampleUser
     *   repo: ConfigRepo
     *   tag: v1.0
     *   token: ~some-github-token~
     *   folder-path: lang
     * </pre>
     *
     * @param file    The configuration file
     * @param config  The yaml configuration
     * @param section The configuration section
     * @return A future containing the language file
     * @see #fromURI(Locale, URITranslationResourceManager)
     * @see #fromGitHub(Locale, GitHubTranslationResourceManager)
     */
    public static @NotNull CompletableFuture<LanguageFile> fromSection(
            final @NotNull File file,
            final @NotNull YamlConfiguration config,
            final @NotNull ConfigurationSection section
    ) {
        final String localeCode = section.getName();
        final Locale locale = Translator.parseLocale(localeCode);

        if (locale == null) {
            return failedFuture(new IllegalStateException("Failed to parse locale: " + localeCode));
        }

        final String url = section.getString(KEY_URL);

        if (ChatUtils.isNotBlank(url)) {
            return fromURI(
                    locale,
                    TranslationResourceManager.url(url)
            );
        }

        final String user = section.getString(KEY_USER);
        final String repo = section.getString(KEY_REPO);

        if (
                ChatUtils.isBlank(user)
                || ChatUtils.isBlank(repo)
        ) {
            return failedFuture(new IllegalStateException("Specify the URL or user and repository for : " + section));
        }

        final GitHubTranslationResourceManager resourceManager =
                TranslationResourceManager.github(
                        getFile(localeCode),
                        user,
                        repo,
                        section.getString(KEY_TAG),
                        section.getString(KEY_TOKEN),
                        section.getString(KEY_FOLDER_PATH)
                );

        return fromGitHub(locale, resourceManager)
                .thenApply(languageFile -> {
                    final Tag latestTag = resourceManager.getLatestTagNow();

                    if (latestTag != null) {
                        section.set(KEY_TAG, latestTag.getName());

                        synchronized (LangFileFabric.class) {
                            try {
                                config.save(file);
                            } catch (final Throwable e) {
                                MSLogger.warning(
                                        "Failed to save the configuration file: " + file,
                                        e
                                );
                            }
                        }
                    }

                    return languageFile;
                });
    }

    /**
     * Creates and loads a new language file from the given locale and resource
     * manager.
     * <br>
     * Completes exceptionally with :
     * <ul>
     *     <li>{@link JsonIOException}     - If the file cannot be read</li>
     *     <li>{@link JsonSyntaxException} - If the file is not a valid JSON
     *                                       file</li>
     * </ul>
     *
     * @param locale          The locale of the language file
     * @param resourceManager The resource manager of the language file
     * @return A future containing the language file
     * @see #fromResource(Locale, TranslationResourceManager)
     */
    public static @NotNull CompletableFuture<LanguageFile> fromGitHub(
            final @NotNull Locale locale,
            final @NotNull GitHubTranslationResourceManager resourceManager
    ) {
        return resourceManager
                .updateFile(false)
                .thenApplyAsync(ignored -> fromResource(locale, resourceManager));
    }

    /**
     * Creates and loads a new language file from the given locale and uri
     * resource manager.
     * <br>
     * Completes exceptionally with :
     * <ul>
     *     <li>{@link JsonIOException}     - If the file cannot be read</li>
     *     <li>{@link JsonSyntaxException} - If the file is not a valid JSON
     *                                       file</li>
     * </ul>
     *
     * @param locale          The locale of the language file
     * @param resourceManager The resource manager of the language file
     * @return A future containing the language file
     * @see #fromResource(Locale, TranslationResourceManager)
     */
    public static @NotNull CompletableFuture<LanguageFile> fromURI(
            final @NotNull Locale locale,
            final @NotNull URITranslationResourceManager resourceManager
    ) {
        return CompletableFuture.supplyAsync(() -> fromResource(locale, resourceManager));
    }

    /**
     * Creates and loads a new language file from the given locale and resource
     * manager
     *
     * @param locale          The locale of the language file
     * @param resourceManager The resource manager of the language file
     * @return A future containing the language file
     * @throws JsonIOException     If the file cannot be read
     * @throws JsonSyntaxException If the file is not a valid JSON file
     * @see #fromStream(Locale, InputStream)
     */
    public static @NotNull LanguageFile fromResource(
            final @NotNull Locale locale,
            final @NotNull TranslationResourceManager resourceManager
    ) throws JsonIOException, JsonSyntaxException {
        try (final var in = resourceManager.openStream()) {
            return fromStream(locale, in);
        } catch (final IOException e) {
            throw new JsonIOException(
                    "Failed to read resource : " + resourceManager,
                    e
            );
        }
    }

    /**
     * Creates and loads a new language file from the given locale and input
     * stream
     *
     * @param locale      The locale of the language file
     * @param inputStream The input stream of the language file
     * @return The language file
     * @throws JsonIOException     If the file cannot be read
     * @throws JsonSyntaxException If the file is not a valid JSON file
     */
    public static @NotNull LanguageFile fromStream(
            final @NotNull Locale locale,
            final @NotNull InputStream inputStream
    ) throws JsonIOException, JsonSyntaxException {
        final LanguageFile file = new LanguageFile(locale);

        try (inputStream) {
            Language.loadFromJson(
                    inputStream,
                    file.getTranslationMap()::put
            );
        } catch (final IOException e) {
            throw new JsonIOException(e);
        }

        return file;
    }

    private static @NotNull File getFile(final @NotNull String path) {
        return new File(SharedConstants.LANGUAGE_FOLDER_PATH, path + ".json");
    }
}
