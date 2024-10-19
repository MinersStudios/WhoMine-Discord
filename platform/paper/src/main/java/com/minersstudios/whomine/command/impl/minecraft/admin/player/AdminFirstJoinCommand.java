package com.minersstudios.whomine.command.impl.minecraft.admin.player;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.utility.MSLogger;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import com.minersstudios.wholib.paper.utility.DateUtils;
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
