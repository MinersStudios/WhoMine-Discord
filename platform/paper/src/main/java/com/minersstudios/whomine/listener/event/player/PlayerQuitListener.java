package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.paper.PaperCache;
import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerQuitEvent.class)
public final class PlayerQuitListener extends PaperEventListener {

    @CancellableHandler
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
