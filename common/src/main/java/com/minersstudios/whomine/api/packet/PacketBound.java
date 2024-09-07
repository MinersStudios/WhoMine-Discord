package com.minersstudios.whomine.api.packet;

import com.minersstudios.whomine.api.packet.type.PacketType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * This enum represents a packet's flow direction in the Minecraft server
 * networking
 *
 * @see PacketType
 * @see PacketProtocol
 * @see PacketRegistry
 */
public enum PacketBound {
    SERVERBOUND("serverbound"),
    CLIENTBOUND("clientbound");

    private final String id;

    PacketBound(final @NotNull String id) {
        this.id = id;
    }

    /**
     * Returns the ID of the flow direction
     *
     * @return The ID of the flow direction
     */
    public @NotNull String getId() {
        return this.id;
    }

    /**
     * Returns the opposite flow direction
     *
     * @return The opposite flow direction
     */
    public @NotNull PacketBound getOpposite() {
        return this == CLIENTBOUND
               ? SERVERBOUND
               : CLIENTBOUND;
    }

    /**
     * Returns the flow direction from the given ID
     *
     * @param id The ID of the flow direction
     * @return The flow direction from the given ID
     * @throws IllegalArgumentException If the ID is unknown
     */
    public static @NotNull PacketBound fromId(final @NotNull String id) throws IllegalArgumentException {
        return switch (id.toLowerCase(Locale.ENGLISH)) {
            case "serverbound" -> SERVERBOUND;
            case "clientbound" -> CLIENTBOUND;
            default -> throw new IllegalArgumentException("Unknown packet flow: " + id);
        };
    }
}
