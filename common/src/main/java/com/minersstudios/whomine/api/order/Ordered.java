package com.minersstudios.whomine.api.order;

import com.minersstudios.whomine.api.event.EventOrder;
import org.jetbrains.annotations.NotNull;

/**
 * An interface that represents an object that can be ordered
 *
 * @param <O> The type of the object to compare
 *
 * @see EventOrder
 */
@SuppressWarnings("unused")
public interface Ordered<O extends Order<O>> {

    /**
     * Returns the order of this object
     *
     * @return The order of this object
     */
    @NotNull O getOrder();

    /**
     * Returns whether this object is higher than the provided object using
     * the {@link #compareByOrder(Order)} method
     *
     * @param other The object to compare
     * @return Whether this object is higher than the provided object
     * @see #compareByOrder(Order)
     */
    default boolean isHigherThan(final @NotNull O other) {
        return this.compareByOrder(other) > 0;
    }

    /**
     * Returns whether this object is higher than or equal to the provided
     * object using the {@link #compareByOrder(Order)} method
     *
     * @param other The object to compare
     * @return Whether this object is higher or equal than the provided object
     * @see #compareByOrder(Order)
     */
    default boolean isHigherOrEqualThan(final @NotNull O other) {
        return this.compareByOrder(other) >= 0;
    }

    /**
     * Returns whether this object is higher than or equal to (if {@code orEqual}
     * is {@code true}) the provided object using the {@link #compareByOrder(Order)}
     * method
     *
     * @param other   The object to compare
     * @param orEqual Whether to check if this object is equal to the provided
     *                object
     * @return Whether this object is higher or equal than the provided object
     * @see #compareByOrder(Order)
     */
    default boolean isHigherThan(
            final @NotNull O other,
            final boolean orEqual
    ) {
        return orEqual ? isHigherOrEqualThan(other) : isHigherThan(other);
    }

    /**
     * Returns whether this object is lower than the provided object using
     * the {@link #compareByOrder(Order)} method
     *
     * @param other The object to compare
     * @return Whether this object is lower than the provided object
     * @see #compareByOrder(Order)
     */
    default boolean isLowerThan(final @NotNull O other) {
        return this.compareByOrder(other) < 0;
    }

    /**
     * Returns whether this object is lower than or equal to the provided
     * object using the {@link #compareByOrder(Order)} method
     *
     * @param other The object to compare
     * @return Whether this object is lower or equal than the provided object
     * @see #compareByOrder(Order)
     */
    default boolean isLowerOrEqualThan(final @NotNull O other) {
        return this.compareByOrder(other) <= 0;
    }

    /**
     * Returns whether this object is lower than or equal to (if {@code orEqual}
     * is {@code true}) the provided object using the {@link #compareByOrder(Order)}
     * method
     *
     * @param other   The object to compare
     * @param orEqual Whether to check if this object is equal to the provided
     *                object
     * @return Whether this object is lower or equal than the provided object
     * @see #compareByOrder(Order)
     */
    default boolean isLowerThan(
            final @NotNull O other,
            final boolean orEqual
    ) {
        return orEqual ? isLowerOrEqualThan(other) : isLowerThan(other);
    }

    /**
     * Returns whether this object is equal to the provided object using the
     * {@link #compareByOrder(Order)} method
     *
     * @param other The object to compare
     * @return Whether this object is equal to the provided object
     * @see #compareByOrder(Order)
     */
    default boolean isEqualTo(final @NotNull O other) {
        return this.compareByOrder(other) == 0;
    }

    /**
     * Compares this object with the specified order
     *
     * @param order The order to compare
     * @return The value {@code 0} if this order is equal to the provided order;
     *         a value less than {@code 0} if this order is lower than the
     *         provided order; and a value greater than {@code 0} if this order
     *         is higher than the provided order
     */
    default int compareByOrder(final @NotNull O order) {
        return this.getOrder().compareTo(order);
    }

    /**
     * Compares this object with the specified ordered object with the same
     * order type
     *
     * @param ordered The ordered object to compare
     * @return The value {@code 0} if this order is equal to the provided order;
     *         a value less than {@code 0} if this order is lower than the
     *         provided order; and a value greater than {@code 0} if this order
     *         is higher than the provided order
     */
    default int compareByOrder(final @NotNull Ordered<O> ordered) {
        return this.compareByOrder(ordered.getOrder());
    }

    /**
     * Compares this object with the specified order by its
     * {@link Order#asNumber() order number} bypassing overridden
     * {@link Order#compareTo(Order)} methods
     *
     * @param order The order to compare
     * @return The value {@code 0} if this order is equal to the provided order;
     *         a value less than {@code 0} if this order is lower than the
     *         provided order; and a value greater than {@code 0} if this order
     *         is higher than the provided order
     */
    default int rawCompareByOrder(final @NotNull Order<?> order) {
        return Integer.compare(this.getOrder().asNumber(), order.asNumber());
    }

    /**
     * Compares this object with the specified ordered object by its
     * {@link Order#asNumber() order number} bypassing overridden
     * {@link Order#compareTo(Order)} methods
     *
     * @param ordered The ordered object to compare
     * @return The value {@code 0} if this order is equal to the provided order;
     *         a value less than {@code 0} if this order is lower than the
     *         provided order; and a value greater than {@code 0} if this order
     *         is higher than the provided order
     */
    default int rawCompareByOrder(final @NotNull Ordered<?> ordered) {
        return ordered.rawCompareByOrder(this.getOrder());
    }
}
