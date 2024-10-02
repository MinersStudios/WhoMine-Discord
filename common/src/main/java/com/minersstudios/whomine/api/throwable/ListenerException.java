package com.minersstudios.whomine.api.throwable;

import com.minersstudios.whomine.api.listener.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an exception thrown when an error occurs while working with
 * {@link Listener listeners}
 */
public class ListenerException extends RuntimeException {

    /**
     * Constructs a new exception with no detail message
     */
    public ListenerException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public ListenerException(final @NotNull String message) {
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
    public ListenerException(
            final @NotNull String message,
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
    public ListenerException(final @Nullable Throwable cause) {
        super(cause);
    }
}
