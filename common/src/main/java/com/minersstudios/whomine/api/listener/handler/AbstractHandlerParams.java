package com.minersstudios.whomine.api.listener.handler;

import com.minersstudios.whomine.api.order.Order;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

/**
 * An immutable abstract class that represents the parameters of a handler.
 *
 * @param <O> The order type
 */
@Immutable
public abstract class AbstractHandlerParams<O extends Order<O>>
        implements HandlerParams<O> {

    private final O order;

    protected AbstractHandlerParams(final @NotNull O order) {
        this.order = order;
    }

    @Override
    public final @NotNull O getOrder() {
        return this.order;
    }

    @Override
    public int compareTo(final @NotNull HandlerParams<O> other) {
        return this.compareByOrder(other);
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{'
                + "order=" + this.order
                + '}';
    }
}
