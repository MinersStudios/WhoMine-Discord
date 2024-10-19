package com.minersstudios.whomine.listener.event.player;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerStopSpectatingEntityEvent.class)
public final class PlayerStopSpectatingEntityListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerStopSpectatingEntity(final @NotNull PaperEventContainer<PlayerStopSpectatingEntityEvent> container) {
        final PlayerStopSpectatingEntityEvent event = container.getEvent();

        if (container.getModule().getCache().getWorldDark().isInWorldDark(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
