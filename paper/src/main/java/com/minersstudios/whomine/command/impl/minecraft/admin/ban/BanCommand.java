package com.minersstudios.whomine.command.impl.minecraft.admin.ban;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.utility.DateUtils;
import com.minersstudios.whomine.api.utility.Font;
import com.mojang.brigadier.arguments.StringArgumentType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.minersstudios.whomine.api.locale.Translations.*;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public final class BanCommand extends PluginCommandExecutor {

    public BanCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("ban")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [id/никнейм] [время][s/m/h/d/M/y] [причина]")
                .description("Покажи кто тут главный и забань игрока")
                .permission("msessentials.ban")
                .permissionDefault(PermissionDefault.OP)
                .commandNode(
                        literal("ban")
                        .then(
                                argument("id/nickname", StringArgumentType.word())
                                .then(
                                        argument("time", StringArgumentType.word())
                                        .then(argument("reason", StringArgumentType.greedyString()))
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
        if (args.length < 2) {
            return false;
        }

        final Instant date = DateUtils.getDateFromString(args[1], false);

        if (date == null) {
            MSLogger.severe(
                    sender,
                    ERROR_WRONG_FORMAT.asTranslatable()
            );
            return true;
        }

        final String reason = args.length > 2
                ? ChatUtils.extractMessage(args, 2)
                : COMMAND_BAN_DEFAULT_REASON.asString();

        final PlayerInfo playerInfo = PlayerInfo.fromString(this.getPlugin(), args[0]);

        if (playerInfo == null) {
            MSLogger.severe(
                    sender,
                    ERROR_PLAYER_NOT_FOUND.asTranslatable()
            );
            return true;
        }

        playerInfo.setBanned(true, date, reason, sender);
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        switch (args.length) {
            case 1 -> {
                final var completions = new ObjectArrayList<String>();

                for (final var offlinePlayer : sender.getServer().getOfflinePlayers()) {
                    final String nickname = offlinePlayer.getName();
                    final UUID uuid = offlinePlayer.getUniqueId();

                    if (
                            ChatUtils.isBlank(nickname)
                            || offlinePlayer.isBanned()
                    ) {
                        continue;
                    }

                    final int id = this.getPlugin().getCache().getIdMap().getID(uuid, false, false);

                    if (id != -1) {
                        completions.add(String.valueOf(id));
                    }

                    if (offlinePlayer.hasPlayedBefore()) {
                        completions.add(nickname);
                    }
                }

                return completions;
            }
            case 2 -> {
                return DateUtils.getTimeSuggestions(args[1]);
            }
            default -> {
                return EMPTY_TAB;
            }
        }
    }
}
