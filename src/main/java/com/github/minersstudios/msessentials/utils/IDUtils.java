package com.github.minersstudios.msessentials.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IDUtils {
    public static final @NotNull String ID_REGEX = "-?\\d+";

    private IDUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parses ID from the string
     *
     * @param stringId ID string
     * @return int ID from string or -1 if
     */
    public static int parseID(@NotNull String stringId) {
        try {
            return Integer.parseInt(stringId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean matchesIDRegex(@Nullable String string) {
        return string != null && string.matches(ID_REGEX);
    }
}
