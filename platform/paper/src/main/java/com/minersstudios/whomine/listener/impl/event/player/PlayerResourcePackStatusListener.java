package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.ResourcePack;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerResourcePackStatusEvent.class)
public final class PlayerResourcePackStatusListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerResourcePackStatus(final @NotNull PaperEventContainer<PlayerResourcePackStatusEvent> container) {
        final PlayerResourcePackStatusEvent event = container.getEvent();
        final PlayerResourcePackStatusEvent.Status status = event.getStatus();
        final Player player = event.getPlayer();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(container.getModule(), player);
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
