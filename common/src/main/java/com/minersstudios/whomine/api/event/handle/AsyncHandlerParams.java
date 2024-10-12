package com.minersstudios.whomine.api.event.handle;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.handler.HandlerParams;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

/**
 * An immutable class that represents the parameters of an async event handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The order of the event handler</li>
 *     <li>Whether the event handler must be called asynchronously</li>
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
 *         <td>{@link #of(AsyncHandler)}</td>
 *         <td>Creates a new event handler params with the given handler's
 *         order and async state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder)}</td>
 *         <td>Creates a new event handler params with the given order and
 *         default async state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(boolean)}</td>
 *         <td>Creates a new event handler params with the default order and
 *         async state</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder, boolean)}</td>
 *         <td>Creates a new event handler params with the given order and async
 *         state</td>
 *     </tr>
 * </table>
 *
 * @see AsyncHandler
 */
@SuppressWarnings("unused")
@Immutable
public class AsyncHandlerParams extends EventHandlerParams {
    /**
     * Default async state of the
     * {@link AsyncHandlerParams async handler params}
     */
    public static final boolean DEFAULT_ASYNC = true;
    private static final AsyncHandlerParams DEFAULT =
            new AsyncHandlerParams(
                    DEFAULT_ORDER,
                    DEFAULT_ASYNC
            );

    private final boolean async;

    protected AsyncHandlerParams(
            final @NotNull EventOrder order,
            final boolean async
    ) {
        super(order);

        this.async = async;
    }

    /**
     * Returns whether the event handler must be called asynchronously
     *
     * @return Whether the event handler must be called asynchronously
     */
    public final boolean isAsync() {
        return this.async;
    }

    @Override
    public int compareTo(final @NotNull HandlerParams<EventOrder> other) {
        int result = super.compareTo(other);

        if (
                result == 0
                && other instanceof AsyncHandlerParams that
        ) {
            result = Boolean.compare(this.async, that.async);
        }

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.getOrder().hashCode();
        result = prime * result + Boolean.hashCode(this.async);

        return result;
    }

    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof AsyncHandlerParams that
                        && this.getOrder().isEqualTo(that.getOrder())
                        && this.async == that.async
                );
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{'
                + "order=" + this.getOrder()
                + ", async=" + this.async
                + '}';
    }

    /**
     * Returns the default event handler params.
     * <p>
     * <ul>
     *     <li>Order: {@link #DEFAULT_ORDER}</li>
     *     <li>Async: {@value #DEFAULT_ASYNC}</li>
     * </ul>
     *
     * @return The default event handler params
     */
    public static @NotNull AsyncHandlerParams defaultParams() {
        return DEFAULT;
    }

    /**
     * Creates a new event handler params with the given handler's order and
     * default async state
     *
     * @param handler The event handler
     * @return A new event handler params with the given handler's order and
     *         default async state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull AsyncHandlerParams of(final @NotNull EventHandler handler) {
        return of(handler.value());
    }

    /**
     * Creates a new event handler params with the given handler's order and
     * async state
     *
     * @param handler The event handler
     * @return A new event handler params with the given handler's order and
     *         async state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull AsyncHandlerParams of(final @NotNull AsyncHandler handler) {
        return of(handler.order(), handler.async());
    }

    /**
     * Creates a new event handler params with the given order and default
     * async state.
     * <p>
     * The default async state is {@value #DEFAULT_ASYNC}.
     *
     * @param order The order of the event handler
     * @return A new event handler params with the given order and default
     *         async state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull AsyncHandlerParams of(final @NotNull EventOrder order) {
        return of(order, DEFAULT_ASYNC);
    }

    /**
     * Creates a new event handler params with the default order and async state.
     * <p>
     * The default order is {@link EventHandlerParams#DEFAULT_ORDER}.
     *
     * @param async Whether the event handler must be called asynchronously
     * @return A new event handler params with the default order and async state
     * @see #of(EventOrder, boolean)
     */
    @Contract("_ -> new")
    public static @NotNull AsyncHandlerParams of(final boolean async) {
        return of(DEFAULT_ORDER, async);
    }

    /**
     * Creates a new event handler params with the given order and async state
     *
     * @param order The order of the event handler
     * @param async Whether the event handler must be called asynchronously
     * @return A new event handler params with the given order and async state
     */
    @Contract("_, _ -> new")
    public static @NotNull AsyncHandlerParams of(
            final @NotNull EventOrder order,
            final boolean async
    ) {
        return new AsyncHandlerParams(order, async);
    }
}
