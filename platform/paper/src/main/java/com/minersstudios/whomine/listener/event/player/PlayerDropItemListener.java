package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerDropItemEvent.class)
public final class PlayerDropItemListener extends PaperEventListener {

    @CancellableHandler(ignoreCancelled = true)
    public void onPlayerDropItem(final @NotNull PaperEventContainer<PlayerDropItemEvent> container) {
        final PlayerDropItemEvent event = container.getEvent();

        if (container.getModule().getCache().getWorldDark().isInWorldDark(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
