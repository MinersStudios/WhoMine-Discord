package com.minersstudios.whomine.command.impl.minecraft.admin.teleport;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.api.utility.Font;
import com.minersstudios.whomine.utility.MSPlayerUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.minersstudios.whomine.api.locale.Translations.*;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

public final class TeleportToLastDeathLocationCommand extends PluginCommandExecutor {

    public TeleportToLastDeathLocationCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("teleporttolastdeathlocation")
                .aliases(
                        "teleporttolastdeathloc",
                        "tptolastdeathlocation",
                        "tptolastdeathloc",
                        "tptolastdeath"
                )
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [id/никнейм]")
                .description("Телепортирует игрока на его последнее место смерти")
                .permission("msessentials.teleporttolastdeathlocation")
                .permissionDefault(PermissionDefault.OP)
                .commandNode(
                        literal("teleporttolastdeathlocation")
                        .then(argument("id/никнейм", StringArgumentType.word()))
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

        final PlayerInfo playerInfo = PlayerInfo.fromString(this.getPlugin(), args[0]);

        if (playerInfo == null) {
            MSLogger.severe(
                    sender,
                    ERROR_PLAYER_NOT_FOUND.asTranslatable()
            );

            return true;
        }

        if (playerInfo.getOnlinePlayer() == null) {
            MSLogger.warning(
                    sender,
                    ERROR_PLAYER_NOT_ONLINE.asTranslatable()
            );

            return true;
        }

        playerInfo.teleportToLastDeathLocation().thenRun(() ->
                MSLogger.fine(
                        sender,
                        COMMAND_TELEPORT_TO_LAST_DEATH_SENDER_MESSAGE.asTranslatable()
                        .arguments(
                                playerInfo.getGrayIDGreenName(),
                                text(playerInfo.getNickname())
                        )
                )
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
