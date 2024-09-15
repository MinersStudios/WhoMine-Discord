package com.minersstudios.whomine.listener.impl.packet.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.packet.registry.PlayPackets;
import com.minersstudios.whomine.listener.api.PacketListener;
import com.minersstudios.whomine.packet.PaperPacketEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class SwingArmListener extends PacketListener {
    private Map<String, CompletableFuture<Block>> clickRequestMap;

    public SwingArmListener(final @NotNull WhoMine plugin) {
        super(plugin, PlayPackets.SERVER_SWING_ARM);
    }

    @Override
    public void onPacketReceive(final @NotNull PaperPacketEvent event) {
        if (this.clickRequestMap == null) {
            this.setupClickRequestMap();
        }

        if (this.clickRequestMap.isEmpty()) {
            return;
        }

        final ServerPlayer serverPlayer = event.getConnection().getPlayer();

        if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL) {
            final String uuid = serverPlayer.getStringUUID();
            final var future = this.clickRequestMap.get(uuid);

            if (future == null) {
                return;
            }

            this.getPlugin().runTask(() -> {
                final Block targetBlock = PlayerActionListener.getTargetBlock(serverPlayer);

                if (targetBlock != null) {
                    future.complete(targetBlock);
                    this.clickRequestMap.remove(uuid);
                }
            });
        }
    }

    private void setupClickRequestMap() {
        for (final var listener : this.getPlugin().getListenerManager().packetListeners()) {
            if (listener instanceof final PlayerActionListener playerActionListener) {
                this.clickRequestMap = playerActionListener.clickRequestMap;
                break;
            }
        }
    }
}
