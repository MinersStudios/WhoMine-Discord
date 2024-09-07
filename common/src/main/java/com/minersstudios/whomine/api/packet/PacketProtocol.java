package com.minersstudios.whomine.api.packet;

import com.minersstudios.whomine.api.packet.type.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;

/**
 * Represents the different packet protocols used in Minecraft. Each protocol
 * corresponds to a specific phase in the network connection process.
 *
 * @see PacketType
 * @see PacketBound
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol">Protocol Wiki</a>
 */
@SuppressWarnings("unused")
public enum PacketProtocol {
    HANDSHAKING  ("handshake",     HandshakePackets.boundedMap()),
    PLAY         ("play",          PlayPackets.boundedMap()),
    STATUS       ("status",        StatusPackets.boundedMap()),
    LOGIN        ("login",         LoginPackets.boundedMap()),
    CONFIGURATION("configuration", ConfigurationPackets.boundedMap());

    private final String id;
    private final Map<PacketBound, Int2ObjectMap<? extends PacketType>> packetMap;

    /**
     * Constructor for the {@link PacketProtocol} enum
     *
     * @param id        The state id of this protocol
     * @param packetMap The map of packet types associated with this protocol,
     *                  organized by packet flow and packet ID
     */
    PacketProtocol(
            final @NotNull String id,
            final @NotNull Map<PacketBound, Int2ObjectMap<? extends PacketType>> packetMap
    ) {
        this.id = id;
        this.packetMap = packetMap;
    }

    /**
     * Returns the state id of this protocol.
     * <p>
     * The id is the same as the id of the minecraft protocol associated with
     * this protocol.
     *
     * @return The state id of this protocol
     */
    public @NotNull String getId() {
        return this.id;
    }

    /**
     * Get the packet map associated with this protocol. The packet map contains
     * packet types organized by packet flow and packet ID.
     *
     * @return The unmodifiable packet map of this protocol.
     */
    public @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<? extends PacketType>> getPacketMap() {
        return Collections.unmodifiableMap(this.packetMap);
    }
}
