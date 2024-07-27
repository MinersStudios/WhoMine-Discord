package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerGameModeChangeListener extends EventListener {

    public PlayerGameModeChangeListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerGameModeChange(final @NotNull PlayerGameModeChangeEvent event) {
        this.getPlugin().getCache().getDiggingMap().removeAll(event.getPlayer());
    }
}
