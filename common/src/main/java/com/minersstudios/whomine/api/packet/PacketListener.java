package com.minersstudios.whomine.api.packet;

import com.minersstudios.whomine.api.event.EventExecutor;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.handler.HandlerParams;
import com.minersstudios.whomine.api.listener.ListenerManager;
import com.minersstudios.whomine.api.registrable.Registrable;
import com.minersstudios.whomine.api.throwable.ListenerException;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * An abstract class that represents a packet listener.
 * <table>
 *     <caption>Available optional overridable methods</caption>
 *     <tr>
 *         <th>Method</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #onRegister()}</td>
 *         <td>
 *             Called when the listener is registered by a manager in the
 *             {@link ListenerManager#register(Registrable)} method
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link #onUnregister()}</td>
 *         <td>
 *             Called when the listener is unregistered by a manager in the
 *             {@link ListenerManager#unregister(Registrable)} method
 *         </td>
 *     </tr>
 * </table>
 *
 * @param <C> The packet container type of the event listener
 *
 * @see PacketContainer
 * @see EventExecutor
 */
@SuppressWarnings("unused")
public abstract class PacketListener<C extends PacketContainer<?, ?>>
        extends EventListener<PacketType, C> {

    /**
     * Constructs a new packet listener with the specified packet type.
     * <p>
     * This constructor will automatically retrieve all handlers from the
     * listener class.
     *
     * @param packetType       The packet type of the packet listener
     * @param annotationClass  The annotation class of the event handler
     * @param executorFunction The function to retrieve the handler parameters
     *                         from the annotation
     * @throws ListenerException If the listener has duplicate handlers for the
     *                           same order
     */
    protected <A extends Annotation> PacketListener(
            final @NotNull PacketType packetType,
            final @NotNull Class<A> annotationClass,
            final @NotNull Function<A, HandlerParams<EventOrder>> executorFunction
    ) throws ListenerException {
        super(packetType, annotationClass, executorFunction);
    }
}
