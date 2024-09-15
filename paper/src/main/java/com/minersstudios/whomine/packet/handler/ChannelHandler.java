package com.minersstudios.whomine.packet.handler;

import com.minersstudios.whomine.api.packet.*;
import com.minersstudios.whomine.packet.PaperPacketContainer;
import com.minersstudios.whomine.packet.PaperPacketEvent;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.WhoMine;
import io.netty.channel.*;
import net.kyori.adventure.text.Component;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

/**
 * The ChannelHandler class is responsible for handling incoming and outgoing
 * packets in the Minecraft server networking pipeline.
 * <br>
 * It extends {@link ChannelDuplexHandler}, which allows handling of both
 * inbound and outbound data.
 *
 * @see PacketType
 * @see PacketContainer
 * @see PacketEvent
 */
@Immutable
public final class ChannelHandler extends ChannelDuplexHandler {

    private final WhoMine plugin;
    private final Connection connection;

    /**
     * Channel handler constructor
     *
     * @param plugin     The plugin associated with this channel handler
     * @param connection The connection associated with this channel handler
     */
    public ChannelHandler(
            final @NotNull WhoMine plugin,
            final @NotNull Connection connection
    ) {
        this.plugin = plugin;
        this.connection = connection;
    }

    /**
     * @return The plugin associated with this channel handler
     */
    public @NotNull WhoMine getPlugin() {
        return this.plugin;
    }

    /**
     * @return The connection associated with this channel handler
     */
    public @NotNull Connection getConnection() {
        return this.connection;
    }

    /**
     * This method is called when a packet is received from the client. It
     * processes the packet, creates a {@link PacketContainer}, and fires a
     * {@link PacketEvent}. If the event is not cancelled, the packet is passed
     * to the next channel handler in the pipeline.
     *
     * @param ctx The ChannelHandlerContext
     * @param msg The received packet
     * @throws Exception If an error occurs while processing the packet
     */
    @Override
    public void channelRead(
            final @NotNull ChannelHandlerContext ctx,
            final @NotNull Object msg
    ) throws Exception {
        final var event = this.handle(msg);

        if (event != null) {
            this.plugin.getListenerManager().callPacketReceiveEvent(event);

            if (!event.isCancelled()) {
                super.channelRead(ctx, event.getPacketContainer().getPacket());
            }
        }
    }

    /**
     * This method is called when a packet is about to be sent to the client.
     * It processes the packet, creates a {@link PacketContainer}, and fires a
     * {@link PacketEvent}. If the event is not cancelled, the packet is passed
     * to the next channel handler in the pipeline.
     *
     * @param ctx     The ChannelHandlerContext
     * @param msg     The packet to be sent
     * @param promise The ChannelPromise
     * @throws Exception If an error occurs while processing the packet
     */
    @Override
    public void write(
            final @NotNull ChannelHandlerContext ctx,
            final @NotNull Object msg,
            final @NotNull ChannelPromise promise
    ) throws Exception {
        final var event = this.handle(msg);

        if (event != null) {
            this.plugin.getListenerManager().callPacketSendEvent(event);

            if (!event.isCancelled()) {
                super.write(ctx, event.getPacketContainer().getPacket(), promise);
            }
        }
    }

    private @Nullable PaperPacketEvent handle(final @NotNull Object msg) {
        if (!(msg instanceof final Packet<?> packet)) {
            return null;
        }

        final var nmsType = packet.type();
        final PacketListener packetListener = this.connection.getPacketListener();

        if (packetListener == null) {
            this.kick(
                    Component.text("No packet listener found"),
                    Component.text("No packet listener found for " + this.connection.getPlayer().getName() + " with packet type: " + nmsType)
            );

            return null;
        }

        final PacketType packetType =
                PacketProtocol.byOrdinal(packetListener.protocol().ordinal())
                              .getRegistry()
                              .byClass()
                              .get(
                                      PacketBound.fromId(nmsType.flow().id()),
                                      packet.getClass()
                              );

        if (packetType == null) {
            this.kick(
                    Component.text("Unknown packet type: " + nmsType),
                    Component.text("Unknown packet type: " + nmsType + " sent by " + this.connection.getPlayer().getName())
            );

            return null;
        }

        return new PaperPacketEvent(
                new PaperPacketContainer(packet, packetType),
                this.connection
        );
    }

    private void kick(
            final @NotNull Component playerKickMessage,
            final @NotNull Component consoleKickMessage
    ) {
        final ServerPlayer serverPlayer = this.connection.getPlayer();

        this.plugin.runTask(() -> serverPlayer.connection.disconnect(
                playerKickMessage,
                PlayerKickEvent.Cause.PLUGIN
        ));
        MSLogger.severe(consoleKickMessage);
    }
}
