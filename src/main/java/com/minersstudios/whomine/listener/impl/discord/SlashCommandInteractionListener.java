package com.minersstudios.whomine.listener.impl.discord;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.SlashCommandExecutor;
import com.minersstudios.whomine.listener.api.DiscordListener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public final class SlashCommandInteractionListener extends DiscordListener {

    public SlashCommandInteractionListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @Override
    public void onSlashCommandInteraction(final @NotNull SlashCommandInteractionEvent event) {
        final SlashCommandExecutor executor =
                this.getPlugin().getCommandManager().getDiscordExecutor(event.getCommandIdLong());

        if (executor != null) {
            executor.execute(event);
        }
    }
}
