package com.minersstudios.whomine.api.packet;

import com.minersstudios.whomine.api.packet.type.PacketType;
import com.minersstudios.whomine.api.packet.type.PlayPackets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a packet registry for Minecraft packets. This class maps packet
 * classes to their corresponding {@link PacketType} and vice versa.
 *
 * @see PacketType
 * @see PacketProtocol
 * @see PacketBound
 * @see <a href="https://wiki.vg/Protocol">Protocol Wiki</a>
 */
@SuppressWarnings("unused")
public final class PacketRegistry {
    private static volatile PacketRegistry singleton;

    private final Map<String, PacketType> idToType;
    private final Int2ObjectMap<String> ordinalToId;

    private PacketRegistry() {
        this.idToType = new Object2ObjectOpenHashMap<>();
        this.ordinalToId = new Int2ObjectOpenHashMap<>();
    }

    /**
     * Initialize the packet registry with the given map of packet classes to their
     * corresponding {@link PacketType}
     *
     * @param idToType The map containing packet ids as keys and their
     *                 corresponding {@link PacketType} as values
     * @throws IllegalStateException If the packet registry has already been
     *                               initialized
     */
    public static void init(final @NotNull Map<String, PacketType> idToType) throws IllegalStateException {
        if (singleton != null) {
            throw new IllegalStateException("PacketRegistry has already been initialized");
        }

        singleton = new PacketRegistry();

        for (final var entry : idToType.entrySet()) {
            final var packetType = entry.getValue();
            final var packetId = entry.getKey();

            singleton.idToType.put(packetId, packetType);
            singleton.ordinalToId.put(packetType.ordinal(), packetId);
        }
    }

    /**
     * Get an unmodifiable view of the map that maps packet classes to their
     * corresponding {@link PacketType}
     *
     * @return An unmodifiable view of the map containing packet classes as keys
     *         and their corresponding {@link PacketType} as values
     */
    public static @NotNull @UnmodifiableView Map<String, PacketType> idToType() {
        return Collections.unmodifiableMap(singleton.idToType);
    }

    /**
     * Get an unmodifiable view of the map that maps {@link PacketType} ordinal
     * to their corresponding packet classes
     *
     * @return An unmodifiable view of the map containing {@link PacketType}
     *         ordinal as keys and their corresponding packet classes as values
     */
    public static @NotNull @UnmodifiableView Map<Integer, String> ordinalToId() {
        return Collections.unmodifiableMap(singleton.ordinalToId);
    }

    public static @Nullable PacketType getTypeFromId(final @NotNull String packetId) {
        return "bundle".equals(packetId)
               ? PlayPackets.CLIENT_BUNDLE_DELIMITER
               : singleton.idToType.get(packetId);
    }

    public static @NotNull String getIdFromType(final @NotNull PacketType type) {
        return singleton.ordinalToId.get(type.ordinal());
    }
}
