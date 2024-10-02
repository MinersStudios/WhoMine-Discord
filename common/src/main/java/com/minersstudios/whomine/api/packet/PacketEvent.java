package com.minersstudios.whomine.api.packet;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet event.
 * <p>
 * It contains the packet type, the packet and the connection who sent or
 * received the packet.
 *
 * @param <P> The packet type
 * @param <C> The connection type
 *
 * @see PacketContainer
 * @see PacketListener
 */
@SuppressWarnings("unused")
public abstract class PacketEvent<P, C> {

    private final PacketType type;
    private final P packet;
    private final C connection;
    private boolean cancelled;

    /**
     * Constructs a new packet event
     *
     * @param type       The packet type
     * @param packet     The packet
     * @param connection The connection who sent or received the packet
     */
    protected PacketEvent(
            final @NotNull PacketType type,
            final @NotNull P packet,
            final @NotNull C connection
    ) {
        this.type = type;
        this.packet = packet;
        this.connection = connection;
    }

    /**
     * Returns the packet type
     *
     * @return The packet type
     */
    public final @NotNull PacketType getType() {
        return this.type;
    }

    /**
     * Returns the packet
     *
     * @return The packet
     */
    public final @NotNull P getPacket() {
        return this.packet;
    }

    /**
     * Returns the connection who sent or received the packet
     *
     * @return The connection who sent or received the packet
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
     * Returns the cancellation state of this event
     *
     * @return True if this event is cancelled
     */
    public final boolean isCancelled() {
        return this.cancelled;
    }
}
