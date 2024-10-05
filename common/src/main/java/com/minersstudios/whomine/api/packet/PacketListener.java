package com.minersstudios.whomine.api.packet;

import com.minersstudios.whomine.api.event.EventExecutor;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.listener.ListenerManager;
import com.minersstudios.whomine.api.registrable.Registrable;
import com.minersstudios.whomine.api.throwable.ListenerException;
import org.jetbrains.annotations.NotNull;

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
     * @param packetType The packet type of the packet listener
     * @throws ListenerException If the listener has duplicate handlers for the
     *                           same order
     */
    protected PacketListener(final @NotNull PacketType packetType) throws ListenerException {
        super(packetType);
    }
}
