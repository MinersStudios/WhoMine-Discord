package com.minersstudios.whomine.api.packet.collection;

import com.minersstudios.whomine.api.packet.PacketType;
import com.minersstudios.whomine.api.utility.ResourcedPath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

final class Path2PacketMapImpl extends PacketMapImpl<ResourcedPath> implements Path2PacketMap {

    Path2PacketMapImpl(
            final @NotNull Map<ResourcedPath, PacketType> clientMap,
            final @NotNull Map<ResourcedPath, PacketType> serverMap
    ) {
        super(clientMap, serverMap);
    }

    public static final class Path2PacketBuilderImpl
            extends BuilderImpl<Path2PacketMap.Builder, Path2PacketMap, ResourcedPath>
            implements Path2PacketMap.Builder {

        @Contract("_ -> this")
        @Override
        public @NotNull Path2PacketMap.Builder add(final @NotNull PacketType packetType) {
            return this.add(packetType.getResourcedPath(), packetType);
        }

        @Contract("_ -> this")
        @Override
        public @NotNull Path2PacketMap.Builder add(final PacketType @NotNull ... packets) {
            for (final var packet : packets) {
                this.add(packet);
            }

            return this;
        }

        @Contract(" -> new")
        @Override
        public @NotNull Path2PacketMap build() {
            return new Path2PacketMapImpl(
                    this.clientMap,
                    this.serverMap
            );
        }
    }
}
