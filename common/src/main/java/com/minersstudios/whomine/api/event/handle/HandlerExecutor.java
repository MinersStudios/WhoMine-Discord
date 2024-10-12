package com.minersstudios.whomine.api.event.handle;

import com.minersstudios.whomine.api.event.EventContainer;
import com.minersstudios.whomine.api.listener.handler.HandlerParams;
import com.minersstudios.whomine.api.order.Order;
import com.minersstudios.whomine.api.order.Ordered;
import com.minersstudios.whomine.api.throwable.ListenerException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Method;

/**
 * Represents an executor that can execute
 * {@link EventContainer event container}
 * <p>
 * <b>It also contains :</b>
 * <ul>
 *     <li>The event handler with the method to call the root method</li>
 *     <li>The parameters of the event handler</li>
 * </ul>
 *
 * @see EventContainer
 */
@Immutable
public interface HandlerExecutor<O extends Order<O>> extends EventExecutor, Ordered<O> {

    /**
     * Returns the event handler
     *
     * @return The event handler
     */
    @NotNull EventExecutor getHandler();

    /**
     * Returns the parameters of the event handler
     *
     * @return The parameters of the event handler
     */
    @NotNull HandlerParams<O> getParams();

    /**
     * Returns the hash code of this handler executor
     *
     * @return The hash code of this handler executor
     */
    @Override
    int hashCode();

    /**
     * Indicates whether some other object is "equal to" this handler executor
     *
     * @param obj The reference object with which to compare
     * @return True if this handler executor is the same as the obj argument
     */
    @Contract("null -> false")
    @Override
    boolean equals(final @Nullable Object obj);

    /**
     * Returns a string representation of this handler executor
     *
     * @return A string representation of this handler executor
     */
    @Override
    @NotNull String toString();

    /**
     * Creates a handler executor
     *
     * @param method The method to execute
     * @param params The parameters of the event handler
     * @return A handler executor
     */
    @Contract("_, _ -> new")
    static <O extends Order<O>> @NotNull HandlerExecutor<O> of(
            final @NotNull Method method,
            final @NotNull HandlerParams<O> params
    ) throws IllegalStateException, ListenerException {
        return new HandlerExecutorImpl<>(
                EventExecutorFabric.create(method),
                params
        );
    }
}
