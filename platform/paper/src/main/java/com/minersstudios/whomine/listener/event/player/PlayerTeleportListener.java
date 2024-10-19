package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.paper.PaperCache;
import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerTeleportEvent.class)
public final class PlayerTeleportListener extends PaperEventListener {

    @CancellableHandler
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
