package com.minersstudios.whomine.api.packet;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet event. It contains the packet container and the player
 * who sent or received. The packet container contains the packet and the
 * packet type and can be modified, but the packet type cannot be changed.
 *
 * @see PacketContainer
 */
public abstract class PacketEvent<P extends PacketContainer<?>, C> {

    private final P packetContainer;
    private final C connection;
    private boolean cancelled;

    /**
     * @param packetContainer The packet container
     * @param connection      The connection
     */
    protected PacketEvent(
            final @NotNull P packetContainer,
            final @NotNull C connection
    ) {
        this.packetContainer = packetContainer;
        this.connection = connection;
    }

    /**
     * Returns the packet container of this event
     *
     * @return The packet container of this event
     */
    public final @NotNull P getPacketContainer() {
        return this.packetContainer;
    }

    /**
     * Returns the player who sent or received the packet
     *
     * @return The player who sent or received the packet
     */
    public final @NotNull C getConnection() {
        return this.connection;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not be
     * sent or received.
     *
     * @param cancel True if you wish to cancel this event
     */
    public final void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * @return True if this event is cancelled
     */
    public final boolean isCancelled() {
        return this.cancelled;
    }
}
