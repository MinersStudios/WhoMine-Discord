package com.minersstudios.whomine.api.status;

import com.minersstudios.whomine.api.order.Order;

/**
 * Priority of the status.
 * <br>
 * There are two types of priorities:
 * <ul>
 *     <li>{@link #LOW Low-priority}</li>
 *     <li>{@link #HIGH High-priority}</li>
 * </ul>
 */
public enum StatusPriority implements Order<StatusPriority> {
    LOW, HIGH;

    @Override
    public int asNumber() {
        return this.ordinal();
    }
}
