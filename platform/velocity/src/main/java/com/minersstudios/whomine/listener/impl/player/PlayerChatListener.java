package com.minersstudios.whomine.listener.impl.player;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.event.handle.AsyncHandler;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.VelocityEventContainer;
import com.minersstudios.whomine.event.VelocityEventListener;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@ListenFor(PlayerChatEvent.class)
public class PlayerChatListener extends VelocityEventListener {

    @AsyncHandler(order = EventOrder.CUSTOM)
    public void onPlayerChat(final @NotNull VelocityEventContainer<PlayerChatEvent> container) {
        container.getModule().getLogger().log(
                Level.INFO,
                container.getEvent().getPlayer().getUsername() + " : " + container.getEvent().getMessage()
        );
    }
}
