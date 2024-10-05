package com.minersstudios.whomine.api.listener.handler;

import com.minersstudios.whomine.api.order.Order;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public int hashCode() {
        return 31 + this.getOrder().hashCode();
    }

    @SuppressWarnings("unchecked")
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof HandlerParams<?> that) {
            final O thisOrder = this.getOrder();
            final var thatOrder = that.getOrder();

            return thisOrder.getClass().isInstance(thatOrder)
                    && thisOrder.isEqualTo((O) thatOrder);
        }

        return false;
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{'
                + "order=" + this.order
                + '}';
    }
}
