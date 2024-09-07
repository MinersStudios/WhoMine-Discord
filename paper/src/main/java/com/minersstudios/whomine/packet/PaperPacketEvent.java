package com.minersstudios.whomine.packet;

import com.minersstudios.whomine.api.packet.PacketContainer;
import com.minersstudios.whomine.api.packet.PacketEvent;
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
public final class PaperPacketEvent extends PacketEvent<PaperPacketContainer, Connection> {

    /**
     * @param packetContainer The packet container
     * @param connection      The connection
     */
    public PaperPacketEvent(
            final @NotNull PaperPacketContainer packetContainer,
            final @NotNull Connection connection
    ) {
        super(packetContainer, connection);
    }
}
