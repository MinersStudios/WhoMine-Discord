package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public final class EntityDamageListener extends EventListener {

    public EntityDamageListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(final @NotNull EntityDamageEvent event) {
        if (
                event.getEntity() instanceof Player player
                && this.getPlugin().getCache().getWorldDark().isInWorldDark(player)
        ) {
            event.setCancelled(true);
        }
    }
}
