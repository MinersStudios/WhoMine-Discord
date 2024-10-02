package com.minersstudios.whomine.command.impl.minecraft.admin.ban;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.collection.PlayerInfoMap;
import com.minersstudios.whomine.api.utility.Font;
import com.mojang.brigadier.arguments.StringArgumentType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public final class UnBanCommand extends PluginCommandExecutor {

    public UnBanCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("unban")
                .aliases("pardon")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [id/никнейм]")
                .description("Разбанить игрока")
                .permission("msessentials.ban")
                .permissionDefault(PermissionDefault.OP)
                .commandNode(
                        literal("unban")
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

        final PlayerInfo playerInfo = PlayerInfo.fromString(this.getModule(), args[0]);

        if (playerInfo == null) {
            MSLogger.severe(
                    sender,
                    Translations.ERROR_PLAYER_NOT_FOUND.asTranslatable()
            );

            return true;
        }

        playerInfo.pardon(sender);

        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        if (args.length == 1) {
            final var completions = new ObjectArrayList<String>();
            final PlayerInfoMap playerInfoMap = this.getModule().getCache().getPlayerInfoMap();
            final ProfileBanList banList = Bukkit.getServer().getBanList(BanList.Type.PROFILE);
            final Set<BanEntry<PlayerProfile>> entries = banList.getEntries();

            for (final var entry : entries) {
                final PlayerProfile playerProfile = entry.getBanTarget();
                final UUID uuid = playerProfile.getId();
                final String name = playerProfile.getName();

                if (
                        name != null
                        && uuid != null
                ) {
                    final int id =
                            playerInfoMap
                            .get(uuid, name)
                            .getID(false, false);

                    if (id != -1) {
                        completions.add(String.valueOf(id));
                    }

                    completions.add(name);
                }
            }

            return completions;
        }

        return EMPTY_TAB;
    }
}
