package com.minersstudios.whomine.api.packet.type;

import com.minersstudios.whomine.api.packet.PacketBound;
import com.minersstudios.whomine.api.packet.PacketProtocol;
import com.minersstudios.whomine.api.packet.PacketRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;

import static com.minersstudios.whomine.api.packet.PacketBound.SERVERBOUND;

/**
 * This enum represents a handshake packet type used in the Minecraft server
 * networking. It contains information about packet's {@link PacketBound flow}
 * (CLIENTBOUND or SERVERBOUND), its ID, and its name.
 *
 * @see PacketType
 * @see PacketProtocol
 * @see PacketBound
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol#Handshaking">Protocol Wiki</a>
 * @version 1.21.1, protocol 767
 */
@SuppressWarnings("unused")
public enum HandshakePackets implements PacketType {
    //<editor-fold desc="Handshaking client packets" defaultstate="collapsed">

    // There are no clientbound packets in the Handshaking state, since the
    // protocol immediately switches to a different state after the client sends
    // the first packet.

    //</editor-fold>
    //<editor-fold desc="Handshaking server packets" defaultstate="collapsed">

    SERVER_HANDSHAKE              (SERVERBOUND, 0x00, "Handshake"),
    SERVER_LEGACY_SERVER_LIST_PING(SERVERBOUND, 0xFE, "Legacy Server List Ping");

    //</editor-fold>

    private static final Map<PacketBound, Int2ObjectMap<HandshakePackets>> PACKET_MAP = PacketType.createMap(values());

    private final PacketBound flow;
    private final int id;
    private final String name;

    HandshakePackets(
            final @NotNull PacketBound flow,
            final int id,
            final @NotNull String name
    ) {
        this.flow = flow;
        this.id = id;
        this.name = name;
    }

    @Override
    public @NotNull PacketBound getFlow() {
        return this.flow;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull String toString() {
        return this.name() + '{' +
                "bound=" + this.flow +
                ", id=" + this.id +
                ", name='" + this.name + '\'' +
                '}';
    }

    /**
     * Retrieves the packet associated with given flow and ID
     *
     * @param flow The flow of the packet
     * @param id   The ID of the packet
     *
     * @return The packet associated with given flow and ID
     */
    public static @Nullable HandshakePackets getPacket(
            final @NotNull PacketBound flow,
            final int id
    ) {
        return PACKET_MAP.get(flow).get(id);
    }

    /**
     * Returns an unmodifiable map of a handshake packet flows to packet IDs
     * to packet instances
     *
     * @return An unmodifiable map of a handshake packet flows to packet IDs
     *         to packet instances
     */
    public static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<HandshakePackets>> map() {
        return Collections.unmodifiableMap(PACKET_MAP);
    }

    /**
     * Returns an unmodifiable map of a handshake packet flows to packet IDs
     * to {@link PacketType} instances
     *
     * @return An unmodifiable map of a handshake packet flows to packet IDs
     *         to {@link PacketType} instances
     */
    public static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<? extends PacketType>> boundedMap() {
        return Collections.unmodifiableMap(PACKET_MAP);
    }
}
