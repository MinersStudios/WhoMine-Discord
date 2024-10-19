package com.minersstudios.wholib.packet.collection;

import com.minersstudios.wholib.packet.PacketBound;
import com.minersstudios.wholib.packet.PacketType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.Map;

import static com.minersstudios.wholib.packet.PacketBound.CLIENTBOUND;

@Immutable
abstract class PacketMapImpl<K> implements PacketMap<K> {

    private final Map<K, PacketType> clientMap;
    private final Map<K, PacketType> serverMap;

    protected PacketMapImpl(
            final @NotNull Map<K, PacketType> clientMap,
            final @NotNull Map<K, PacketType> serverMap
    ) {
        this.clientMap = clientMap;
        this.serverMap = serverMap;
    }

    @Override
    public @NotNull @Unmodifiable Map<K, PacketType> clientDelegate() {
        return Collections.unmodifiableMap(this.clientMap);
    }

    @Override
    public @NotNull @Unmodifiable Map<K, PacketType> serverDelegate() {
        return Collections.unmodifiableMap(this.serverMap);
    }

    @Override
    public @Nullable PacketType get(
            final @NotNull PacketBound bound,
            final @NotNull K key
    ) {
        return this.forBound(bound)
                   .get(key);
    }

    @Override
    public boolean contains(
            final @NotNull PacketBound bound,
            final @NotNull K key
    ) {
        return this.forBound(bound)
                   .containsKey(key);
    }

    @Override
    public boolean contains(final @NotNull K key) {
        return this.clientMap.containsKey(key)
            || this.serverMap.containsKey(key);
    }

    @Override
    public boolean contains(final @NotNull PacketType packetType) {
        return this.forBound(packetType.getBound())
                   .containsValue(packetType);
    }

    @Override
    public int size() {
        return this.clientMap.size() + this.serverMap.size();
    }

    @Override
    public int size(final @NotNull PacketBound bound) {
        return this.forBound(bound).size();
    }

    protected @NotNull Map<K, PacketType> forBound(final @NotNull PacketBound bound) {
        return bound == CLIENTBOUND ? this.clientMap : this.serverMap;
    }

    @SuppressWarnings("unchecked")
    public static abstract class BuilderImpl<B extends PacketMap.Builder<B, M, K>, M extends PacketMap<K>, K>
            implements PacketMap.Builder<B, M, K> {

        protected final Map<K, PacketType> clientMap;
        protected final Map<K, PacketType> serverMap;

        BuilderImpl() {
            this.clientMap = new Object2ObjectOpenHashMap<>();
            this.serverMap = new Object2ObjectOpenHashMap<>();
        }

        @Override
        public @NotNull B add(
                final @NotNull K key,
                final @NotNull PacketType packetType
        ) {
            if (packetType.isClientbound()) {
                this.clientMap.put(key, packetType);
            } else {
                this.serverMap.put(key, packetType);
            }

            return (B) this;
        }
    }
}
