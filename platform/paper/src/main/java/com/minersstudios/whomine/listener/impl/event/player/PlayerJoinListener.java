package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.packet.ChannelInjector;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerJoinEvent.class)
public final class PlayerJoinListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.LOWEST)
    public void onPlayerJoin(final @NotNull PaperEventContainer<PlayerJoinEvent> container) {
        final PlayerJoinEvent event = container.getEvent();
        final WhoMine module = container.getModule();

        final Player player = event.getPlayer();
        final ChannelInjector injector = new ChannelInjector(
            module,
            ((CraftPlayer) player).getHandle().connection.connection
        );

        module.runTask(injector::inject);

        event.joinMessage(null);

        if (player.isDead()) {
            module.runTaskLater(() -> {
                player.spigot().respawn();
                this.handle(module, player);
            }, 8L);
        } else {
            this.handle(module, player);
        }
    }

    private void handle(
            final @NotNull WhoMine module,
            final @NotNull Player player
    ) {
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(module, player);

        playerInfo.hideNameTag();
        player.displayName(playerInfo.getDefaultName());
        module.getCache().getWorldDark()
        .teleportToDarkWorld(player)
        .thenRun(() -> module.runTaskTimer(task -> {
            if (!player.isOnline()) {
                task.cancel();
                return;
            }

            if (playerInfo.isAuthenticated()) {
                task.cancel();
                playerInfo.handleResourcePack().thenAccept(bool -> {
                    if (bool) {
                        playerInfo.handleJoin();
                    }
                });
            }
        }, 0L, 10L));
    }
}
