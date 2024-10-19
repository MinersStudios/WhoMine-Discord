package com.minersstudios.wholib.paper.packet;

import com.minersstudios.wholib.packet.PacketContainer;
import com.minersstudios.wholib.packet.PacketEvent;
import com.minersstudios.wholib.packet.PacketType;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet event. It contains the packet container and the player
 * who sent or received. The packet container contains the packet and the
 * packet type and can be modified, but the packet type cannot be changed.
 *
 * @see PacketContainer
 */
public final class PaperPacketEvent extends PacketEvent<Packet<?>, Connection> {

    /**
     * Creates a new packet event with the given type, packet and connection
     *
     * @param type       The packet type
     * @param packet     The packet
     * @param connection The connection
     */
    public PaperPacketEvent(
            final @NotNull PacketType type,
            final @NotNull Packet<?> packet,
            final @NotNull Connection connection
    ) {
        super(type, packet, connection);
    }
}
