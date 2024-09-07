package com.minersstudios.whomine.packet;

import com.minersstudios.whomine.api.packet.PacketContainer;
import com.minersstudios.whomine.api.packet.PacketEvent;
import com.minersstudios.whomine.api.packet.type.PacketType;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.WhoMine;
import io.netty.channel.*;
import net.kyori.adventure.text.Component;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

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
    public static final String CHANNEL_HANDLER_NAME = "ms_channel_handler";
    public static final String PACKET_HANDLER_NAME  = "packet_handler";

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
        if (!(msg instanceof final Packet<?> packet)) {
            return;
        }

        final var nmsType = packet.type();
        final PacketType packetType = PacketType.fromId(nmsType.id().getPath());

        if (packetType == null) {
            final ServerPlayer serverPlayer = this.connection.getPlayer();

            this.plugin.runTask(() -> serverPlayer.connection.disconnect(
                Component.text("Unknown packet type: " + nmsType),
                PlayerKickEvent.Cause.PLUGIN
            ));
            MSLogger.severe("Unknown packet type: " + nmsType + " sent by " + serverPlayer.getName());

            return;
        }

        final var packetContainer = new PaperPacketContainer(packet, packetType);
        final var event = new PaperPacketEvent(packetContainer, this.connection);

        this.plugin.getListenerManager().callPacketReceiveEvent(event);

        if (!event.isCancelled()) {
            super.channelRead(ctx, event.getPacketContainer().getPacket());
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
        if (!(msg instanceof final Packet<?> packet)) {
            return;
        }

        final var nmsType = packet.type();
        final PacketType packetType = PacketType.fromId(nmsType.id().getPath());

        if (packetType == null) {
            final ServerPlayer serverPlayer = this.connection.getPlayer();

            this.plugin.runTask(() -> serverPlayer.connection.disconnect(
                Component.text("Unknown packet type: " + nmsType),
                PlayerKickEvent.Cause.PLUGIN
            ));
            MSLogger.severe("Unknown packet type: " + nmsType + " sent to " + serverPlayer.getName());

            return;
        }

        final var packetContainer = new PaperPacketContainer(packet, packetType);
        final var event = new PaperPacketEvent(packetContainer, this.connection);

        this.plugin.getListenerManager().callPacketSendEvent(event);

        if (!event.isCancelled()) {
            super.write(ctx, event.getPacketContainer().getPacket(), promise);
        }
    }

    /**
     * Injects the {@link ChannelHandler} for a specific player into the server
     * networking pipeline
     *
     * @param connection The connection to inject the ChannelHandler for
     * @param plugin     The MSPlugin instance associated with the ChannelHandler
     */
    public static void injectConnection(
            final @NotNull Connection connection,
            final @NotNull WhoMine plugin
    ) {
        final ChannelPipeline pipeline = connection.channel.pipeline();

        if (!pipeline.names().contains(ChannelHandler.CHANNEL_HANDLER_NAME)) {
            pipeline.addBefore(
                    ChannelHandler.PACKET_HANDLER_NAME,
                    ChannelHandler.CHANNEL_HANDLER_NAME,
                    new ChannelHandler(plugin, connection)
            );
        }
    }

    /**
     * Removes the {@link ChannelHandler} from a specific player in the server
     * networking pipeline
     *
     * @param connection The connection to remove the ChannelHandler for
     */
    public static void uninjectConnection(final @NotNull Connection connection) {
        final Channel channel = connection.channel;
        final ChannelPipeline pipeline = channel.pipeline();

        if (pipeline.names().contains(CHANNEL_HANDLER_NAME)) {
            try (final var eventLoop = channel.eventLoop()) {
                eventLoop.execute(() -> pipeline.remove(CHANNEL_HANDLER_NAME));
            }
        }
    }
}
