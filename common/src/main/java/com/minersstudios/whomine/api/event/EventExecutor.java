package com.minersstudios.whomine.api.event;

import com.minersstudios.whomine.api.executor.Executor;
import com.minersstudios.whomine.api.listener.handler.HandlerParams;
import com.minersstudios.whomine.api.order.Order;
import com.minersstudios.whomine.api.order.Ordered;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents an executor that can execute
 * {@link EventContainer event container}
 * <p>
 * <b>It also contains :</b>
 * <ul>
 *     <li>The method to execute</li>
 *     <li>The parameters of the event handler</li>
 * </ul>
 *
 * @see EventContainer
 */
@Immutable
public class EventExecutor<O extends Order<O>> implements Executor<EventContainer<?, ?>>, Ordered<O> {
    private static final Logger LOGGER = Logger.getLogger(EventExecutor.class.getSimpleName());

    private final Method method;
    private final HandlerParams<O> params;

    /**
     * Constructs a new event executor
     *
     * @param method The method to execute
     * @param params The parameters of the event handler
     */
    protected EventExecutor(
            final @NotNull Method method,
            final @NotNull HandlerParams<O> params
    ) {
        this.method = method;
        this.params = params;
    }

    /**
     * Returns the method to execute
     *
     * @return The method to execute
     */
    public @NotNull Method getMethod() {
        return this.method;
    }

    /**
     * Returns the parameters of the event handler
     *
     * @return The parameters of the event handler
     */
    public @NotNull HandlerParams<O> getParams() {
        return this.params;
    }

    /**
     * Returns the order of the event
     *
     * @return The order of the event
     */
    @Override
    public @NotNull O getOrder() {
        return this.getParams().getOrder();
    }

    /**
     * @deprecated Use {@link #execute(EventListener, EventContainer)} instead
     */
    @Contract("_ -> fail")
    @Deprecated
    @Override
    public void execute(final @NotNull EventContainer<?, ?> ignored) throws UnsupportedOperationException {
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
    public void execute(
            final @NotNull EventListener<?, ?> listener,
            final @NotNull EventContainer<?, ?> container
    ) {
        try {
            this.method.invoke(listener, container);
        } catch (final Throwable e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Failed to execute event: " + container,
                    e
            );
        }
    }

    /**
     * Returns the hash code of this event executor
     *
     * @return The hash code of this event executor
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.method.hashCode();
        result = prime * result + this.params.hashCode();

        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this event executor
     *
     * @param obj The reference object with which to compare
     * @return True if this event executor is the same as the obj argument
     */
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof EventExecutor<?> other
                        && this.method.equals(other.method)
                        && this.params.equals(other.params)
                );
    }

    /**
     * Returns a string representation of this event executor
     *
     * @return A string representation of this event executor
     */
    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{'
                + "method=" + this.method
                + ", params=" + this.params
                + '}';
    }

    /**
     * Creates an event executor
     *
     * @param method The method to execute
     * @param params The parameters of the event handler
     * @return An event executor
     */
    public static <O extends Order<O>> @NotNull EventExecutor<O> of(
            final @NotNull Method method,
            final @NotNull HandlerParams<O> params
    ) {
        return new EventExecutor<>(method, params);
    }
}
