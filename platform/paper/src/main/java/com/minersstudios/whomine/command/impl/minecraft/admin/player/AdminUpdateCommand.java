package com.minersstudios.whomine.command.impl.minecraft.admin.player;

import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.utility.MSLogger;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class AdminUpdateCommand {

    public static boolean runCommand(
            final @NotNull CommandSender sender,
            final @NotNull PlayerInfo playerInfo
    ) {
        playerInfo.update();
        MSLogger.fine(
                sender,
                Translations.COMMAND_PLAYER_UPDATE_SUCCESS.asTranslatable()
                .arguments(
                        playerInfo.getGrayIDGreenName(),
                        text(playerInfo.getNickname())
                )
        );

        return true;
    }
}
