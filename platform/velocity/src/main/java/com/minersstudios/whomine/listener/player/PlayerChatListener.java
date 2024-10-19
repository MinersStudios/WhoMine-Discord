package com.minersstudios.whomine.listener.player;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.event.handle.AsyncHandler;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.velocity.event.VelocityEventContainer;
import com.minersstudios.wholib.velocity.event.VelocityEventListener;
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
