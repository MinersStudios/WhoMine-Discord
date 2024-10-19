package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import com.minersstudios.wholib.paper.player.ResourcePack;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
