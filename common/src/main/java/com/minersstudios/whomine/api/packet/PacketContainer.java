package com.minersstudios.whomine.api.packet;

import com.minersstudios.whomine.api.event.EventContainer;
import com.minersstudios.whomine.api.module.Module;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet container.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The module that registers and handles the packet event</li>
 *     <li>The packet event itself</li>
 * </ul>
 *
 * @param <M> The module type, that registers and handles the packet event
 * @param <E> The packet event type
 *
 * @see PacketEvent
 * @see PacketListener
 * @see EventContainer
 */
@SuppressWarnings("unused")
public abstract class PacketContainer<M extends Module, E extends PacketEvent<?, ?>> extends EventContainer<M, E> {

    /**
     * Packet container constructor
     *
     * @param module      The module that registers and handles the packet event
     * @param packetEvent The packet event associated with this packet container
     */
    protected PacketContainer(
            final @NotNull M module,
            final @NotNull E packetEvent
    ) {
        super(module, packetEvent);
    }
}
