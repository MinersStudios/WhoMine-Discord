package com.minersstudios.wholib.order;

import org.jetbrains.annotations.NotNull;

/**
 * An interface that represents an order
 */
@SuppressWarnings("unused")
public interface Order<O extends Order<O>> extends Comparable<O> {

    /**
     * Returns the order as a number
     *
     * @return The order as a number
     */
    int asNumber();

    /**
     * Returns whether this order is higher than the provided order
     *
     * @param other The order to compare
     * @return Whether this order is higher than the provided order
     */
    default boolean isHigherThan(final @NotNull O other) {
        return this.compareTo(other) > 0;
    }

    /**
     * Returns whether this order is lower than the provided order
     *
     * @param other The order to compare
     * @return Whether this order is lower than the provided order
     */
    default boolean isLowerThan(final @NotNull O other) {
        return this.compareTo(other) < 0;
    }

    /**
     * Returns whether this order is equal to the provided order
     *
     * @param other The order to compare
     * @return Whether this order is equal to the provided order
     */
    default boolean isEqualTo(final @NotNull O other) {
        return this.compareTo(other) == 0;
    }

    /**
     * Compares this order to the provided order
     *
     * @param other The order to compare
     * @return The value {@code 0} if this order is equal to the provided order;
     *         a value less than {@code 0} if this order is lower than the
     *         provided order; and a value greater than {@code 0} if this order
     *         is higher than the provided order
     */
    @Override
    default int compareTo(final @NotNull O other) {
        return Integer.compare(this.asNumber(), other.asNumber());
    }
}
