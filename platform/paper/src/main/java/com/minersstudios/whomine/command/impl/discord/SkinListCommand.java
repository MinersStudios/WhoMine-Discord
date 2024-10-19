package com.minersstudios.whomine.command.impl.discord;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.whomine.command.api.SlashCommandExecutor;
import com.minersstudios.whomine.command.api.discord.interaction.CommandHandler;
import com.minersstudios.wholib.paper.discord.BotHandler;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public final class SkinListCommand extends SlashCommandExecutor {

    public SkinListCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                Commands.slash("skinlist", "Skin list")
        );
    }

    @Override
    public void onCommand(final @NotNull CommandHandler handler) {
        handler.deferReply();

        final PlayerInfo playerInfo = handler.retrievePlayerInfo();

        if (playerInfo != null) {
            final StringBuilder skinList = new StringBuilder();

            for (final var skin : playerInfo.getPlayerFile().getSkins()) {
                skinList
                .append('\n')
                .append("- ")
                .append(skin.getName());
            }

            handler.send(
                    BotHandler.craftEmbed(
                            Translations.DISCORD_COMMAND_LIST_OF_SKINS
                            .asString(skinList.toString())
                    )
            );
        }
    }
}
