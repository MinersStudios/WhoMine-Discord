package com.minersstudios.whomine.api.listener.handler;

import com.minersstudios.whomine.api.order.Order;
import com.minersstudios.whomine.api.order.Ordered;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

/**
 * Represents the parameters of a handler
 *
 * @param <O> The order type
 */
@Immutable
public interface HandlerParams<O extends Order<O>> extends Ordered<O>, Comparable<HandlerParams<O>> {

    /**
     * Returns a hash code value for this handler params
     *
     * @return A hash code value for this handler params
     */
    @Override
    int hashCode();

    /**
     * Indicates whether some other object is {@code equal to} this
     * handler params
     *
     * @param obj The reference object with which to compare
     * @return True if this object is the same as the obj argument
     */
    @Contract("null -> false")
    @Override
    boolean equals(final @Nullable Object obj);

    /**
     * Returns a string representation of this handler params
     *
     * @return A string representation of this handler params
     */
    @Override
    @NotNull String toString();
}
