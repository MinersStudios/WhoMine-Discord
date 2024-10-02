package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.PaperCache;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(eventClass = PlayerQuitEvent.class)
public final class PlayerQuitListener extends PaperEventListener {

    @EventHandler
    public void onPlayerQuit(final @NotNull PaperEventContainer<PlayerQuitEvent> container) {
        final PlayerQuitEvent event = container.getEvent();
        final WhoMine module = container.getModule();

        final Player player = event.getPlayer();
        final PaperCache cache = module.getCache();

        cache.getDiggingMap().removeAll(player);
        cache.getStepMap().remove(player);

        event.quitMessage(null);
        PlayerInfo
        .fromOnlinePlayer(module, event.getPlayer())
        .handleQuit();
    }
}
