package com.minersstudios.whomine.listener.impl.discord;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.SlashCommandExecutor;
import com.minersstudios.whomine.listener.api.DiscordListener;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class CommandAutoCompleteInteractionListener extends DiscordListener {

    public CommandAutoCompleteInteractionListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @Override
    public void onCommandAutoCompleteInteraction(final @NotNull CommandAutoCompleteInteractionEvent event) {
        final SlashCommandExecutor executor =
                this.getPlugin().getCommandManager().getDiscordExecutor(event.getCommandIdLong());

        if (executor != null) {
            executor.tabComplete(event);
        }
    }
}
