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
 * Represents a registry of login packet types used in the Minecraft
 * server networking.
 *
 * @see PacketType
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol#Login">Protocol Wiki - Login</a>
 *
 * @version Minecraft 1.21.1, Protocol 767
 */
public final class LoginPackets {
    //<editor-fold desc="Login clientbound packets" defaultstate="collapsed">

    public static PacketType CLIENT_DISCONNECT            = ofMC(CLIENTBOUND, 0x00, "login_disconnect");     // Disconnect (login)
    public static PacketType CLIENT_ENCRYPTION_REQUEST    = ofMC(CLIENTBOUND, 0x01, "hello");                // Encryption Request
    public static PacketType CLIENT_LOGIN_SUCCESS         = ofMC(CLIENTBOUND, 0x02, "game_profile");         // Login Success
    public static PacketType CLIENT_SET_COMPRESSION       = ofMC(CLIENTBOUND, 0x03, "login_compression");    // Set Compression
    public static PacketType CLIENT_LOGIN_PLUGIN_REQUEST  = ofMC(CLIENTBOUND, 0x04, "custom_query");         // Login Plugin Request
    public static PacketType CLIENT_COOKIE_REQUEST        = ofMC(CLIENTBOUND, 0x05, "cookie_request");       // Cookie Request (login)

    //</editor-fold>
    //<editor-fold desc="Login serverbound packets" defaultstate="collapsed>

    public static PacketType SERVER_LOGIN_START           = ofMC(SERVERBOUND, 0x00, "hello");                // Login Start
    public static PacketType SERVER_ENCRYPTION_RESPONSE   = ofMC(SERVERBOUND, 0x01, "key");                  // Encryption Response
    public static PacketType SERVER_LOGIN_PLUGIN_RESPONSE = ofMC(SERVERBOUND, 0x02, "custom_query_answer");  // Login Plugin Response
    public static PacketType SERVER_LOGIN_ACKNOWLEDGED    = ofMC(SERVERBOUND, 0x03, "login_acknowledged");   // Login Acknowledged
    public static PacketType SERVER_COOKIE_RESPONSE       = ofMC(SERVERBOUND, 0x04, "cookie_response");      // Cookie Response (login)

    //</editor-fold>

    private static final PacketRegistry REGISTRY =
            PacketRegistry.create(
                    PacketMap.path2PacketBuilder()
                             .add(
                                     //<editor-fold desc="Clientbound packets" defaultstate="collapsed">

                                     CLIENT_DISCONNECT,
                                     CLIENT_ENCRYPTION_REQUEST,
                                     CLIENT_LOGIN_SUCCESS,
                                     CLIENT_SET_COMPRESSION,
                                     CLIENT_LOGIN_PLUGIN_REQUEST,
                                     CLIENT_COOKIE_REQUEST,

                                     //</editor-fold>
                                     //<editor-fold desc="Serverbound packets" defaultstate="collapsed">

                                     SERVER_LOGIN_START,
                                     SERVER_ENCRYPTION_RESPONSE,
                                     SERVER_LOGIN_PLUGIN_RESPONSE,
                                     SERVER_LOGIN_ACKNOWLEDGED,
                                     SERVER_COOKIE_RESPONSE

                                     //</editor-fold>
                             ).build()
            );

    @Contract(" -> fail")
    private LoginPackets() throws AssertionError {
        throw new AssertionError("Registry class");
    }

    /**
     * Returns the registry of login packet types
     *
     * @return The registry of login packet types
     */
    public static @Unmodifiable @NotNull PacketRegistry registry() {
        return REGISTRY;
    }
}
