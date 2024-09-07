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

import static com.minersstudios.whomine.api.packet.PacketBound.CLIENTBOUND;
import static com.minersstudios.whomine.api.packet.PacketBound.SERVERBOUND;

/**
 * This enum represents a login packet type used in the Minecraft server
 * networking. It contains information about packet's {@link PacketBound flow}
 * (CLIENTBOUND or SERVERBOUND), its ID, and its name.
 *
 * @see PacketType
 * @see PacketProtocol
 * @see PacketBound
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol#Login">Protocol Wiki</a>
 * @version 1.21.1, protocol 767
 */
@SuppressWarnings("unused")
public enum LoginPackets implements PacketType {
    //<editor-fold desc="Login client packets" defaultstate="collapsed">

    CLIENT_DISCONNECT                               (CLIENTBOUND, 0x00, "Disconnect (login)"),
    CLIENT_ENCRYPTION_REQUEST                       (CLIENTBOUND, 0x01, "Encryption Request"),
    CLIENT_LOGIN_SUCCESS                            (CLIENTBOUND, 0x02, "Login Success"),
    CLIENT_SET_COMPRESSION                          (CLIENTBOUND, 0x03, "Set Compression"),
    CLIENT_LOGIN_PLUGIN_REQUEST                     (CLIENTBOUND, 0x04, "Login Plugin Request"),
    CLIENT_COOKIE_REQUEST                           (CLIENTBOUND, 0x05, "Cookie Request (login)"),

    //</editor-fold>
    //<editor-fold desc="Login server packets" defaultstate="collapsed">

    SERVER_LOGIN_START                              (SERVERBOUND, 0x00, "Login Start"),
    SERVER_ENCRYPTION_RESPONSE                      (SERVERBOUND, 0x01, "Encryption Response"),
    SERVER_LOGIN_PLUGIN_RESPONSE                    (SERVERBOUND, 0x02, "Login Plugin Response"),
    SERVER_LOGIN_ACKNOWLEDGED                       (SERVERBOUND, 0x03, "Login Acknowledged"),
    SERVER_COOKIE_RESPONSE                          (SERVERBOUND, 0x04, "Cookie Response (login)");

    //</editor-fold>

    private static final Map<PacketBound, Int2ObjectMap<LoginPackets>> PACKET_MAP = PacketType.createMap(values());

    private final PacketBound flow;
    private final int id;
    private final String name;

    LoginPackets(
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
    public static @Nullable LoginPackets getPacket(
            final @NotNull PacketBound flow,
            final int id
    ) {
        return PACKET_MAP.get(flow).get(id);
    }

    /**
     * Returns an unmodifiable map of a login packet flows to packet IDs to
     * packet instances
     *
     * @return An unmodifiable map of a login packet flows to packet IDs to
     *         packet instances
     */
    public static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<LoginPackets>> map() {
        return Collections.unmodifiableMap(PACKET_MAP);
    }

    /**
     * Returns an unmodifiable map of a configuration packet flows to packet IDs
     * to {@link PacketType} instances
     *
     * @return An unmodifiable map of a configuration packet flows to packet IDs
     *         to {@link PacketType} instances
     */
    public static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<? extends PacketType>> boundedMap() {
        return Collections.unmodifiableMap(PACKET_MAP);
    }
}
