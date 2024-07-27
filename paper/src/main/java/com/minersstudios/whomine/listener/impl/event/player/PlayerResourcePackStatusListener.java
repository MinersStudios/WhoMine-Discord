package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.ResourcePack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerResourcePackStatusListener extends EventListener {

    public PlayerResourcePackStatusListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerResourcePackStatus(final @NotNull PlayerResourcePackStatusEvent event) {
        final PlayerResourcePackStatusEvent.Status status = event.getStatus();
        final Player player = event.getPlayer();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getPlugin(), player);
        final ResourcePack.Type currentType = playerInfo.getPlayerFile().getPlayerSettings().getResourcePackType();

        if (
                currentType == ResourcePack.Type.NULL
                || status == PlayerResourcePackStatusEvent.Status.ACCEPTED
        ) {
            return;
        }

        playerInfo.completeResourcePackFuture(status);
    }
}
