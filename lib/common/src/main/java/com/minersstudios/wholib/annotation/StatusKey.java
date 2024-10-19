package com.minersstudios.wholib.annotation;

import com.minersstudios.wholib.throwable.InvalidRegexException;
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
 * Annotation used to mark the key of the status.
 * <p>
 * The status key must match the {@link #REGEX regex} pattern.
 * <p>
 * Example of usage:
 * <pre>
 *     {@code @StatusKey String statusKey = "STATUS_KEY";}
 * </pre>
 *
 * @see StatusKey.Validator
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
@Pattern(StatusKey.REGEX)
public @interface StatusKey {
    /** The regex pattern that a valid status key must match */
    @RegExp String REGEX = "^[A-Z][A-Z0-9_]*$";

    /**
     * Validator class for the {@link StatusKey} annotation to check whether the
     * key of the status matches the {@link #REGEX regex}
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
         * Checks whether the key of the status matches the {@link #REGEX regex}
         *
         * @param key Key of the status
         * @return Whether the key matches the {@link #REGEX regex}
         */
        @Contract("null -> false")
        public static boolean matches(final @Subst("STATUS_KEY") @StatusKey @Nullable String key) {
            if (key == null) {
                return false;
            }

            final int length = key.length();

            if (length == 0) {
                return false;
            }

            final char first = key.charAt(0);

            if (first < 'A' || first > 'Z') {
                return false;
            }

            for (int i = 1; i < length; ++i) {
                final char character = key.charAt(i);

                if (
                        (character < 'A' || character > 'Z')
                        && (character < '0' || character > '9')
                        && character != '_'
                ) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Validates the key of the status
         *
         * @param key Key of the status
         * @throws InvalidRegexException If the key does not match the
         *                               {@link #REGEX regex}
         * @see #matches(String)
         */
        @Contract("null -> fail")
        public static void validate(final @Subst("STATUS_KEY") @StatusKey @Nullable String key) throws InvalidRegexException {
            if (!matches(key)) {
                throw new InvalidRegexException("Status key must match regex: " + REGEX);
            }
        }
    }
}
