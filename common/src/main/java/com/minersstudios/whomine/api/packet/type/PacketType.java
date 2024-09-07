package com.minersstudios.whomine.api.packet.type;

import com.minersstudios.whomine.api.packet.PacketBound;
import com.minersstudios.whomine.api.packet.PacketProtocol;
import com.minersstudios.whomine.api.packet.PacketRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.EnumMap;
import java.util.Map;

import static com.minersstudios.whomine.api.packet.PacketBound.CLIENTBOUND;
import static com.minersstudios.whomine.api.packet.PacketBound.SERVERBOUND;

/**
 * This class represents a packet type used in the Minecraft server networking.
 * It contains information about the packet's flow (CLIENTBOUND or SERVERBOUND),
 * its ID, and its name.
 * <p>
 * Available packet types are defined in the following enums:
 * <ul>
 *     <li>{@link HandshakePackets}</li>
 *     <li>{@link StatusPackets}</li>
 *     <li>{@link LoginPackets}</li>
 *     <li>{@link ConfigurationPackets}</li>
 *     <li>{@link PlayPackets}</li>
 * </ul>
 *
 * @see PacketProtocol
 * @see PacketBound
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol">Protocol Wiki</a>
 * @version 1.21.1, protocol 767
 */
@SuppressWarnings("unused")
public interface PacketType {

    String name();

    int ordinal();

    /**
     * Returns the flow of the packet (CLIENTBOUND or SERVERBOUND)
     *
     * @return The flow of the packet (CLIENTBOUND or SERVERBOUND)
     */
    @NotNull PacketBound getFlow();

    /**
     * Returns the ID of the packet
     *
     * @return The ID of the packet
     */
    int getId();

    /**
     * Returns the name of the packet
     *
     * @return The name of the packet
     */
    @NotNull String getName();

    /**
     * Returns whether the packet is {@link PacketBound#SERVERBOUND serverbound}
     *
     * @return True if the packet is {@link PacketBound#SERVERBOUND serverbound},
     *         false otherwise
     */
    default boolean isReceive() {
        return this.getFlow() == SERVERBOUND;
    }

    /**
     * Returns whether the packet is {@link PacketBound#CLIENTBOUND clientbound}
     *
     * @return True if the packet is {@link PacketBound#CLIENTBOUND clientbound},
     *         false otherwise
     */
    default boolean isSend() {
        return this.getFlow() == CLIENTBOUND;
    }

    /**
     * Returns the string representation of this packet type
     *
     * @return The string representation of this packet type
     */
    @Override
    @NotNull String toString();

    /**
     * Returns an unmodifiable map of a handshaking packet flows to packet IDs
     * to {@link HandshakePackets packet} instances
     *
     * @return An unmodifiable map of a handshaking packet flows to packet IDs
     *         to {@link HandshakePackets packet} instances
     */
    static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<HandshakePackets>> handshake() {
        return HandshakePackets.map();
    }

    /**
     * Returns an unmodifiable map of a status packet flows to packet IDs to
     * {@link StatusPackets packet} instances
     *
     * @return An unmodifiable map of a status packet flows to packet IDs to
     *         {@link StatusPackets packet} instances
     */
    static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<StatusPackets>> status() {
        return StatusPackets.map();
    }

    /**
     * Returns an unmodifiable map of a login packet flows to packet IDs to
     * {@link LoginPackets packet} instances
     *
     * @return An unmodifiable map of a login packet flows to packet IDs to
     *         {@link LoginPackets packet} instances
     */
    static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<LoginPackets>> login() {
        return LoginPackets.map();
    }

    /**
     * Returns an unmodifiable map of a configuration packet flows to packet IDs
     * to {@link ConfigurationPackets packet} instances
     *
     * @return An unmodifiable map of a configuration packet flows to packet IDs
     *         to {@link ConfigurationPackets packet} instances
     */
    static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<ConfigurationPackets>> configuration() {
        return ConfigurationPackets.map();
    }

    /**
     * Returns an unmodifiable map of a play packet flows to packet IDs to
     * {@link PlayPackets packet} instances
     *
     * @return An unmodifiable map of a play packet flows to packet IDs to
     *         {@link PlayPackets packet} instances
     */
    static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<PlayPackets>> play() {
        return PlayPackets.map();
    }

    static @Nullable PacketType fromId(final @NotNull String id) {
        return PacketRegistry.getTypeFromId(id);
    }

    /**
     * Creates a map of packet flows to packet IDs to its corresponding
     * {@link PacketType} instances
     *
     * @param packets The array of packet types to map
     * @param <T>     The type of packet
     * @return A map of packet flows to packet IDs to PacketType instances
     */
    static <T extends PacketType> @NotNull Map<PacketBound, Int2ObjectMap<T>> createMap(final T @NotNull [] packets) {
        return createMap(packets, "CLIENT_", "SERVER_");
    }

    /**
     * Creates a map of packet flows to packet IDs to its corresponding
     * {@link PacketType} instances
     *
     * @param packets           The array of packets
     * @param clientboundPrefix The prefix of the clientbound packet names
     * @param serverboundPrefix The prefix of the serverbound packet names
     * @param <T>               The type of packet
     * @return A map of packet flows to packet IDs to PacketType instances
     */
    static <T extends PacketType> @NotNull Map<PacketBound, Int2ObjectMap<T>> createMap(
            final T @NotNull [] packets,
            final @NotNull String clientboundPrefix,
            final @NotNull String serverboundPrefix
    ) {
        final var clientboundMap = new Int2ObjectOpenHashMap<T>();
        final var serverboundMap = new Int2ObjectOpenHashMap<T>();

        for (final var packet : packets) {
            final String name = packet.name();

            if (name.startsWith(clientboundPrefix)) {
                clientboundMap.put(packet.getId(), packet);
            } else if (name.startsWith(serverboundPrefix)) {
                serverboundMap.put(packet.getId(), packet);
            }
        }

        final var flowMap = new EnumMap<PacketBound, Int2ObjectMap<T>>(PacketBound.class);

        flowMap.put(CLIENTBOUND, clientboundMap);
        flowMap.put(SERVERBOUND, serverboundMap);

        return flowMap;
    }
}
