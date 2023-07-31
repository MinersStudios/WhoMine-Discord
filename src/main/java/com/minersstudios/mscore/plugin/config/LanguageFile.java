package com.minersstudios.mscore.plugin.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.minersstudios.mscore.logger.MSLogger;
import com.minersstudios.mscore.plugin.GlobalConfig;
import com.minersstudios.mscore.plugin.MSPlugin;
import com.minersstudios.mscore.util.ChatUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.translatable;

/**
 * Language file loader
 * <br>
 * Loads the language file from the language repository and adds it to {@link GlobalTranslator}.
 * All downloaded language files are stored in the "/config/minersstudios/language" folder
 */
public final class LanguageFile {
    private final String folderLink;
    private final String code;
    private final JsonObject translations;
    private File file;

    private static TranslationRegistry registry = TranslationRegistry.create(Key.key("ms"));
    private static final Field TRANSLATIONS_FIELD;

    static {
        try {
            var registryImplClass = Class.forName("net.kyori.adventure.translation.TranslationRegistryImpl");
            TRANSLATIONS_FIELD = registryImplClass.getDeclaredField("translations");

            TRANSLATIONS_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize LanguageFile", e);
        }
    }

    private LanguageFile(
            @NotNull String folderLink,
            @NotNull String code
    ) {
        this.folderLink = folderLink;
        this.code = code;
        this.file = this.loadFile();
        this.translations = this.loadTranslations();
    }

    /**
     * @return The folder link of the language repository
     */
    public @NotNull String getFolderLink() {
        return this.folderLink;
    }

    /**
     * @return The language code of the language file
     */
    public @NotNull String getCode() {
        return this.code;
    }

    /**
     * @return The language file
     */
    public @NotNull File getFile() {
        return this.file;
    }

    /**
     * @return The translation registry of the language file
     */
    public @NotNull TranslationRegistry getRegistry() {
        return registry;
    }

    /**
     * Loads the translations from the language file to
     * {@link GlobalTranslator}. If the language file does
     * not exist, it will be downloaded from the language
     * repository.
     *
     * @param folderLink The folder link of the language repository
     * @param code Language code of the language file
     */
    public static void loadLanguage(
            @NotNull String folderLink,
            @NotNull String code
    ) {
        long time = System.currentTimeMillis();
        LanguageFile languageFile = new LanguageFile(folderLink, code);

        Locale locale = Locale.US;
        languageFile.translations.entrySet().forEach(
                entry -> {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();

                    if (registry.contains(key)) {
                        registry.unregister(key);
                    }

                    registry.register(key, locale, new MessageFormat(value));
                }
        );
        GlobalTranslator.translator().addSource(registry);

        MSLogger.fine("Loaded language file: " + languageFile.file.getName() + " in " + (System.currentTimeMillis() - time) + "ms");
    }

    /**
     * Unloads all languages registered in {@link #registry} from {@link GlobalTranslator}
     */
    public static void unloadLanguage() {
        GlobalTranslator.translator().removeSource(registry);
        registry = TranslationRegistry.create(Key.key("ms"));
    }

    /**
     * Reloads language file
     *
     * @see #unloadLanguage()
     * @see #loadLanguage(String, String)
     */
    public static void reloadLanguage() {
        GlobalConfig config = MSPlugin.getGlobalConfig();

        unloadLanguage();
        loadLanguage(config.languageFolderLink, config.languageCode);
    }

    /**
     * Renders the translation from {@link #registry} as {@link TranslatableComponent}.
     * <br>
     * <b>NOTE:</b> Use only for custom translations loaded from the language file.
     * Usually used for item names and lore, because they are renders it's without fallback
     *
     * @param key Translation key
     * @return TranslatableComponent with translation from {@link #registry} or key if translation is not found
     * @see #renderTranslation(String)
     */
    public static @NotNull TranslatableComponent renderTranslationComponent(@NotNull String key) {
        return translatable(key, renderTranslation(key));
    }

