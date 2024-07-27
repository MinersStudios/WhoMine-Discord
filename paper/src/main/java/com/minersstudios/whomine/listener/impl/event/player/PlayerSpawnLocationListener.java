package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public final class PlayerSpawnLocationListener extends EventListener {

    public PlayerSpawnLocationListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerSpawnLocation(final @NotNull PlayerSpawnLocationEvent event) {
        if (!event.getPlayer().isDead()) {
            event.setSpawnLocation(this.getPlugin().getCache().getWorldDark().getSpawnLocation());
        }
    }
}
