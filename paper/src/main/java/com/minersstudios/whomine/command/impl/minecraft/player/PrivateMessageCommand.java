package com.minersstudios.whomine.command.impl.minecraft.player;

import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.api.utility.Font;
import com.minersstudios.whomine.utility.MSPlayerUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.minersstudios.whomine.utility.MessageUtils.sendPrivateMessage;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

public final class PrivateMessageCommand extends PluginCommandExecutor {

    public PrivateMessageCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("privatemessage")
                .aliases(
                        "pmessage",
                        "pm",
                        "w",
                        "tell",
                        "msg"
                )
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [id/никнейм] [сообщение]")
                .description("Отправь другому игроку приватное сообщение")
                .commandNode(
                        literal("privatemessage")
                        .then(
                                argument("id/никнейм", StringArgumentType.word())
                                .then(argument("сообщение", StringArgumentType.greedyString()))
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
        if (args.length < 2) {
            return false;
        }

        final WhoMine plugin = this.getPlugin();
        final PlayerInfo senderInfo =
                sender instanceof Player player
                ? PlayerInfo.fromOnlinePlayer(plugin, player)
                : plugin.getCache().getConsolePlayerInfo();

        if (senderInfo.isMuted()) {
            MSLogger.warning(
                    sender,
                    Translations.COMMAND_MUTE_ALREADY_RECEIVER.asTranslatable()
            );

            return true;
        }

        final PlayerInfo receiverInfo = PlayerInfo.fromString(plugin, args[0]);

        if (receiverInfo == null) {
            MSLogger.warning(
                    sender,
                    Translations.ERROR_PLAYER_NOT_FOUND.asTranslatable()
            );

            return true;
        }

        if (
                !receiverInfo.isOnline()
                && !sender.hasPermission("msessentials.*")
        ) {
            MSLogger.warning(
                    sender,
                    Translations.ERROR_PLAYER_NOT_ONLINE.asTranslatable()
            );

            return true;
        }

        sendPrivateMessage(
                senderInfo,
                receiverInfo,
                text(ChatUtils.extractMessage(args, 1))
        );

        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        return args.length == 1
                ? MSPlayerUtils.getLocalPlayerNames(this.getPlugin())
                : EMPTY_TAB;
    }
}
