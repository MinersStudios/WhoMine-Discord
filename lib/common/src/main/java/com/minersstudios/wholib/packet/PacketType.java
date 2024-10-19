package com.minersstudios.wholib.packet;

import com.minersstudios.wholib.annotation.Path;
import com.minersstudios.wholib.packet.registry.*;
import com.minersstudios.wholib.utility.ResourcedPath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

import static com.minersstudios.wholib.packet.PacketBound.*;

/**
 * Represents a packet type used in the Minecraft server networking.
 * <p>
 * It contains information about the packet's bound, its ID, and its name.
 * <p>
 * Available packet types are defined in the following registries:
 * <ul>
 *     <li>{@link HandshakePackets}</li>
 *     <li>{@link PlayPackets}</li>
 *     <li>{@link StatusPackets}</li>
 *     <li>{@link LoginPackets}</li>
 *     <li>{@link ConfigurationPackets}</li>
 * </ul>
 *
 * To create a new packet type, use one of the following methods:
 * <ul>
 *     <li>{@link #ofMC(PacketBound, int, String)}</li>
 *     <li>{@link #of(PacketBound, int, ResourcedPath)}</li>
 * </ul>
 *
 * @see PacketBound
 * @see ResourcedPath
 */
@SuppressWarnings("unused")
@Immutable
public final class PacketType {

    private final PacketBound bound;
    private final int id;
    private final ResourcedPath resourcedPath;

    private PacketType(
            final @NotNull PacketBound bound,
            final int id,
            final @NotNull ResourcedPath resourcedPath
    ) {
        this.bound = bound;
        this.id = id;
        this.resourcedPath = resourcedPath;
    }

    /**
     * Returns the bound of the packet
     *
     * @return The bound of the packet
     */
    public @NotNull PacketBound getBound() {
        return this.bound;
    }

    /**
     * Returns the ID of the packet
     *
     * @return The ID of the packet
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the resourced-path of the packet
     *
     * @return The resourced-path of the packet
     */
    public @NotNull ResourcedPath getResourcedPath() {
        return this.resourcedPath;
    }

    /**
     * Returns whether the packet is {@link PacketBound#SERVERBOUND serverbound}
     *
     * @return True if the packet is {@link PacketBound#SERVERBOUND serverbound}
     */
    public boolean isServerbound() {
        return this.bound == SERVERBOUND;
    }

    /**
     * Returns whether the packet is {@link PacketBound#CLIENTBOUND clientbound}
     *
     * @return True if the packet is {@link PacketBound#CLIENTBOUND clientbound}
     */
    public boolean isClientbound() {
        return this.bound == CLIENTBOUND;
    }

    /**
     * Returns the hash code value for this packet type
     *
     * @return The hash code value for this packet type
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.bound.hashCode();
        result = prime * result + Integer.hashCode(this.id);
        result = prime * result + this.resourcedPath.hashCode();

        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this packet type
     *
     * @param obj The reference object with which to compare
     * @return True if this object is the same as the object argument
     */
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof PacketType that
                        && this.bound == that.getBound()
                        && this.id == that.getId()
                        && this.resourcedPath.equals(that.getResourcedPath())
                );
    }

    /**
     * Returns the string representation of this packet type
     *
     * @return The string representation of this packet type
     */
    @Override
    public @NotNull String toString() {
        return this.bound + "/" + this.resourcedPath;
    }

    /**
     * Creates a new packet type with the given bound, ID, minecraft resource,
     * and path
     *
     * @param bound The bound of the packet
     * @param id    The ID of the packet
     * @param path  The path of the packet
     * @return A new packet type with the given bound, ID, minecraft resource,
     *         and path
     * @see ResourcedPath#minecraft(String)
     * @see #of(PacketBound, int, ResourcedPath)
     */
    @Contract("_, _, _ -> new")
    public static @NotNull PacketType ofMC(
            final @NotNull PacketBound bound,
            final int id,
            final @Path @NotNull String path
    ) {
        return of(bound, id, ResourcedPath.minecraft(path));
    }

    /**
     * Creates a new packet type with the given bound, ID, and resourced-path
     *
     * @param bound         The bound of the packet
     * @param id            The ID of the packet
     * @param resourcedPath The resourced-path of the packet
     * @return A new packet type with the given bound, ID, and resourced-path
     */
    @Contract("_, _, _ -> new")
    public static @NotNull PacketType of(
            final @NotNull PacketBound bound,
            final int id,
            final @NotNull ResourcedPath resourcedPath
    ) {
        return new PacketType(bound, id, resourcedPath);
    }
}
