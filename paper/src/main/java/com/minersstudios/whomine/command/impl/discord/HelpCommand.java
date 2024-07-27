package com.minersstudios.whomine.command.impl.discord;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.discord.interaction.CommandHandler;
import com.minersstudios.whomine.command.api.SlashCommandExecutor;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public final class HelpCommand extends SlashCommandExecutor {

    public HelpCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                Commands.slash("help", "Help list")
        );
    }

    @Override
    public void onCommand(final @NotNull CommandHandler handler) {
        // TODO
    }
}
