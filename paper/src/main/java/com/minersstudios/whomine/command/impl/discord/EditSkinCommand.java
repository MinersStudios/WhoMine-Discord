package com.minersstudios.whomine.command.impl.discord;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.command.api.SlashCommandExecutor;
import com.minersstudios.whomine.command.api.discord.interaction.CommandHandler;
import com.minersstudios.whomine.command.api.discord.interaction.TabCompleterHandler;
import com.minersstudios.whomine.discord.BotHandler;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.skin.Skin;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;

import static com.minersstudios.whomine.api.locale.Translations.*;
import static net.kyori.adventure.text.Component.text;

public final class EditSkinCommand extends SlashCommandExecutor {

    public EditSkinCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                Commands.slash("editskin", "Edit skin")
                .addOption(OptionType.STRING, "name", "Skin Name", true, true)
                .addOption(OptionType.STRING, "url", "Skin URL")
                .addOption(OptionType.STRING, "value", "Skin Value")
                .addOption(OptionType.STRING, "signature", "Skin Signature")
        );
    }

    @Override
    protected void onCommand(final @NotNull CommandHandler handler) {
        handler.deferReply();

        final PlayerInfo playerInfo = handler.retrievePlayerInfo();

        if (playerInfo == null) {
            return;
        }

        final SlashCommandInteraction interaction = handler.getInteraction();
        final OptionMapping nameOption = interaction.getOption("name");

        if (nameOption == null) {
            handler.send(DISCORD_SKIN_INVALID_NAME_REGEX.asString());
            return;
        }

        final String name = nameOption.getAsString();

        if (!Skin.matchesNameRegex(name)) {
            handler.send(DISCORD_SKIN_INVALID_NAME_REGEX.asString());
            return;
        }

        final int skinIndex = playerInfo.getPlayerFile().getSkinIndex(name);

        if (skinIndex == -1) {
            handler.send(
                    ChatUtils.serializePlainComponent(
                            DISCORD_COMMAND_SKIN_NOT_FOUND
                            .asComponent(text(name))
                    )
            );
        }

        final OptionMapping urlOption = interaction.getOption("url");
        final OptionMapping valueOption = interaction.getOption("value");
        final OptionMapping signatureOption = interaction.getOption("signature");

        if (
                urlOption != null
                && valueOption == null
                && signatureOption == null
        ) {
            try {
                final Skin skin = Skin.create(this.getPlugin(), name, urlOption.getAsString());

                if (skin == null) {
                    handler.send(DISCORD_SKIN_SERVICE_UNAVAILABLE.asString());
                } else {
                    edit(playerInfo, skinIndex, skin, null, handler);
                }
            } catch (final IllegalArgumentException ignored) {
                handler.send(DISCORD_SKIN_INVALID_IMG.asString());
            }
        } else if (
                urlOption == null
                && valueOption != null
                && signatureOption != null
        ) {
            try {
                edit(
                        playerInfo,
                        skinIndex,
                        Skin.create(
                                name,
                                valueOption.getAsString(),
                                signatureOption.getAsString()
                        ),
                        null,
                        handler
                );
            } catch (final IllegalArgumentException ignored) {
                handler.send(DISCORD_SKIN_INVALID_IMG.asString());
            }
        } else {
            handler.send(DISCORD_COMMAND_INVALID_ARGUMENTS.asString());
        }
    }

    @Override
    protected @NotNull List<Command.Choice> onTabComplete(final @NotNull TabCompleterHandler handler) {
        if (!"name".equals(handler.getCurrentOption().getName())) {
            return EMPTY_TAB;
        }

        final PlayerInfo playerInfo = handler.getPlayerInfo();

        if (playerInfo == null) {
            return EMPTY_TAB;
        }

        final var skinNames = new ObjectArrayList<Command.Choice>(MAX_TAB_SIZE);

        for (final var skin : playerInfo.getPlayerFile().getSkins()) {
            skinNames.add(
                    new Command.Choice(
                            skin.getName(),
                            skin.getName()
                    )
            );
        }

        return skinNames;
    }

    public static boolean edit(
            final @NotNull PlayerInfo playerInfo,
            final @Range(from = 0, to = Integer.MAX_VALUE) int currentSkinIndex,
            final @NotNull Skin newSkin,
            final @Nullable Message messageForReply,
            final @Nullable CommandHandler handler
    ) {
        final boolean isEdited = playerInfo.getPlayerFile().setSkin(currentSkinIndex, newSkin);

        if (!isEdited) {
            return false;
        }

        final String skinName = newSkin.getName();
        final Player player = playerInfo.getOnlinePlayer();
        final MessageEmbed embed =
                BotHandler.craftEmbed(
                        ChatUtils.serializePlainComponent(
                                DISCORD_SKIN_SUCCESSFULLY_EDITED
                                .asComponent(
                                        text(skinName),
                                        playerInfo.getDefaultName(),
                                        text(playerInfo.getNickname())
                                )
                        )
                );

        if (player != null) {
            MSLogger.fine(
                    player,
                    DISCORD_SKIN_SUCCESSFULLY_EDITED_MINECRAFT.asTranslatable()
                    .arguments(text(skinName))
            );
        }

        if (messageForReply != null) {
            messageForReply.replyEmbeds(embed).queue();
        } else if (handler != null) {
            handler.send(embed);
        } else {
            playerInfo.sendPrivateDiscordMessage(embed);
        }

        return true;
    }
}
