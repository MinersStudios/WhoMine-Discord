package com.minersstudios.whomine.listener.discord;

import com.minersstudios.wholib.discord.DiscordListener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public final class SlashCommandInteractionListener extends DiscordListener {

    @Override
    public void onSlashCommandInteraction(final @NotNull SlashCommandInteractionEvent event) {
        //final SlashCommandExecutor executor =
        //        this.getModule().getCommandManager().getDiscordExecutor(event.getCommandIdLong());
//
        //if (executor != null) {
        //    executor.execute(event);
        //}
    }
}
