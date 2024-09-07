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
 * This enum represents a configuration packet type used in the Minecraft server
 * networking. It contains information about packet's {@link PacketBound flow}
 * (CLIENTBOUND or SERVERBOUND), its ID, and its name.
 *
 * @see PacketType
 * @see PacketProtocol
 * @see PacketBound
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol#Configuration">Protocol Wiki</a>
 * @version 1.21.1, protocol 767
 */
@SuppressWarnings("unused")
public enum ConfigurationPackets implements PacketType {
    //<editor-fold desc="Configuration client packets" defaultstate="collapsed">

    CLIENT_COOKIE_REQUEST                  (CLIENTBOUND, 0x00, "Cookie Request (configuration)"),
    CLIENT_PLUGIN_MESSAGE                  (CLIENTBOUND, 0x01, "Clientbound Plugin Message (configuration)"),
    CLIENT_DISCONNECT                      (CLIENTBOUND, 0x02, "Disconnect (configuration)"),
    CLIENT_FINISH_CONFIGURATION            (CLIENTBOUND, 0x03, "Finish Configuration"),
    CLIENT_KEEP_ALIVE                      (CLIENTBOUND, 0x04, "Clientbound Keep Alive (configuration)"),
    CLIENT_PING                            (CLIENTBOUND, 0x05, "Ping (configuration)"),
    CLIENT_RESET_CHAT                      (CLIENTBOUND, 0x06, "Reset Chat"),
    CLIENT_REGISTRY_DATA                   (CLIENTBOUND, 0x07, "Registry Data"),
    CLIENT_REMOVE_RESOURCE_PACK            (CLIENTBOUND, 0x08, "Remove Resource Pack (configuration)"),
    CLIENT_ADD_RESOURCE_PACK               (CLIENTBOUND, 0x09, "Add Resource Pack (configuration)"),
    CLIENT_STORE_COOKIE                    (CLIENTBOUND, 0x0A, "Store Cookie"),
    CLIENT_TRANSFER                        (CLIENTBOUND, 0x0B, "Transfer (configuration)"),
    CLIENT_FEATURE_FLAGS                   (CLIENTBOUND, 0x0C, "Feature Flags"),
    CLIENT_UPDATE_TAGS                     (CLIENTBOUND, 0x0D, "Update Tags (configuration)"),
    CLIENT_KNOWN_PACKS                     (CLIENTBOUND, 0x0E, "Clientbound Known Packs"),
    CLIENT_CUSTOM_REPORT_DETAILS           (CLIENTBOUND, 0x0F, "Custom Report Details (configuration)"),
    CLIENT_SERVER_LINKS                    (CLIENTBOUND, 0x10, "Server Links (configuration)"),

    //</editor-fold>
    //<editor-fold desc="Configuration server packets" defaultstate="collapsed">

    SERVER_CLIENT_INFORMATION              (SERVERBOUND, 0x00, "Client Information (configuration)"),
    SERVER_COOKIE_RESPONSE                 (SERVERBOUND, 0x01, "Cookie Response (configuration)"),
    SERVER_PLUGIN_MESSAGE                  (SERVERBOUND, 0x02, "Serverbound Plugin Message (configuration)"),
    SERVER_ACKNOWLEDGE_FINISH_CONFIGURATION(SERVERBOUND, 0x03, "Acknowledge Finish Configuration"),
    SERVER_KEEP_ALIVE                      (SERVERBOUND, 0x04, "Serverbound Keep Alive (configuration)"),
    SERVER_PONG                            (SERVERBOUND, 0x05, "Pong (configuration)"),
    SERVER_RESOURCE_PACK_RESPONSE          (SERVERBOUND, 0x06, "Resource Pack Response (configuration)"),
    SERVER_KNOWN_PACKS                     (SERVERBOUND, 0x07, "Serverbound Known Packs");

    //</editor-fold>

    private static final Map<PacketBound, Int2ObjectMap<ConfigurationPackets>> PACKET_MAP = PacketType.createMap(values());

    private final PacketBound flow;
    private final int id;
    private final String name;

    ConfigurationPackets(
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
    public static @Nullable ConfigurationPackets getPacket(
            final @NotNull PacketBound flow,
            final int id
    ) {
        return PACKET_MAP.get(flow).get(id);
    }

    /**
     * Returns an unmodifiable map of a configuration packet flows to packet IDs
     * to packet instances
     *
     * @return An unmodifiable map of a configuration packet flows to packet IDs
     *         to packet instances
     */
    public static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<ConfigurationPackets>> map() {
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
