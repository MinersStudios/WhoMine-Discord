package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
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
