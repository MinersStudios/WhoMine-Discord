package com.minersstudios.whomine.listener.event.entity;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
