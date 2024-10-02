package com.minersstudios.whomine.command.impl.minecraft.admin.msessentials;

import com.minersstudios.whomine.PaperCache;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class UpdateIdsCommand {

    public static boolean runCommand(
            final @NotNull WhoMine plugin,
            final @NotNull CommandSender sender
    ) {
        final long time = System.currentTimeMillis();
        final PaperCache cache = plugin.getCache();

        cache.getIdMap().reloadIds();
        cache.getPlayerInfoMap().playerInfos().forEach(PlayerInfo::initNames);
        MSLogger.fine(
                sender,
                Translations.COMMAND_MSESSENTIALS_UPDATE_IDS_SUCCESS.asTranslatable()
                .arguments(text(System.currentTimeMillis() - time))
        );

        return true;
    }
}
