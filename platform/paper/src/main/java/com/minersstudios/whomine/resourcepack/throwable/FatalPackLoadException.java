package com.minersstudios.whomine.resourcepack.throwable;

import org.jetbrains.annotations.Nullable;

/**
 * Fatal resource pack load exception.
 * <br>
 * Signals that a fatal error occurred while loading the resource pack and
 * the resource pack object cannot be created
 *
 * @see PackLoadException
 */
public class FatalPackLoadException extends Exception {

    /**
     * Constructs a new exception with no detail message
     */
    public FatalPackLoadException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public FatalPackLoadException(final @Nullable String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause
     *
     * @param message The detail message
     *                (which is saved for later retrieval by
     *                {@link #getMessage()} method)
     * @param cause   The cause
     *                (which is saved for later retrieval by {@link #getCause()}
     *                method)
     *                (A null value is permitted, and indicates that the cause
     *                is nonexistent or unknown)
     */
    public FatalPackLoadException(
            final @Nullable String message,
            final @Nullable Throwable cause
    ) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of {@code (cause == null ? null : cause.toString())}
     *
     * @param cause The cause
     *              (which is saved for later retrieval by {@link #getCause()}
     *              method)
     *              (A null value is permitted, and indicates that the cause is
     *              nonexistent or unknown)
     */
    public FatalPackLoadException(final @Nullable Throwable cause) {
        super(cause);
    }
}
