package com.minersstudios.whomine.listener.impl.packet.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import com.minersstudios.whomine.api.packet.registry.PlayPackets;
import com.minersstudios.whomine.packet.PaperPacketContainer;
import com.minersstudios.whomine.packet.PaperPacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class SwingArmListener extends PaperPacketListener {

    private Map<String, CompletableFuture<Block>> clickRequestMap;

    public SwingArmListener() {
        super(PlayPackets.SERVER_SWING_ARM);
    }

    @CancellableHandler
    public void onEvent(final @NotNull PaperPacketContainer container) {
        final WhoMine module = container.getModule();

        if (this.clickRequestMap == null) {
            this.setupClickRequestMap(module);
        }

        final ServerPlayer serverPlayer = container.getEvent().getConnection().getPlayer();

        container.getEvent().setCancelled(true);

        if (this.clickRequestMap.isEmpty()) {
            return;
        }

        if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL) {
            final String uuid = serverPlayer.getStringUUID();
            final var future = this.clickRequestMap.get(uuid);

            if (future == null) {
                return;
            }

            module.runTask(() -> {
                final Block targetBlock = PlayerActionListener.getTargetBlock(serverPlayer);

                if (targetBlock != null) {
                    future.complete(targetBlock);
                    this.clickRequestMap.remove(uuid);
                }
            });
        }
    }

    private void setupClickRequestMap(final @NotNull WhoMine module) {
        final var listeners = module.getListenerManager().getPacketListeners(PlayPackets.SERVER_PLAYER_ACTION);

        if (listeners == null) {
            return;
        }

        for (final var listener : listeners) {
            if (listener instanceof final PlayerActionListener playerActionListener) {
                this.clickRequestMap = playerActionListener.clickRequestMap;

                break;
            }
        }
    }
}
