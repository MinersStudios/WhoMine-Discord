package com.minersstudios.whomine.api.event;

import com.minersstudios.whomine.api.order.Ordered;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

/**
 * An immutable class that represents the parameters of an event handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The order of the event handler</li>
 *     <li>Whether the event handler should ignore cancelled events</li>
 * </ul>
 * <table>
 *     <caption>Factory Methods</caption>
 *     <tr>
 *         <th>Method</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #defaultParams()}</td>
 *         <td>Returns the default event handler params</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventHandler)}</td>
 *         <td>Creates a new event handler params with the given handler's
 *         order and ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder)}</td>
 *         <td>Creates a new event handler params with the given order and
 *         default ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(boolean)}</td>
 *         <td>Creates a new event handler params with the default order and
 *         ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder, boolean)}</td>
 *         <td>Creates a new event handler params with the given order and
 *         ignore cancelled state</td>
 *     </tr>
 * </table>
 *
 * @see EventHandler
 */
@SuppressWarnings("unused")
@Immutable
public class EventHandlerParams implements Ordered<EventOrder>, Comparable<EventHandlerParams> {
    private static final EventHandlerParams DEFAULT = new EventHandlerParams(EventOrder.NORMAL, false);

    private final EventOrder order;
    private final boolean ignoreCancelled;

    private EventHandlerParams(
            final @NotNull EventOrder order,
            final boolean ignoreCancelled
    ) {
        this.order = order;
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * Returns the order of the event handler
     *
     * @return The order of the event handler
     */
    @Override
    public final @NotNull EventOrder getOrder() {
        return this.order;
    }

    /**
     * Returns whether the event handler should ignore cancelled events
     *
     * @return Whether the event handler should ignore cancelled events
     */
    public final boolean isIgnoringCancelled() {
        return this.ignoreCancelled;
    }

    @Override
    public int compareTo(final @NotNull EventHandlerParams other) {
        int result = this.compareByOrder(other);

        return result == 0
                ? Boolean.compare(other.ignoreCancelled, this.ignoreCancelled)
                : result;
    }

    /**
     * Returns a hash code value for this event handler params
     *
     * @return A hash code value for this event handler params
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.order.hashCode();
        result = prime * result + Boolean.hashCode(this.ignoreCancelled);

        return result;
    }

    /**
     * Indicates whether some other object is {@code equal to} this event
     * handler params
     *
     * @param obj The reference object with which to compare
     * @return True if this object is the same as the obj argument
     */
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof EventHandlerParams that
                        && this.order == that.order
                        && this.ignoreCancelled == that.ignoreCancelled
                );
    }

    /**
     * Returns a string representation of this event handler params
     *
     * @return A string representation of this event handler params
     */
    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{'
                + "order=" + this.order
                + ", ignoreCancelled=" + this.ignoreCancelled
                + '}';
    }

    /**
     * Returns the default event handler params.
     * <p>
     * <ul>
     *     <li>Order: {@link EventOrder#NORMAL}</li>
     *     <li>Ignore Cancelled: {@code false}</li>
     * </ul>
     *
     * @return The default event handler params
     */
    public static @NotNull EventHandlerParams defaultParams() {
        return DEFAULT;
    }

    /**
     * Creates a new event handler params with the given handler's order and
     * ignore cancelled state
     *
     * @param handler The event handler
     * @return A new event handler params with the given handler's order and
     *         ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final @NotNull EventHandler handler) {
        return of(handler.order(), handler.ignoreCancelled());
    }

    /**
     * Creates a new event handler params with the given order and default
     * ignore cancelled state.
     * <p>
     * The default ignore cancelled state retrieved from the
     * {@link #defaultParams() default params}.
     *
     * @param order The order of the event handler
     * @return A new event handler params with the given order and default
     *         ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final @NotNull EventOrder order) {
        return of(order, DEFAULT.isIgnoringCancelled());
    }

    /**
     * Creates a new event handler params with the default order and ignore
     * cancelled state.
     * <p>
     * The default order retrieved from the {@link #defaultParams() default params}.
     *
     * @param ignoreCancelled Whether the event handler should ignore cancelled
     *                        events
     * @return A new event handler params with the default order and ignore
     *         cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final boolean ignoreCancelled) {
        return of(DEFAULT.getOrder(), ignoreCancelled);
    }

    /**
     * Creates a new event handler params with the given order and ignore
     * cancelled state
     *
     * @param order           The order of the event handler
     * @param ignoreCancelled Whether the event handler should ignore cancelled
     *                        events
     * @return A new event handler params with the given order and ignore
     *         cancelled state
     */
    @Contract("_, _ -> new")
    public static @NotNull EventHandlerParams of(
            final @NotNull EventOrder order,
            final boolean ignoreCancelled
    ) {
        return new EventHandlerParams(order, ignoreCancelled);
    }
}
