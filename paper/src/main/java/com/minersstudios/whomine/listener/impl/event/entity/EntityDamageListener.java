package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(EntityDamageEvent.class)
public final class EntityDamageListener extends PaperEventListener {

    @CancellableHandler(ignoreCancelled = true)
    public void onEntityDamage(final @NotNull PaperEventContainer<EntityDamageEvent> container) {
        final EntityDamageEvent event = container.getEvent();

        if (
                event.getEntity() instanceof Player player
                && container.getModule().getCache().getWorldDark().isInWorldDark(player)
        ) {
            event.setCancelled(true);
        }
    }
}
