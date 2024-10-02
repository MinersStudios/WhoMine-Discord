package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(eventClass = PlayerGameModeChangeEvent.class)
public final class PlayerGameModeChangeListener extends PaperEventListener {

    @EventHandler
    public void onPlayerGameModeChange(final @NotNull PaperEventContainer<PlayerGameModeChangeEvent> container) {
        container.getModule().getCache().getDiggingMap().removeAll(container.getEvent().getPlayer());
    }
}
