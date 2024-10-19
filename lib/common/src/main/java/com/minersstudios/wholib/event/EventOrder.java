package com.minersstudios.wholib.event;

import com.minersstudios.wholib.order.Order;
import com.minersstudios.wholib.order.Ordered;

/**
 * This enum represents the order.
 * <p>
 * <b>Handling occurs in the following order :</b>
 * <ol>
 *     <li>{@link #LOWEST}</li>
 *     <li>{@link #LOW}</li>
 *     <li>{@link #NORMAL}</li>
 *     <li>{@link #HIGH}</li>
 *     <li>{@link #HIGHEST}</li>
 *     <li>{@link #CUSTOM}</li>
 * </ol>
 *
 * @see Ordered
 */
public enum EventOrder implements Order<EventOrder> {
    LOWEST, LOW, NORMAL, HIGH, HIGHEST, CUSTOM;

    @Override
    public int asNumber() {
        return this.ordinal();
    }
}
