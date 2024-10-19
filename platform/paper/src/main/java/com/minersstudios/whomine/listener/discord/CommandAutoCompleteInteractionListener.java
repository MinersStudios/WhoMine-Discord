package com.minersstudios.whomine.listener.discord;

import com.minersstudios.wholib.discord.DiscordListener;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class CommandAutoCompleteInteractionListener extends DiscordListener {

    @Override
    public void onCommandAutoCompleteInteraction(final @NotNull CommandAutoCompleteInteractionEvent event) {
        //final SlashCommandExecutor executor =
        //        this.getModule().getCommandManager().getDiscordExecutor(event.getCommandIdLong());

        //if (executor != null) {
        //    executor.tabComplete(event);
        //}
    }
}
