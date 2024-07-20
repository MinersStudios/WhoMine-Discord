package com.minersstudios.whomine.listener.impl.event.player;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public final class PlayerStopSpectatingEntityListener extends EventListener {

    public PlayerStopSpectatingEntityListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerStopSpectatingEntity(final @NotNull PlayerStopSpectatingEntityEvent event) {
        if (this.getPlugin().getCache().getWorldDark().isInWorldDark(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
