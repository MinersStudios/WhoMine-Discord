package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.Cache;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.utility.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerDeathListener extends EventListener {

    public PlayerDeathListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getPlugin(), player);
        final Cache cache = this.getPlugin().getCache();

        cache.getDiggingMap().removeAll(player);
        cache.getStepMap().put(player, 0.0d);
        event.deathMessage(null);
        playerInfo.unsetSitting();
        MessageUtils.sendDeathMessage(player, player.getKiller());
    }
}
