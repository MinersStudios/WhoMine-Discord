package com.minersstudios.whomine.api.executor;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an executor that can execute an {@link Executable}
 *
 * @param <E> The executable type
 */
@SuppressWarnings("unused")
public interface Executor<E extends Executable> {

    /**
     * Executes the provided executable
     *
     * @param executable The executable to execute
     * @throws UnsupportedOperationException If the implementation does not
     *                                       support this method
     */
    default void execute(final @NotNull E executable) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use another method provided by the implementation");
    }
}
