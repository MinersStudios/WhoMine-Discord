package com.minersstudios.whomine.api.listener.handler;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.order.Order;
import com.minersstudios.whomine.api.order.Ordered;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

/**
 * Represents the parameters of a handler
 *
 * @param <O> The order type
 */
@Immutable
public interface HandlerParams<O extends Order<O>> extends Ordered<O>, Comparable<HandlerParams<O>> {
    /** Default order of the handler */
    EventOrder DEFAULT_ORDER = EventOrder.NORMAL;

    /**
     * Returns a string representation of this handler params
     *
     * @return A string representation of this handler params
     */
    @Override
    @NotNull String toString();
}
