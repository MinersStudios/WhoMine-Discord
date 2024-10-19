package com.minersstudios.whomine.command.impl.discord;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.utility.MSLogger;
import com.minersstudios.wholib.utility.ChatUtils;
import com.minersstudios.whomine.command.api.SlashCommandExecutor;
import com.minersstudios.whomine.command.api.discord.interaction.CommandHandler;
import com.minersstudios.wholib.paper.discord.BotHandler;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.wholib.locale.Translations.COMMAND_DISCORD_UNLINK_DISCORD_SUCCESS;
import static com.minersstudios.wholib.locale.Translations.COMMAND_DISCORD_UNLINK_MINECRAFT_SUCCESS;
import static net.kyori.adventure.text.Component.text;

public final class UnlinkCommand extends SlashCommandExecutor {

    public UnlinkCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                Commands.slash("unlink", "Unlink Discord account from Minecraft account")
        );
    }

    @Override
    public void onCommand(final @NotNull CommandHandler handler) {
        handler.deferReply();

        final PlayerInfo playerInfo = handler.retrievePlayerInfo();

        if (playerInfo != null) {
            playerInfo.unlinkDiscord();
            handler.send(
                    BotHandler.craftEmbed(
                            COMMAND_DISCORD_UNLINK_DISCORD_SUCCESS
                            .asString(
                                    ChatUtils.serializePlainComponent(playerInfo.getDefaultName()),
                                    playerInfo.getPlayerFile().getPlayerName().getNickname()
                            )
                    )
            );

            final Player onlinePlayer = playerInfo.getOnlinePlayer();

            if (onlinePlayer != null) {
                MSLogger.fine(
                        onlinePlayer,
                        COMMAND_DISCORD_UNLINK_MINECRAFT_SUCCESS.asTranslatable()
                        .arguments(text(handler.getInteraction().getUser().getName()))
                );
            }
        }
    }
}
