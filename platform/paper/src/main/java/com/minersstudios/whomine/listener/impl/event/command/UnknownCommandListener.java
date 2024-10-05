package com.minersstudios.whomine.listener.impl.event.command;

import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.MSLogger;
import org.bukkit.event.command.UnknownCommandEvent;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.api.locale.Translations.ERROR_UNKNOWN_COMMAND;

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
