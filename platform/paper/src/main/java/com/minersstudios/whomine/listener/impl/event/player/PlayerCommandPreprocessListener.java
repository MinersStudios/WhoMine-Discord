package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.utility.MSLogger;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
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
