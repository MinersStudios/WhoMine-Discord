package com.minersstudios.whomine.command.impl.minecraft.admin.player;

import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.utility.DateUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class AdminFirstJoinCommand {

    public static boolean runCommand(
            final @NotNull WhoMine plugin,
            final @NotNull CommandSender sender,
            final @NotNull PlayerInfo playerInfo
    ) {
        MSLogger.fine(
                sender,
                Translations.COMMAND_PLAYER_FIRST_JOIN.asTranslatable()
                .arguments(
                        playerInfo.getGrayIDGreenName(),
                        text(playerInfo.getNickname()),
                        text(DateUtils.getSenderDate(
                                playerInfo.getPlayerFile().getFirstJoin(),
                                sender,
                                plugin.getConfiguration().getDateFormatter()
                        ))
                )
        );

        return true;
    }
}
