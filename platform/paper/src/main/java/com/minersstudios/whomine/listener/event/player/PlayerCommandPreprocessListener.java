package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.utility.MSLogger;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerCommandPreprocessEvent.class)
public final class PlayerCommandPreprocessListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerCommandPreprocess(final @NotNull PaperEventContainer<PlayerCommandPreprocessEvent> container) {
        final PlayerCommandPreprocessEvent event = container.getEvent();
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        if (
                (
                        message.startsWith("/l")
                        && !message.startsWith("/logout")
                )
                || message.startsWith("/reg")
                || !container.getModule().getCache().getWorldDark().isInWorldDark(player)
        ) {
            return;
        }

        event.setCancelled(true);
        MSLogger.warning(
                player,
                Translations.WARNING_YOU_CANT_DO_THIS_NOW.asTranslatable()
        );
    }
}
