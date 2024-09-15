package com.minersstudios.whomine.packet;

import com.minersstudios.whomine.api.packet.PacketContainer;
import com.minersstudios.whomine.api.packet.PacketType;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet container. It contains the packet and the packet type.
 * The packet type contains the id, flow, name and class of the packet. The
 * packet can be modified, but the packet type cannot be changed.
 *
 * @see PacketType
 */
public final class PaperPacketContainer extends PacketContainer<Packet<?>> {

    /**
     * Packet container constructor
     *
     * @param packet The packet to contain
     * @param type   The packet type of the packet
     */
    public PaperPacketContainer(
            final @NotNull Packet<?> packet,
            final @NotNull PacketType type
    ) {
        super(packet, type);
    }
}
