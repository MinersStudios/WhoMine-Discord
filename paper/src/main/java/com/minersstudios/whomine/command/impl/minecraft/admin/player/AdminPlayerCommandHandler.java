package com.minersstudios.whomine.command.impl.minecraft.admin.player;

import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.collection.IDMap;
import com.minersstudios.whomine.utility.DateUtils;
import com.minersstudios.whomine.api.utility.Font;
import com.minersstudios.whomine.api.utility.IDUtils;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.minersstudios.whomine.locale.Translations.*;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public final class AdminPlayerCommandHandler extends PluginCommandExecutor {
    private static final List<String> TAB_2 = Arrays.asList(
            "update",
            "info",
            "first-join",
            "pronouns",
            "game-params",
            "settings",
            "ban-info",
            "mute-info",
            "name"
    );
    private static final List<String> TAB_3_PRONOUNS = Arrays.asList(
            "he",
            "she",
            "they"
    );
    private static final List<String> TAB_3_GAME_PARAMS = Arrays.asList(
            "game-mode",
            "health",
            "air"
    );
    private static final List<String> TAB_3_SETTINGS = Arrays.asList(
            "resourcepack-type"
    );
    private static final List<String> TAB_3_BAN_MUTE_INFO = Arrays.asList(
            "reason",
            "time"
    );
    private static final List<String> TAB_3_NAME = Arrays.asList(
            "reset",
            "first-name",
            "last-name",
            "patronymic"
    );
    private static final List<String> TAB_4_GAME_PARAMS_GAME_MODE = Arrays.asList(
            "survival",
            "creative",
            "spectator",
            "adventure"
    );
    private static final List<String> TAB_4_GAME_PARAMS_AIR = Arrays.asList(
            "300",
            "0"
    );
    private static final List<String> TAB_4_GAME_PARAMS_HEALTH = Arrays.asList(
            "20.0",
            "0.0"
    );
    private static final List<String> TAB_4_SETTINGS_RESOURCEPACK_TYPE = Arrays.asList(
            "full",
            "lite",
            "none",
            "null"
    );
    private static final List<String> TAB_4_SETTINGS_SKIN = Arrays.asList(
            "set",
            "remove",
            "add"
    );
    private static final List<String> TAB_4_BAN_INFO_REASON = Collections.singletonList(COMMAND_BAN_DEFAULT_REASON.asString());
    private static final List<String> TAB_4_MUTE_INFO_REASON = Collections.singletonList(COMMAND_MUTE_DEFAULT_REASON.asString());
    private static final List<String> TAB_4_NAME_EMPTY = Collections.singletonList("empty");

    public AdminPlayerCommandHandler(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("player")
                .description("Команды, отвечающие за параметры игрока")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [id/никнейм] [параметры]")
                .permission("msessentials.player.*")
                .permissionDefault(PermissionDefault.OP)
                .commandNode(
                        literal("player")
                        .then(
                                argument("айди/никнейм", StringArgumentType.word())
                                .then(literal("update"))
                                .then(literal("info"))
                                .then(
                                        literal("pronouns")
                                        .then(literal("he"))
                                        .then(literal("she"))
                                        .then(literal("they"))
                                )
                                .then(
                                        literal("game-params")
                                        .then(
                                                literal("game-mode")
                                                .then(literal("survival"))
                                                .then(literal("creative"))
                                                .then(literal("spectator"))
                                                .then(literal("adventure"))
                                        )
                                        .then(
                                                literal("health")
                                                .then(argument("значение", DoubleArgumentType.doubleArg()))
                                        )
                                        .then(
                                                literal("air")
                                                .then(argument("значение", IntegerArgumentType.integer()))
                                        )
                                )
                                .then(literal("first-join"))
                                .then(
                                        literal("settings")
                                        .then(
                                                literal("resourcepack-type")
                                                .then(literal("full"))
                                                .then(literal("lite"))
                                                .then(literal("none"))
                                                .then(literal("null"))
                                        )
                                        .then(
                                                literal("skin")
                                                .then(
                                                        literal("set")
                                                        .then(argument("имя", StringArgumentType.word()))
                                                )
                                                .then(
                                                        literal("remove")
                                                        .then(argument("имя", StringArgumentType.word()))
                                                )
                                                .then(
                                                        literal("add")
                                                        .then(
                                                                argument("имя", StringArgumentType.word())
                                                                .then(argument("ссылка", StringArgumentType.greedyString()))
                                                        )
                                                )
                                        )
                                )
                                .then(
                                        literal("ban-info")
                                        .then(
                                                literal("reason")
                                                .then(argument("причина", StringArgumentType.greedyString()))
                                        )
                                        .then(
                                                literal("time")
                                                .then(argument("время", StringArgumentType.greedyString()))
                                        )
                                )
                                .then(
                                        literal("mute-info")
                                        .then(
                                                literal("reason")
                                                .then(argument("причина", StringArgumentType.greedyString()))
                                        )
                                        .then(
                                                literal("time")
                                                .then(argument("время", StringArgumentType.greedyString()))
                                        )
                                )
                                .then(
                                        literal("name")
                                        .then(literal("reset"))
                                        .then(
                                                literal("first-name")
                                                .then(argument("имя", StringArgumentType.greedyString()))
                                        )
                                        .then(
                                                literal("last-name")
                                                .then(literal("empty"))
                                                .then(argument("фамилия", StringArgumentType.greedyString()))
                                        )
                                        .then(
                                                literal("patronymic")
                                                .then(literal("empty"))
                                                .then(argument("отчество", StringArgumentType.greedyString()))
                                        )
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

        final WhoMine plugin = this.getPlugin();
        final PlayerInfo playerInfo = PlayerInfo.fromString(plugin, args[0]);

        if (playerInfo == null) {
            MSLogger.severe(
                    sender,
                    ERROR_PLAYER_NOT_FOUND.asTranslatable()
            );

            return true;
        }

        return switch (args[1]) {
            case "update" ->      AdminUpdateCommand.runCommand(sender, playerInfo);
            case "info" ->        AdminInfoCommand.runCommand(plugin, sender, playerInfo);
            case "pronouns" ->    AdminPronounsCommand.runCommand(sender, args, playerInfo);
            case "game-params" -> AdminGameParamsCommand.runCommand(sender, args, playerInfo);
            case "first-join" ->  AdminFirstJoinCommand.runCommand(plugin, sender, playerInfo);
            case "settings" ->    AdminSettingsCommand.runCommand(plugin, sender, args, playerInfo);
            case "ban-info" ->    AdminBanInfoCommand.runCommand(plugin, sender, args, playerInfo);
            case "mute-info" ->   AdminMuteInfoCommand.runCommand(plugin, sender, args, playerInfo);
            case "name" ->        AdminNameCommand.runCommand(sender, args, playerInfo);
            default -> false;
        };
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
                final IDMap idMap = this.getPlugin().getCache().getIdMap();

                for (final var offlinePlayer : sender.getServer().getOfflinePlayers()) {
                    final String nickname = offlinePlayer.getName();

                    if (nickname == null) {
                        continue;
                    }

                    final int id = idMap.getID(offlinePlayer.getUniqueId(), false, false);

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
                return TAB_2;
            }
            case 3 -> {
                switch (args[1]) {
                    case "pronouns" -> {
                        return TAB_3_PRONOUNS;
                    }
                    case "game-params" -> {
                        return TAB_3_GAME_PARAMS;
                    }
                    case "settings" -> {
                        return TAB_3_SETTINGS;
                    }
                    case "ban-info", "mute-info" -> {
                        return TAB_3_BAN_MUTE_INFO;
                    }
                    case "name" -> {
                        return TAB_3_NAME;
                    }
                }
            }
            case 4 -> {
                switch (args[1]) {
                    case "game-params" -> {
                        switch (args[2]) {
                            case "game-mode" -> {
                                return TAB_4_GAME_PARAMS_GAME_MODE;
                            }
                            case "air" -> {
                                return TAB_4_GAME_PARAMS_AIR;
                            }
                            case "health" -> {
                                return TAB_4_GAME_PARAMS_HEALTH;
                            }
                        }
                    }
                    case "settings" -> {
                        switch (args[2]) {
                            case "resourcepack-type" -> {
                                return TAB_4_SETTINGS_RESOURCEPACK_TYPE;
                            }
                            case "skin" -> {
                                return TAB_4_SETTINGS_SKIN;
                            }
                        }
                    }
                    case "ban-info" -> {
                        switch (args[2]) {
                            case "time" -> {
                                return DateUtils.getTimeSuggestions(args[3]);
                            }
                            case "reason" -> {
                                return TAB_4_BAN_INFO_REASON;
                            }
                        }
                    }
                    case "mute-info" -> {
                        switch (args[2]) {
                            case "time" -> {
                                return DateUtils.getTimeSuggestions(args[3]);
                            }
                            case "reason" -> {
                                return TAB_4_MUTE_INFO_REASON;
                            }
                        }
                    }
                    case "name" -> {
                        switch (args[2]) {
                            case "last-name", "patronymic" -> {
                                return TAB_4_NAME_EMPTY;
                            }
                        }
                    }
                }
            }
            case 5 -> {
                switch (args[2]) {
                    case "skin" -> {
                        final OfflinePlayer offlinePlayer;

                        if (IDUtils.matchesIDRegex(args[0])) {
                            final IDMap idMap = this.getPlugin().getCache().getIdMap();
                            offlinePlayer = idMap.getPlayerByID(args[0]);
                        } else if (args[0].length() > 2) {
                            offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                        } else {
                            return EMPTY_TAB;
                        }

                        final PlayerInfo playerInfo = PlayerInfo.fromOfflinePlayer(this.getPlugin(), offlinePlayer);

                        switch (args[3]) {
                            case "set", "remove" -> {
                                if (playerInfo == null) {
                                    return EMPTY_TAB;
                                }

                                final var skins = playerInfo.getPlayerFile().getSkins();
                                final var names = new ObjectArrayList<String>(skins.size());

                                for (var skin : skins) {
                                    names.add(skin.getName());
                                }

                                return names;
                            }
                        }
                    }
                }
            }
        }

        return EMPTY_TAB;
    }
}
