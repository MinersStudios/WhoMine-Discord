package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.paper.PaperCache;
import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import com.minersstudios.wholib.paper.utility.MessageUtils;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerDeathEvent.class)
public final class PlayerDeathListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerDeath(final @NotNull PaperEventContainer<PlayerDeathEvent> container) {
        final PlayerDeathEvent event = container.getEvent();
        final WhoMine module = container.getModule();

        final Player player = event.getEntity();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(module, player);
        final PaperCache cache = module.getCache();

        cache.getDiggingMap().removeAll(player);
        cache.getStepMap().put(player, 0.0d);
        event.deathMessage(null);
        playerInfo.unsetSitting();
        MessageUtils.sendDeathMessage(player, player.getKiller());
    }
}
