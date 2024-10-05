package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.MSPlayerUtils;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerChangedWorldEvent.class)
public final class PlayerChangedWorldListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerChangedWorld(final @NotNull PaperEventContainer<PlayerChangedWorldEvent> container) {
        MSPlayerUtils.hideNameTag(
                container.getModule(),
                container.getEvent().getPlayer()
        );
    }
}
