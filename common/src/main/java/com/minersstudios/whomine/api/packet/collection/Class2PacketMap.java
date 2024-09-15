package com.minersstudios.whomine.api.packet.collection;

import com.minersstudios.whomine.api.packet.PacketType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a map of {@link Class classes} to {@link PacketType packet types}.
 * <p>
 * This map is mutable and can be modified after creation by using the internal
 * {@link #put(Class, PacketType)} method. It is only present for internal use,
 * such as when registering packet classes for Bukkit servers.
 * <p>
 * To create a new instance of this map, use the following method:
 * {@link #builder()}
 *
 * @see PacketMap
 */
public interface Class2PacketMap extends PacketMap<Class<?>> {

    /**
     * Puts a packet into the map
     *
     * @param key    The key of the packet
     * @param packet The packet to put
     * @return This map for chaining
     */
    @ApiStatus.Internal
    @Contract("_, _ -> this")
    @NotNull Class2PacketMap put(
            final @NotNull Class<?> key,
            final @NotNull PacketType packet
    );

    /**
     * Creates a new class to packet map builder
     *
     * @return A new class to packet map builder
     */
    @Contract(" -> new")
    static @NotNull Builder builder() {
        return new Class2PacketMapImpl.Class2PacketBuilderImpl();
    }

    /**
     * Represents a builder for a {@link Class2PacketMap class to packet map}.
     * <p>
     * To create a new instance of this builder, use the following method:
     * {@link Class2PacketMap#builder()}
     *
     * @see PacketMap.Builder
     */
    interface Builder extends PacketMap.Builder<Builder, Class2PacketMap, Class<?>> {}
}
