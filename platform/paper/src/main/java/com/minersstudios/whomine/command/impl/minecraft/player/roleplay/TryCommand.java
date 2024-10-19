package com.minersstudios.whomine.command.impl.minecraft.player.roleplay;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import com.minersstudios.wholib.utility.ChatUtils;
import com.minersstudios.wholib.utility.Font;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

import static com.minersstudios.wholib.locale.Translations.*;
import static com.minersstudios.wholib.paper.utility.MessageUtils.RolePlayActionType.ME;
import static com.minersstudios.wholib.paper.utility.MessageUtils.sendRPEventMessage;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

public final class TryCommand extends PluginCommandExecutor {
    private static final TranslatableComponent[] VARIANTS = new TranslatableComponent[] {
            COMMAND_TRY_VARIANT_SUCCESS.asTranslatable().color(NamedTextColor.GREEN),
            COMMAND_TRY_VARIANT_FAIL.asTranslatable().color(NamedTextColor.RED)
    };

    private final SecureRandom random;

    public TryCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("try")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [действие]")
                .description("Рандомно определяет исход того, что делает ваш персонаж")
                .playerOnly(true)
                .commandNode(
                        literal("try")
                        .then(argument("action", StringArgumentType.greedyString()))
                        .build()
                )
                .build()
        );

        this.random = new SecureRandom();
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
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getModule(), player);

        if (playerInfo.isMuted()) {
            MSLogger.warning(
                    player,
                    COMMAND_MUTE_ALREADY_RECEIVER.asTranslatable()
            );

            return true;
        }

        sendRPEventMessage(
                player,
                text(ChatUtils.extractMessage(args, 0))
                .appendSpace()
                .append(VARIANTS[this.random.nextInt(2)]),
                ME
        );

        return true;
    }
}
