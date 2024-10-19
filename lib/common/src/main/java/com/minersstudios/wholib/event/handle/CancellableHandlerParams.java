package com.minersstudios.wholib.event.handle;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.handler.HandlerParams;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

/**
 * An immutable class that represents the parameters of a cancellable handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The order of the handler</li>
 *     <li>Whether the handler should ignore cancelled events</li>
 * </ul>
 * <table>
 *     <caption>Factory Methods</caption>
 *     <tr>
 *         <th>Method</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #defaultParams()}</td>
 *         <td>Returns the default cancellable handler params</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(CancellableHandler)}</td>
 *         <td>Creates a new cancellable handler params with the given handler's
 *         order and ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder)}</td>
 *         <td>Creates a new cancellable handler params with the given order and
 *         default ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(boolean)}</td>
 *         <td>Creates a new cancellable handler params with the default order
 *         and ignore cancelled state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder, boolean)}</td>
 *         <td>Creates a new cancellable handler params with the given order and
 *         ignore cancelled state</td>
 *     </tr>
 * </table>
 *
 * @see CancellableHandler
 */
@SuppressWarnings("unused")
@Immutable
public class CancellableHandlerParams extends EventHandlerParams {
    /**
     * Default ignore cancelled state of the
     * {@link CancellableHandlerParams cancellable handler params}
     */
    public static final boolean DEFAULT_IGNORE_CANCELLED = false;
    private static final CancellableHandlerParams DEFAULT =
            new CancellableHandlerParams(
                    DEFAULT_ORDER,
                    DEFAULT_IGNORE_CANCELLED
            );

    private final boolean ignoreCancelled;

    protected CancellableHandlerParams(
            final @NotNull EventOrder order,
            final boolean ignoreCancelled
    ) {
        super(order);

        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * Returns whether the handler should ignore cancelled events
     *
     * @return Whether the handler should ignore cancelled events
     */
    public final boolean isIgnoringCancelled() {
        return this.ignoreCancelled;
    }

    @Override
    public int compareTo(final @NotNull HandlerParams<EventOrder> other) {
        int result = super.compareTo(other);

        if (
                result == 0
                && other instanceof CancellableHandlerParams that
        ) {
            result = Boolean.compare(this.ignoreCancelled, that.ignoreCancelled);
        }

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.getOrder().hashCode();
        result = prime * result + Boolean.hashCode(this.ignoreCancelled);

        return result;
    }

    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof CancellableHandlerParams that
                        && this.getOrder().isEqualTo(that.getOrder())
                        && this.ignoreCancelled == that.ignoreCancelled
                );
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{'
                + "order=" + this.getOrder()
                + ", ignoreCancelled=" + this.ignoreCancelled
                + '}';
    }

    /**
     * Returns the default cancellable handler params.
     * <p>
     * <ul>
     *     <li>Order: {@link EventOrder#NORMAL}</li>
     *     <li>Ignore Cancelled: {@code false}</li>
     * </ul>
     *
     * @return The default cancellable handler params
     */
    public static @NotNull CancellableHandlerParams defaultParams() {
        return DEFAULT;
    }

    /**
     * Creates a new cancellable handler params with the given handler's order
     * and default ignore cancelled state
     *
     * @param handler The cancellable event handler
     * @return A new cancellable handler params with the given handler's order
     *         and default ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull CancellableHandlerParams of(final @NotNull EventHandler handler) {
        return of(handler.value());
    }

    /**
     * Creates a new cancellable handler params with the given handler's order
     * and ignore cancelled state
     *
     * @param handler The cancellable event handler
     * @return A new cancellable handler params with the given handler's order
     *         and ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull CancellableHandlerParams of(final @NotNull CancellableHandler handler) {
        return of(handler.order(), handler.ignoreCancelled());
    }

    /**
     * Creates a new cancellable handler params with the given order and default
     * ignore cancelled state.
     * <p>
     * The default ignore cancelled state retrieved from the
     * {@link #defaultParams() default params}.
     *
     * @param order The order of the handler
     * @return A new cancellable handler params with the given order and default
     *         ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull CancellableHandlerParams of(final @NotNull EventOrder order) {
        return of(order, DEFAULT_IGNORE_CANCELLED);
    }

    /**
     * Creates a new cancellable handler params with the default order and
     * ignore cancelled state.
     * <p>
     * The default order is {@link EventHandlerParams#DEFAULT_ORDER}.
     *
     * @param ignoreCancelled Whether the cancellable event handler should
     *                        ignore cancelled events
     * @return A new cancellable handler params with the default order and
     *         ignore cancelled state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull CancellableHandlerParams of(final boolean ignoreCancelled) {
        return of(DEFAULT_ORDER, ignoreCancelled);
    }

    /**
     * Creates a new cancellable handler params with the given order and ignore
     * cancelled state
     *
     * @param order           The order of the handler
     * @param ignoreCancelled Whether the cancellable handler should ignore
     *                        cancelled events
     * @return A new cancellable handler params with the given order and ignore
     *         cancelled state
     */
    @Contract("_, _ -> new")
    public static @NotNull CancellableHandlerParams of(
            final @NotNull EventOrder order,
            final boolean ignoreCancelled
    ) {
        return new CancellableHandlerParams(order, ignoreCancelled);
    }
}
