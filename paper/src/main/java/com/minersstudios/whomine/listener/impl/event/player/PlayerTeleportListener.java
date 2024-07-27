package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.Cache;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerTeleportListener extends EventListener {

    public PlayerTeleportListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerTeleport(final @NotNull PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final Cache cache = this.getPlugin().getCache();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getPlugin(), player);

        cache.getDiggingMap().removeAll(player);
        cache.getStepMap().put(player, 0.0d);

        if (playerInfo.isSitting()) {
            playerInfo.unsetSitting();
        }

        if (
                event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE
                && playerInfo.isInWorldDark()
        ) {
            event.setCancelled(true);
        }
    }
}
