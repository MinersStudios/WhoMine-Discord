package com.minersstudios.whomine.api.packet.registry;

import com.minersstudios.whomine.api.packet.PacketRegistry;
import com.minersstudios.whomine.api.packet.collection.PacketMap;
import com.minersstudios.whomine.api.packet.PacketType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import static com.minersstudios.whomine.api.packet.PacketBound.*;
import static com.minersstudios.whomine.api.packet.PacketType.ofMC;

/**
 * Represents a registry of status packet types used in the Minecraft
 * server networking.
 *
 * @see PacketType
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol#Status">Protocol Wiki - Status</a>
 *
 * @version Minecraft 1.21.1, Protocol 767
 */
public final class StatusPackets {
    //<editor-fold desc="Status clientbound packets" defaultstate="collapsed">

    public static PacketType CLIENT_STATUS_RESPONSE = ofMC(CLIENTBOUND, 0x00, "status_response"); // Status Response
    public static PacketType CLIENT_PONG_RESPONSE   = ofMC(CLIENTBOUND, 0x01, "pong_response");   // Pong Response (status)

    //</editor-fold>
    //<editor-fold desc="Status serverbound packets" defaultstate="collapsed">

    public static PacketType SERVER_STATUS_REQUEST  = ofMC(SERVERBOUND, 0x00, "status_request");  // Status Request
    public static PacketType SERVER_PING_REQUEST    = ofMC(SERVERBOUND, 0x01, "ping_request");    // Ping Request (status)

    //</editor-fold>

    private static final PacketRegistry REGISTRY =
            PacketRegistry.create(
                    PacketMap.path2PacketBuilder()
                             .add(
                                     //<editor-fold desc="Clientbound packets" defaultstate="collapsed">

                                     CLIENT_STATUS_RESPONSE,
                                     CLIENT_PONG_RESPONSE,

                                     //</editor-fold>
                                     //<editor-fold desc="Serverbound packets" defaultstate="collapsed">

                                     SERVER_STATUS_REQUEST,
                                     SERVER_PING_REQUEST

                                     //</editor-fold>
                             ).build()
            );

    @Contract(" -> fail")
    private StatusPackets() throws AssertionError {
        throw new AssertionError("Registry class");
    }

    /**
     * Returns the registry of status packet types
     *
     * @return The registry of status packet types
     */
    public static @Unmodifiable @NotNull PacketRegistry registry() {
        return REGISTRY;
    }
}
