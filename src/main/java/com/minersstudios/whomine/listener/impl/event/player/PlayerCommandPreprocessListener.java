package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.locale.Translations;
import com.minersstudios.whomine.utility.MSLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerCommandPreprocessListener extends EventListener {

    public PlayerCommandPreprocessListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(final @NotNull PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        if (
                (
                        message.startsWith("/l")
                        && !message.startsWith("/logout")
                )
                || message.startsWith("/reg")
                || !this.getPlugin().getCache().getWorldDark().isInWorldDark(player)
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
