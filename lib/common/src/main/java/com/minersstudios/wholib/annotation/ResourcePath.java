package com.minersstudios.wholib.annotation;

import com.minersstudios.wholib.throwable.InvalidResourceException;
import com.minersstudios.wholib.utility.ResourcedPath;
import io.netty.buffer.ByteBufUtil;
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
 * Annotation used to mark the string as {@link ResourcedPath resourced-path}.
 * <p>
 * The resourced-path must match the {@link #REGEX regex} pattern.
 * <p>
 * Example of usage:
 * <pre>
 *     {@code @ResourcePath String resourcedPath = "resource:path";}
 *     {@code @ResourcePath String resourcedPath = "path";}
 * </pre>
 *
 * @see ResourcePath.Validator
 * @see Resource
 * @see Path
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
@Pattern(ResourcePath.REGEX)
public @interface ResourcePath {
    /** The regex pattern that a valid resourced-path must match */
    @RegExp String REGEX = "(" + Resource.REGEX + ")(:(" + Path.REGEX + "))?";

    /**
     * Validator class for the {@link ResourcePath} annotation to check whether
     * the resourced-path matches the {@link #REGEX regex}
     *
     * @see #matches(String)
     * @see #matchesLength(String)
     * @see #validate(String)
     * @see #validateLength(String)
     */
    final class Validator {

        @Contract(" -> fail")
        private Validator() throws AssertionError {
            throw new AssertionError("Utility class");
        }

        /**
         * Checks whether the resourced-path matches the {@link #REGEX regex}
         *
         * @param resourcedPath The resourced-path
         * @return Whether the resourced-path matches the {@link #REGEX regex}
         */
        @Contract("null -> true")
        public static boolean matches(final @Subst("resource:path") @ResourcePath @Nullable String resourcedPath) {
            if (resourcedPath == null) {
                return true;
            }

            if (!matchesLength(resourcedPath)) {
                return false;
            }

            final int colonIndex = resourcedPath.indexOf(':');

            @Subst("resource") String resource = "";
            @Subst("path") String path = resourcedPath;

            if (colonIndex >= 0) {
                path = resourcedPath.substring(colonIndex + 1);

                if (colonIndex >= 1) {
                    resource = resourcedPath.substring(0, colonIndex);
                }
            }

            return Resource.Validator.matches(resource)
                && Path.Validator.matches(path);
        }

        /**
         * Checks whether the resourced-path matches the length requirements
         *
         * @param resourcedPath The resourced-path
         * @return Whether the resourced-path matches the length requirements
         */
        @Contract("null -> true")
        public static boolean matchesLength(final @Subst("resource:path") @ResourcePath @Nullable String resourcedPath) {
            return resourcedPath == null
                    || (
                            resourcedPath.length() <= Short.MAX_VALUE
                            && ByteBufUtil.utf8MaxBytes(resourcedPath) <= 2 * Short.MAX_VALUE + 1
                    );
        }

        /**
         * Validates the resourced-path.
         * <p>
         * If the resourced-path is {@code null}, then it is considered valid.
         *
         * @param resourcedPath The resourced-path
         * @throws InvalidResourceException If the resourced-path does not match
         *                                  the {@link #REGEX regex}
         * @see #matches(String)
         */
        public static void validate(final @Subst("resource:path") @ResourcePath @Nullable String resourcedPath) throws InvalidResourceException {
            if (!matches(resourcedPath)) {
                throw new InvalidResourceException("Resourced-path must match regex: " + REGEX);
            }
        }

        /**
         * Validates the length of the resourced-path.
         * <p>
         * If the resourced-path is {@code null}, then it is considered valid.
         *
         * @param resourcedPath The resourced-path
         * @throws InvalidResourceException If the resourced-path is too long
         */
        public static void validateLength(final @Subst("resource:path") @ResourcePath @Nullable String resourcedPath) throws InvalidResourceException {
            if (!matchesLength(resourcedPath)) {
                throw new InvalidResourceException("Resourced-path is too long: " + resourcedPath);
            }
        }
    }
}
