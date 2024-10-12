package com.minersstudios.whomine.api.event.handle;

import com.minersstudios.whomine.api.event.EventContainer;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.executor.Executor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

/**
 * Represents an executor that can execute
 * {@link EventContainer event container}.
 * <p>
 * Default {@link #execute(EventContainer) method} is unsupported, use
 * {@link #execute(EventListener, EventContainer)} instead.
 *
 * @see EventContainer
 */
@Immutable
public interface EventExecutor extends Executor<EventContainer<?, ?>> {

    /**
     * @deprecated Use {@link #execute(EventListener, EventContainer)} instead
     */
    @Contract("_ -> fail")
    @Deprecated
    @Override
    default void execute(final @NotNull EventContainer<?, ?> ignored) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Use " + this.getClass().getSimpleName() + "#execute(EventListener, EventContainer)"
        );
    }

    /**
     * Executes the event
     *
     * @param listener  The event listener
     * @param container The event container
     */
    void execute(
            final @NotNull EventListener<?, ?> listener,
            final @NotNull EventContainer<?, ?> container
    );
}
