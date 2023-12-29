package com.minersstudios.msessentials.command.impl.minecraft.admin.player;

import com.minersstudios.mscore.language.LanguageRegistry;
import com.minersstudios.mscore.plugin.MSLogger;
import com.minersstudios.msessentials.player.PlayerInfo;
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
                LanguageRegistry.Components.COMMAND_PLAYER_UPDATE_SUCCESS
                .arguments(
                        playerInfo.getGrayIDGreenName(),
                        text(playerInfo.getNickname())
                )
        );

        return true;
    }
}
