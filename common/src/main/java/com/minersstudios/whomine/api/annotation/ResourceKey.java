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
 * Annotation used to mark the {@code namespaced-key}.
 * <br>
 * The namespaced-key must match the {@link #REGEX regex} pattern.
 *
 * @see ResourceKey.Validator
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
        FIELD,
        LOCAL_VARIABLE,
        METHOD,
        PARAMETER
})
@org.intellij.lang.annotations.Pattern(ResourceKey.REGEX)
public @interface ResourceKey {
    /** The regex pattern that a valid namespaced-key must match */
    @RegExp String REGEX = "(" + Namespace.REGEX + ")(:(" + Key.REGEX + "))?";

    /**
     * Validator class for the {@link ResourceKey} annotation to check whether
     * the namespaced-key matches the {@link #REGEX regex}
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
         * Checks whether the namespaced-key matches the {@link #REGEX regex}
         *
         * @param namespacedKey The namespaced-key
         * @return Whether the namespaced-key matches the {@link #REGEX regex}
         */
        public static boolean matches(final @Subst("namespace:key") @ResourceKey @Nullable String namespacedKey) {
            if (namespacedKey == null) {
                return true;
            }

            final int colonIndex = namespacedKey.indexOf(':');

            @Subst("namespace") String namespace = "";
            @Subst("key") String key = namespacedKey;

            if (colonIndex >= 0) {
                key = namespacedKey.substring(colonIndex + 1);

                if (colonIndex >= 1) {
                    namespace = namespacedKey.substring(0, colonIndex);
                }
            }

            return Namespace.Validator.matches(namespace)
                    && Key.Validator.matches(key);
        }

        /**
         * Validates the namespaced-key
         *
         * @param namespacedKey The namespaced-key
         * @throws InvalidRegexException If the namespaced-key does not match
         *                               the {@link #REGEX regex}
         * @see #matches(String)
         */
        public static void validate(final @Subst("namespace:key") @ResourceKey @Nullable String namespacedKey) throws InvalidRegexException {
            if (!matches(namespacedKey)) {
                throw new InvalidRegexException("NamespacedKey must match regex: " + REGEX);
            }
        }
    }
}
