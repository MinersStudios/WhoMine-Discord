package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerGameModeChangeEvent.class)
public final class PlayerGameModeChangeListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerGameModeChange(final @NotNull PaperEventContainer<PlayerGameModeChangeEvent> container) {
        container.getModule().getCache().getDiggingMap().removeAll(container.getEvent().getPlayer());
    }
}
