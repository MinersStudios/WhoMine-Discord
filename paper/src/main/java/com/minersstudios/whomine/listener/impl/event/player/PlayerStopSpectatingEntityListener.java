package com.minersstudios.whomine.listener.impl.event.player;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.EventHandler;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerStopSpectatingEntityEvent.class)
public final class PlayerStopSpectatingEntityListener extends PaperEventListener {

    @EventHandler
    public void onPlayerStopSpectatingEntity(final @NotNull PaperEventContainer<PlayerStopSpectatingEntityEvent> container) {
        final PlayerStopSpectatingEntityEvent event = container.getEvent();

        if (container.getModule().getCache().getWorldDark().isInWorldDark(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
