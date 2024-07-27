package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerDropItemListener extends EventListener {

    public PlayerDropItemListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        if (this.getPlugin().getCache().getWorldDark().isInWorldDark(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
