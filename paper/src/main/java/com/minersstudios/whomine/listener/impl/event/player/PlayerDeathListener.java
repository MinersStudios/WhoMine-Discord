package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.PaperCache;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.utility.MessageUtils;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerDeathEvent.class)
public final class PlayerDeathListener extends PaperEventListener {

    @EventHandler
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
