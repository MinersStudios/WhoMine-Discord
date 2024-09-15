package com.minersstudios.whomine.api.packet.collection;

import com.minersstudios.whomine.api.packet.PacketType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

final class Class2PacketMapImpl extends PacketMapImpl<Class<?>> implements Class2PacketMap {

    Class2PacketMapImpl(
            final @NotNull Map<Class<?>, PacketType> clientMap,
            final @NotNull Map<Class<?>, PacketType> serverMap
    ) {
        super(clientMap, serverMap);
    }

    @ApiStatus.Internal
    @Contract("_, _ -> this")
    @Override
    public synchronized @NotNull Class2PacketMap put(
            final @NotNull Class<?> key,
            final @NotNull PacketType packet
    ) {
        this.forBound(packet.getBound())
            .put(key, packet);

        return this;
    }

    public static final class Builder
            extends PacketMapImpl.Builder<Class2PacketMap.Builder, Class2PacketMap, Class<?>>
            implements Class2PacketMap.Builder {

        @Contract(" -> new")
        @Override
        public @NotNull Class2PacketMap build() {
            return new Class2PacketMapImpl(
                    this.clientMap,
                    this.serverMap
            );
        }
    }
}
