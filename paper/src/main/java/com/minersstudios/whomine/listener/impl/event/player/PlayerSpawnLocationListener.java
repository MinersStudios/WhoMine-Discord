package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@ListenFor(eventClass = PlayerSpawnLocationEvent.class)
public final class PlayerSpawnLocationListener extends PaperEventListener {

    @EventHandler
    public void onPlayerSpawnLocation(final @NotNull PaperEventContainer<PlayerSpawnLocationEvent> container) {
        final PlayerSpawnLocationEvent event = container.getEvent();

        if (!event.getPlayer().isDead()) {
            event.setSpawnLocation(container.getModule().getCache().getWorldDark().getSpawnLocation());
        }
    }
}
