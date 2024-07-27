package com.minersstudios.whomine.api.annotation;

import com.minersstudios.whomine.api.throwable.InvalidRegexException;
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
 * Annotation used to mark the namespace.
 * <br>
 * The namespace must match the {@link #REGEX regex} pattern.
 *
 * @see Namespace.Validator
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
        FIELD,
        LOCAL_VARIABLE,
        METHOD,
        PARAMETER
})
@org.intellij.lang.annotations.Pattern(Namespace.REGEX)
public @interface Namespace {
    /** The regex pattern that a valid namespace must match */
    @RegExp String REGEX = "[a-z0-9._-]*";

    /**
     * Validator class for the {@link Namespace} annotation to check whether the
     * namespace matches the {@link #REGEX regex}
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
         * Checks whether the namespace matches the {@link #REGEX regex}
         *
         * @param namespace The namespace
         * @return Whether the namespace matches the {@link #REGEX regex}
         */
        public static boolean matches(final @Subst("namespace") @Namespace @Nullable String namespace) {
            if (namespace == null) {
                return false;
            }

            for(int i = 0; i < namespace.length(); ++i) {
                final char character = namespace.charAt(i);

                switch (character) {
                    case '_', '-', '.' -> {}
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
         * Validates the namespace
         *
         * @param namespace The namespace
         * @throws InvalidRegexException If the namespace does not match the
         *                               {@link #REGEX regex}
         * @see #matches(String)
         */
        public static void validate(final @Subst("namespace") @Namespace @Nullable String namespace) throws InvalidRegexException {
            if (!matches(namespace)) {
                throw new InvalidRegexException("Namespace must match regex: " + REGEX);
            }
        }
    }
}
