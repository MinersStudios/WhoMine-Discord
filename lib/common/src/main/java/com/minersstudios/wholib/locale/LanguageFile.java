package com.minersstudios.wholib.locale;

import com.google.common.base.Joiner;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.*;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a language file containing translations for a specific locale
 */
@SuppressWarnings("unused")
@Immutable
public class LanguageFile {

    private final Locale locale;
    private final Map<String, String> translationMap;

    /**
     * Constructs a new language file with the given locale
     *
     * @param locale The locale of the language file
     */
    public LanguageFile(
            final @NotNull Locale locale,
            final @NotNull Map<String, String> translationMap
    ) {
        this.locale = locale;
        this.translationMap = new Object2ObjectOpenHashMap<>(translationMap);
    }

    /**
     * Returns the locale of this language file
     *
     * @return The locale of this language file
     */
    public @NotNull Locale getLocale() {
        return this.locale;
    }

    /**
     * Returns the unmodifiable map of translations in this language file
     *
     * @return The unmodifiable map of translations in this language file
     */
    public @NotNull @Unmodifiable Map<String, String> getTranslationMap() {
        return Collections.unmodifiableMap(this.translationMap);
    }

    /**
     * Gets the translation for the given path
     *
     * @param path The path to get the translation for
     * @return The translation for the given path, or null if it doesn't exist
     */
    public @Nullable String get(final @NotNull String path) {
        return this.getOrDefault(path, null);
    }

    /**
     * Gets the translation for the given path, or the fallback if it doesn't
     * exist
     *
     * @param path     The path to get the translation for
     * @param fallback The fallback to return if the translation doesn't exist
     * @return The translation for the given path, or the fallback if it doesn't
     *         exist
     */
    public @UnknownNullability String getOrDefault(
            final @NotNull String path,
            final @Nullable String fallback
    ) {
        return this.translationMap.getOrDefault(path, fallback);
    }

    /**
     * Returns the number of translations in this language file
     *
     * @return The number of translations in this language file
     */
    public int size() {
        return this.translationMap.size();
    }

    /**
     * Returns a hash code based on the locale and translations
     *
     * @return A hash code based on the locale and translations
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.locale.hashCode();
        result = prime * result + this.translationMap.hashCode();

        return result;
    }

    /**
     * Compares the specified object with this language file for equality
     *
     * @param obj The object to compare
     * @return True if the object is a language file and has the same locale and
     *         translations
     */
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof LanguageFile that
                        && this.getLocale().equals(that.getLocale())
                        && this.getTranslationMap().equals(that.getTranslationMap())
                );
    }

    /**
     * Checks whether the given path exists in this language file
     *
     * @param path The path to check for
     * @return Whether the given path exists in this language file
     */
    public boolean containsPath(final @NotNull String path) {
        return this.translationMap.containsKey(path);
    }

    /**
     * Returns whether this language file contains the given translation
     *
     * @param translation The translation to check for
     * @return True if this language file contains the given translation
     */
    public boolean containsTranslation(final @NotNull String translation) {
        return this.translationMap.containsValue(translation);
    }

    /**
     * Returns whether this language file contains no translations
     *
     * @return True if this language file contains no translations
     */
    public boolean isEmpty() {
        return this.translationMap.isEmpty();
    }

    /**
     * Returns a string representation of this language file
     *
     * @return A string representation of this language file containing the
     *         translations
     */
    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() +
                "{locale=" + this.locale +
                ", translations=[" +
                Joiner.on(", ")
                      .withKeyValueSeparator("=")
                      .join(this.translationMap) +
                "]}";
    }
}
