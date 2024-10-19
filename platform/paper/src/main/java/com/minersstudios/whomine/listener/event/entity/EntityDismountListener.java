package com.minersstudios.whomine.listener.event.entity;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.entity.EntityDismountEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(EntityDismountEvent.class)
public final class EntityDismountListener extends PaperEventListener {

    @CancellableHandler
    public void onEntityDismount(final @NotNull PaperEventContainer<EntityDismountEvent> container) {
        if (container.getEvent().getEntity() instanceof final Player player) {
            PlayerInfo
            .fromOnlinePlayer(container.getModule(), player)
            .unsetSitting();
        }
    }
}
