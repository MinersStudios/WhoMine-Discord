package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerDropItemEvent.class)
public final class PlayerDropItemListener extends PaperEventListener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(final @NotNull PaperEventContainer<PlayerDropItemEvent> container) {
        final PlayerDropItemEvent event = container.getEvent();

        if (container.getModule().getCache().getWorldDark().isInWorldDark(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
