package com.minersstudios.whomine.packet;

import com.minersstudios.whomine.api.event.EventExecutor;
import com.minersstudios.whomine.api.listener.Listener;
import com.minersstudios.whomine.api.packet.PacketListener;
import com.minersstudios.whomine.api.packet.PacketType;
import com.minersstudios.whomine.api.throwable.ListenerException;
import com.minersstudios.whomine.listener.api.PaperListenerManager;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class that represents a paper packet listener.
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
 *             {@link PaperListenerManager#register(Listener)} method
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link #onUnregister()}</td>
 *         <td>
 *             Called when the listener is unregistered by a manager in the
 *             {@link PaperListenerManager#unregister(Listener)} method
 *         </td>
 *     </tr>
 * </table>
 *
 * @see PaperPacketContainer
 * @see EventExecutor
 */
@SuppressWarnings("unused")
public abstract class PaperPacketListener extends PacketListener<PaperPacketContainer> {

    /**
     * Constructs a new packet listener with the specified packet type.
     * <p>
     * This constructor will automatically retrieve all handlers from the
     * listener class.
     *
     * @param packetType The packet type of the packet listener
     * @throws ListenerException If the listener has duplicate handlers for the
     *                           same priority
     */
    protected PaperPacketListener(final @NotNull PacketType packetType) throws ListenerException {
        super(packetType);
    }
}
