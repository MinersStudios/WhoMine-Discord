package com.minersstudios.whomine.listener.impl.event.server;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.utility.MSLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

public final class ServerCommandListener extends EventListener {

    public ServerCommandListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerCommand(final @NotNull ServerCommandEvent event) {
        final String command = event.getCommand().split(" ")[0];

        if (this.getPlugin().getCommandManager().isPlayerOnly(command)) {
            MSLogger.severe(
                    event.getSender(),
                    Translations.ERROR_ONLY_PLAYER_COMMAND.asTranslatable()
            );
            event.setCancelled(true);
        }
    }
}
