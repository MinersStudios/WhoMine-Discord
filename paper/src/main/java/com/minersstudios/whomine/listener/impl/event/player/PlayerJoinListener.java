package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.packet.ChannelHandler;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerJoinListener extends EventListener {

    public PlayerJoinListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        final WhoMine plugin = this.getPlugin();
        final Player player = event.getPlayer();

        plugin.runTask(() ->
            ChannelHandler.injectConnection(
                ((CraftPlayer) event.getPlayer()).getHandle().connection.connection,
                plugin
            )
        );

        event.joinMessage(null);

        if (player.isDead()) {
            this.getPlugin().runTaskLater(() -> {
                player.spigot().respawn();
                this.handle(player);
            }, 8L);
        } else {
            this.handle(player);
        }
    }

    private void handle(final @NotNull Player player) {
        final WhoMine plugin = this.getPlugin();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);

        playerInfo.hideNameTag();
        player.displayName(playerInfo.getDefaultName());
        plugin.getCache().getWorldDark()
        .teleportToDarkWorld(player)
        .thenRun(() -> plugin.runTaskTimer(task -> {
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
