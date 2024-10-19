package com.minersstudios.whomine.listener.event.server;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(ServerCommandEvent.class)
public final class ServerCommandListener extends PaperEventListener {

    @CancellableHandler
    public void onServerCommand(final @NotNull PaperEventContainer<ServerCommandEvent> container) {
        final ServerCommandEvent event = container.getEvent();
        final String command = event.getCommand().split(" ")[0];

        // TODO: fix getCommandManager
        //if (container.getModule().getCommandManager().isPlayerOnly(command)) {
        //    MSLogger.severe(
        //            event.getSender(),
        //            Translations.ERROR_ONLY_PLAYER_COMMAND.asTranslatable()
        //    );
        //    event.setCancelled(true);
        //}
    }
}
