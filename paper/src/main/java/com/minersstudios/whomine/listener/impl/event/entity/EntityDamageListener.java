package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(eventClass = EntityDamageEvent.class)
public final class EntityDamageListener extends PaperEventListener {

    @EventHandler(ignoreCancelled = true)
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
