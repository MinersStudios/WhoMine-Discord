package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.PaperCache;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerTeleportEvent.class)
public final class PlayerTeleportListener extends PaperEventListener {

    @EventHandler
    public void onPlayerTeleport(final @NotNull PaperEventContainer<PlayerTeleportEvent> container) {
        final PlayerTeleportEvent event = container.getEvent();
        final WhoMine module = container.getModule();

        final Player player = event.getPlayer();
        final PaperCache cache = module.getCache();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(module, player);

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