    /**
     * Renders the translation from {@link GlobalTranslator} as {@link Component}
     *
     * @param translatable TranslatableComponent to render
     * @return Translated component or key if translation is not found
     */
    public static @NotNull Component renderTranslationComponent(@NotNull TranslatableComponent translatable) {
        return GlobalTranslator.render(translatable, Locale.US);
    }

    /**
     * Renders the translation from {@link #registry} as {@link String}
     * <br>
     * <b>NOTE:</b> Use only for custom translations loaded from the language file
     *
     * @param key Translation key
     * @return Translated string or key if translation is not found
     */
    public static @NotNull String renderTranslation(@NotNull String key) {
        MessageFormat format = registry.translate(key, Locale.US);
        return format == null ? key : format.toPattern();
    }

    /**
     * Renders the translation from {@link GlobalTranslator} as {@link String}
     *
     * @param translatable TranslatableComponent to render
     * @return Translated string or key if translation is not found
     */
    public static @NotNull String renderTranslation(@NotNull TranslatableComponent translatable) {
        return ChatUtils.serializePlainComponent(renderTranslationComponent(translatable));
    }

    /**
     * @return True if language is loaded
     *         (if {@link #registry} is not empty)
     */
    public static boolean isLoaded() {
        try {
            return !((Map<?, ?>) TRANSLATIONS_FIELD.get(registry)).isEmpty();
        } catch (IllegalAccessException e) {
            MSLogger.log(Level.SEVERE, "Failed to check if language is loaded", e);
            return false;
        }
    }

    /**
     * Loads language file from the language repository.
     * File will be downloaded to "/config/minersstudios/language" folder
     *
     * @return Language file
     */
    private @NotNull File loadFile() {
        File langFolder = new File("config/minersstudios/language");

        if (!langFolder.exists() && !langFolder.mkdirs()) {
            throw new RuntimeException("Failed to create language folder");
        }

        File langFile = new File(langFolder, this.code + ".json");

        if (!langFile.exists()) {
            String link =
                    this.folderLink
                    + (
                            this.folderLink.endsWith("/")
                            ? ""
                            : '/'
                    )
                    + this.code + ".json";

            try (
                    var in = new URL(link).openStream();
                    var out = new FileOutputStream(langFile)
            ) {
                in.transferTo(out);
            } catch (IOException e) {
                MSLogger.log(Level.SEVERE, "Failed to download language file: " + link, e);
            }
        }

        return langFile;
    }

    /**
     * Loads language file as JsonObject from file path specified in {@link #file}
     *
     * @return JsonObject of language file
     * @throws JsonSyntaxException If language file is corrupted
     */
    private @NotNull JsonObject loadTranslations() throws JsonSyntaxException {
        try {
            Path path = Path.of(this.file.getAbsolutePath());
            String content = Files.readString(path);
            JsonElement element = JsonParser.parseString(content);

            return element.getAsJsonObject();
        } catch (IOException | JsonSyntaxException e) {
            MSLogger.severe("Failed to load corrupted language file: " + this.code + ".json");
            MSLogger.severe("Creating backup file and trying to load language file again");

            this.createBackupFile();
            this.file = this.loadFile();

            return this.loadTranslations();
        }
    }

    /**
     * Creates a backup file of the corrupted language file.
     * The backup file will be named as the original file with ".OLD" appended to the end.
     * If the backup file already exists, it will be replaced
     *
     * @throws RuntimeException If failed to create backup file
     */
    private void createBackupFile() throws RuntimeException {
        String backupFileName = this.file.getName() + ".OLD";
        File backupFile = new File(this.file.getParent(), backupFileName);
        Path filePath = Path.of(this.file.getAbsolutePath());
        Path backupFilePath = Path.of(backupFile.getAbsolutePath());

        try {
            Files.move(filePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create" + backupFileName + " backup file", e);
        }
    }
}
