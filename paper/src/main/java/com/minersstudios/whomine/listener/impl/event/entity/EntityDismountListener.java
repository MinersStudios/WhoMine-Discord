package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
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
