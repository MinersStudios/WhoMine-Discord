package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerGameModeChangeEvent.class)
public final class PlayerGameModeChangeListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerGameModeChange(final @NotNull PaperEventContainer<PlayerGameModeChangeEvent> container) {
        container.getModule().getCache().getDiggingMap().removeAll(container.getEvent().getPlayer());
    }
}
