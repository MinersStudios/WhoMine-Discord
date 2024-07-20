package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.Cache;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerQuitListener extends EventListener {

    public PlayerQuitListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final Cache cache = this.getPlugin().getCache();

        cache.getDiggingMap().removeAll(player);
        cache.getStepMap().remove(player);

        event.quitMessage(null);
        PlayerInfo
        .fromOnlinePlayer(this.getPlugin(), event.getPlayer())
        .handleQuit();
    }
}
