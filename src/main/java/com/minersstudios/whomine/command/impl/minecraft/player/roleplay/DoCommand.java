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

import static com.minersstudios.whomine.utility.MessageUtils.RolePlayActionType.DO;
import static com.minersstudios.whomine.utility.MessageUtils.sendRPEventMessage;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

public final class DoCommand extends PluginCommandExecutor {

    public DoCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("do")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [действие]")
                .description("Описывает состояние вашего персонажа и объектов вокруг вас")
                .playerOnly(true)
                .commandNode(
                        literal("do")
                        .then(argument("action", StringArgumentType.greedyString()))
                        .build()
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
        if (args.length == 0) {
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

        sendRPEventMessage(
                player,
                text(ChatUtils.extractMessage(args, 0)),
                DO
        );

        return true;
    }
}
