package com.minersstudios.wholib.event.handle;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.handler.AbstractHandlerParams;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

/**
 * An immutable class that represents the parameters of an event handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The order of the handler</li>
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
 *         order</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(EventOrder)}</td>
 *         <td>Creates a new event handler params with the given order</td>
 *     </tr>
 * </table>
 *
 * @see EventHandler
 */
@SuppressWarnings("unused")
@Immutable
public class EventHandlerParams extends AbstractHandlerParams<EventOrder> {
    /** Default order of the handler */
    public static EventOrder DEFAULT_ORDER = EventOrder.NORMAL;
    private static final EventHandlerParams DEFAULT = new EventHandlerParams(DEFAULT_ORDER);

    protected EventHandlerParams(final @NotNull EventOrder order) {
        super(order);
    }

    /**
     * Returns the default event handler params.
     * <p>
     * <ul>
     *     <li>Order: {@link EventOrder#NORMAL}</li>
     * </ul>
     *
     * @return The default event handler params
     */
    public static @NotNull EventHandlerParams defaultParams() {
        return DEFAULT;
    }

    /**
     * Creates a new event handler params with the given handler's order
     *
     * @param handler The event handler
     * @return A new event handler params with the given handler's order
     * @see #of(EventOrder)
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final @NotNull EventHandler handler) {
        return of(handler.value());
    }

    /**
     * Creates a new event handler params with the given order
     *
     * @param order The order of the event handler
     * @return A new event handler params with the given order
     */
    @Contract("_ -> new")
    public static @NotNull EventHandlerParams of(final @NotNull EventOrder order) {
        return new EventHandlerParams(order);
    }
}
