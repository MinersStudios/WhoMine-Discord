package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@ListenFor(PlayerSpawnLocationEvent.class)
public final class PlayerSpawnLocationListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerSpawnLocation(final @NotNull PaperEventContainer<PlayerSpawnLocationEvent> container) {
        final PlayerSpawnLocationEvent event = container.getEvent();

        if (!event.getPlayer().isDead()) {
            event.setSpawnLocation(container.getModule().getCache().getWorldDark().getSpawnLocation());
        }
    }
}
