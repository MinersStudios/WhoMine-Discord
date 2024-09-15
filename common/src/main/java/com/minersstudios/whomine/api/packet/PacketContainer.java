package com.minersstudios.whomine.api.packet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a packet container. It contains the packet and the packet type.
 * The packet type contains the id, flow, name and class of the packet. The
 * packet can be modified, but the packet type cannot be changed.
 *
 * @see PacketType
 * @param <P> The packet's object type
 */
@SuppressWarnings("unused")
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
     * Returns the packet type of the packet contained in this container
     *
     * @return The packet type of the packet contained in this container
     */
    public @NotNull PacketType getType() {
        return this.type;
    }

    /**
     * Returns hash code value for this packet container
     *
     * @return The hash code value for this packet container
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.packet.hashCode();
        result = prime * result + this.type.hashCode();

        return result;
    }

    /**
     * Returns whether the given object is equal to this packet container
     *
     * @param obj The object to compare
     * @return True if the given object is equal to this packet container
     */
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof PacketContainer<?> that
                        && this.packet.equals(that.packet)
                        && this.type.equals(that.type)
                );
    }

    /**
     * Returns the string representation of this packet container
     *
     * @return The string representation of this packet container
     */
    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{' +
                "packet=" + this.packet.getClass() +
                ", type=" + this.type +
                '}';
    }
}
