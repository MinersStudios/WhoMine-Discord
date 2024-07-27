package com.minersstudios.whomine.command.impl.minecraft.player.roleplay;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.locale.Translations;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.utility.ChatUtils;
import com.minersstudios.whomine.utility.Font;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.utility.MessageUtils.RolePlayActionType.TODO;
import static com.minersstudios.whomine.utility.MessageUtils.sendRPEventMessage;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

public final class TodoCommand extends PluginCommandExecutor {

    public TodoCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("todo")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [речь] * [действие]")
                .description("Описывает действие и речь в чате")
                .playerOnly(true)
                .commandNode(
                        literal("todo")
                        .then(
                                argument("speech", StringArgumentType.greedyString())
                                .then(
                                        literal("*")
                                        .then(argument("action", StringArgumentType.greedyString()))
                                )
                        ).build()
                )
                .build()
        );
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        final String message = ChatUtils.extractMessage(args, 0);

        if (
                args.length < 3
                || !message.contains("*")
        ) {
            return false;
        }

        final Player player = (Player) sender;
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getPlugin(), player);

        if (playerInfo.isMuted()) {
            MSLogger.warning(
                    player,
                    Translations.COMMAND_MUTE_ALREADY_RECEIVER.asTranslatable()
            );

            return true;
        }

        final String action = message.substring(message.indexOf('*') + 1).trim();
        final String speech = message.substring(0, message.indexOf('*')).trim();

        if (
                action.isEmpty()
                || speech.isEmpty()
        ) {
            return false;
        }

        sendRPEventMessage(
                player,
                text(speech),
                text(action),
                TODO
        );

        return true;
    }
}
