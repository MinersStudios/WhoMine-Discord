package com.minersstudios.whomine.api.throwable;

import org.jetbrains.annotations.Nullable;

/**
 * Unchecked invalid resource exception.
 * <br>
 * Signals that when checking a string against a resource or path pattern, the
 * string was invalid.
 */
public class InvalidResourceException extends RuntimeException {

    /**
     * Constructs a new exception with no detail message
     */
    public InvalidResourceException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public InvalidResourceException(final @Nullable String message) {
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
    public InvalidResourceException(
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
    public InvalidResourceException(final @Nullable Throwable cause) {
        super(cause);
    }
}
