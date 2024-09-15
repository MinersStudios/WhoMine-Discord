package com.minersstudios.whomine.packet;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.packet.handler.ChannelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.Connection;
import net.minecraft.network.HandlerNames;

public class ChannelInjector {
    public static final String CHANNEL_HANDLER_NAME = "ms_channel_handler";

    private final WhoMine plugin;
    private final Connection connection;

    public ChannelInjector(
            final WhoMine plugin,
            final Connection connection
    ) {
        this.plugin = plugin;
        this.connection = connection;
    }

    public void inject() {
        final ChannelPipeline pipeline = this.connection.channel.pipeline();

        if (!pipeline.names().contains(CHANNEL_HANDLER_NAME)) {
            pipeline.addBefore(
                    HandlerNames.PACKET_HANDLER,
                    CHANNEL_HANDLER_NAME,
                    new ChannelHandler(this.plugin, this.connection)
            );
        }
    }

    public void uninject() {
        final Channel channel = this.connection.channel;
        final ChannelPipeline pipeline = channel.pipeline();

        if (pipeline.names().contains(CHANNEL_HANDLER_NAME)) {
            try (final var eventLoop = channel.eventLoop()) {
                eventLoop.execute(() -> pipeline.remove(CHANNEL_HANDLER_NAME));
            }
        }
    }
}
