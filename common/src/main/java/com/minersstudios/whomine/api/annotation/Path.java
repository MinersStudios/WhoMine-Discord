package com.minersstudios.whomine.api.annotation;

import com.minersstudios.whomine.api.throwable.InvalidResourceException;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Annotation used to mark the path.
 * <p>
 * The path must match the {@link #REGEX regex} pattern.
 * <p>
 * Example of usage:
 * <pre>
 *     {@code @Path String path = "path";}
 * </pre>
 *
 * @see Path.Validator
 * @see ResourcePath
 * @see Resource
 */
@SuppressWarnings("unused")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
        FIELD,
        LOCAL_VARIABLE,
        METHOD,
        PARAMETER
})
@Pattern(Path.REGEX)
public @interface Path {
    /** The regex pattern that a valid path must match */
    @RegExp String REGEX = "[a-z0-9/._-]*";

    /**
     * Validator class for the {@link Path} annotation to check whether the
     * path matches the {@link #REGEX regex}
     *
     * @see #matches(String)
     * @see #validate(String)
     */
    final class Validator {

        @Contract(" -> fail")
        private Validator() throws AssertionError {
            throw new AssertionError("Utility class");
        }

        /**
         * Checks whether the path matches the {@link #REGEX regex}
         *
         * @param path The path
         * @return Whether the path matches the {@link #REGEX regex}
         */
        @Contract("null -> true")
        public static boolean matches(final @Subst("path") @Path @Nullable String path) {
            if (path == null) {
                return true;
            }

            for(int i = 0; i < path.length(); ++i) {
                final char character = path.charAt(i);

                switch (character) {
                    case '_', '-', '.', '/' -> {}
                    default -> {
                        if (character < 'a' || character > 'z') {
                            if (character < '0' || character > '9') {
                                return false;
                            }
                        }
                    }
                }
            }

            return true;
        }

        /**
         * Validates the path
         *
         * @param path The path
         * @throws InvalidResourceException If the path does not match the
         *                                  {@link #REGEX regex}
         * @see #matches(String)
         */
        public static void validate(final @Subst("path") @Path @Nullable String path) throws InvalidResourceException {
            if (!matches(path)) {
                throw new InvalidResourceException("Path must match regex: " + REGEX);
            }
        }
    }
}
