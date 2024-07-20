package com.minersstudios.whomine.listener.impl.event.command;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.MSLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.command.UnknownCommandEvent;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.locale.Translations.ERROR_UNKNOWN_COMMAND;

public class UnknownCommandListener extends EventListener {

    public UnknownCommandListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onUnknownCommand(final @NotNull UnknownCommandEvent event) {
        event.message(null);
        MSLogger.severe(
                event.getSender(),
                ERROR_UNKNOWN_COMMAND.asTranslatable()
        );
    }
}
