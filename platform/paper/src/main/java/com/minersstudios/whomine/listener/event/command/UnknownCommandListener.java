package com.minersstudios.whomine.listener.event.command;

import com.minersstudios.wholib.event.handle.CancellableHandler;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.MSLogger;
import org.bukkit.event.command.UnknownCommandEvent;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.wholib.locale.Translations.ERROR_UNKNOWN_COMMAND;

@ListenFor(UnknownCommandEvent.class)
public class UnknownCommandListener extends PaperEventListener {

    @CancellableHandler
    public void onUnknownCommand(final @NotNull PaperEventContainer<UnknownCommandEvent> container) {
        final UnknownCommandEvent event = container.getEvent();

        event.message(null);
        MSLogger.severe(
                event.getSender(),
                ERROR_UNKNOWN_COMMAND.asTranslatable()
        );
    }
}
