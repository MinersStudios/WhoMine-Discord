package com.minersstudios.whomine.api.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

/**
 * An immutable class that represents the parameters of an event handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The priority of the event handler</li>
 *     <li>Whether the event handler should ignore cancelled events</li>
 * </ul>
 * <table>
 *     <caption>Factory Methods</caption>
 *     <tr>
 *         <th>Method</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #normal()}</td>
 *         <td>Returns the default event handler params</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventHandler)}</td>
 *         <td>Creates a new event handler params with the given handler's
 *         priority and ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder)}</td>
 *         <td>Creates a new event handler params with the given priority and
 *         default ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(boolean)}</td>
 *         <td>Creates a new event handler params with the default priority and
 *         ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder, boolean)}</td>
 *         <td>Creates a new event handler params with the given priority and
 *         ignore cancelled state</td>
 *     </tr>
 * </table>
 *
 * @see EventHandler
 */
@SuppressWarnings("unused")
@Immutable
public final class EventHandlerParams implements Comparable<EventHandlerParams> {
    private static final EventHandlerParams NORMAL = new EventHandlerParams(EventOrder.NORMAL, false);

    private final EventOrder priority;
    private final boolean ignoreCancelled;

    private EventHandlerParams(
            final @NotNull EventOrder priority,
            final boolean ignoreCancelled
    ) {
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * Returns the priority of the event handler
     *
     * @return The priority of the event handler
     */
    public @NotNull EventOrder getPriority() {
        return this.priority;
    }

    /**
     * Returns whether the event handler should ignore cancelled events
     *
     * @return Whether the event handler should ignore cancelled events
     */
    public boolean isIgnoringCancelled() {
        return this.ignoreCancelled;
    }

    /**
     * Compares this event handler params with the specified event handler
     * params for order.
     * <p>
     * Returns a negative integer, zero, or a positive integer as this event
     * handler params is less than, equal to, or greater than the specified
     * event handler params.
     *
     * @param that The event handler params to be compared
     * @return A negative integer, zero, or a positive integer as this event
     *         handler params is less than, equal to, or greater than the
     *         specified event handler params
     */
    @Override
    public int compareTo(final @NotNull EventHandlerParams that) {
        return this.priority.compareTo(that.priority);
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

        result = prime * result + this.priority.hashCode();
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
                        && this.priority == that.priority
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
                + "priority=" + this.priority
                + ", ignoreCancelled=" + this.ignoreCancelled
                + '}';
    }

    /**
     * Returns the default event handler params.
     * <p>
     * <ul>
     *     <li>Priority: {@link EventOrder#NORMAL}</li>
     *     <li>Ignore Cancelled: {@code false}</li>
     * </ul>
     *
     * @return The default event handler params
     */
    public static @NotNull EventHandlerParams normal() {
        return NORMAL;
    }

    /**
     * Creates a new event handler params with the given handler's priority and
     * ignore cancelled state
     *
     * @param handler The event handler
     * @return A new event handler params with the given handler's priority and
     *         ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final @NotNull EventHandler handler) {
        return of(handler.priority(), handler.ignoreCancelled());
    }

    /**
     * Creates a new event handler params with the given priority and default
     * ignore cancelled state.
     * <p>
     * The default ignore cancelled state retrieved from the
     * {@link #normal() default params}.
     *
     * @param priority The priority of the event handler
     * @return A new event handler params with the given priority and default
     *         ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final @NotNull EventOrder priority) {
        return of(priority, NORMAL.isIgnoringCancelled());
    }

    /**
     * Creates a new event handler params with the default priority and ignore
     * cancelled state.
     * <p>
     * The default priority retrieved from the {@link #normal() default params}.
     *
     * @param ignoreCancelled Whether the event handler should ignore cancelled
     *                        events
     * @return A new event handler params with the default priority and ignore
     *         cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final boolean ignoreCancelled) {
        return of(NORMAL.getPriority(), ignoreCancelled);
    }

    /**
     * Creates a new event handler params with the given priority and ignore
     * cancelled state
     *
     * @param priority        The priority of the event handler
     * @param ignoreCancelled Whether the event handler should ignore cancelled
     *                        events
     * @return A new event handler params with the given priority and ignore
     *         cancelled state
     */
    @Contract("_, _ -> new")
    public static @NotNull EventHandlerParams of(
            final @NotNull EventOrder priority,
            final boolean ignoreCancelled
    ) {
        return new EventHandlerParams(priority, ignoreCancelled);
    }
}
