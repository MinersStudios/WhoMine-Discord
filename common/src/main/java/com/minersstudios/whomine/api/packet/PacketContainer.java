package com.minersstudios.whomine.api.packet;

import com.minersstudios.whomine.api.packet.type.PacketType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet container. It contains the packet and the packet type.
 * The packet type contains the id, flow, name and class of the packet. The
 * packet can be modified, but the packet type cannot be changed.
 *
 * @see PacketType
 */
public abstract class PacketContainer<P> {

    private P packet;
    private final PacketType type;

    /**
     * Packet container constructor
     *
     * @param packet The packet to contain
     * @param type   The packet type of the packet
     */
    protected PacketContainer(
            final @NotNull P packet,
            final @NotNull PacketType type
    ) {
        this.packet = packet;
        this.type = type;
    }

    /**
     * Returns the packet contained in this container
     *
     * @return The packet contained in this container
     */
    public @NotNull P getPacket() {
        return this.packet;
    }

    /**
     * @param packet The packet to set
     * @throws IllegalArgumentException If the packet type of the packet is
     *                                  different from the packet type of this
     *                                  container
     *                                  (Checks by comparing the classes)
     */
    public void setPacket(final @NotNull P packet) throws IllegalArgumentException {
        if (this.packet.getClass() != packet.getClass()) {
            throw new IllegalArgumentException("Packet type cannot be changed!");
        }

        this.packet = packet;
    }

    /**
     * @return The packet type of the packet contained in this container
     */
    public @NotNull PacketType getType() {
        return this.type;
    }

    /**
     * @return The string representation of this packet container
     */
    @Override
    public @NotNull String toString() {
        return "PacketContainer{" +
                "packet=" + this.packet.getClass() +
                ", type=" + this.type +
                '}';
    }
}
