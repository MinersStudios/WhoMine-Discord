package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.MSPlayerUtils;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
