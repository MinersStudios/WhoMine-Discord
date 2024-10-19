package com.minersstudios.wholib.event.handle;

import com.minersstudios.wholib.event.EventContainer;
import com.minersstudios.wholib.listener.handler.HandlerParams;
import com.minersstudios.wholib.order.Order;
import com.minersstudios.wholib.order.Ordered;
import com.minersstudios.wholib.throwable.ListenerException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Method;

/**
 * Represents an executor that can execute
 * {@link EventContainer event container}
 * <p>
 * <b>It also contains :</b>
 * <ul>
 *     <li>The parameters of the event handler</li>
 * </ul>
 *
 * @see EventContainer
 */
@Immutable
public interface HandlerExecutor<O extends Order<O>> extends EventExecutor, Ordered<O> {

    /**
     * Returns the parameters of the event handler
     *
     * @return The parameters of the event handler
     */
    @NotNull HandlerParams<O> getParams();

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
        return HandlerExecutorFabric.create(method, params);
    }
}
