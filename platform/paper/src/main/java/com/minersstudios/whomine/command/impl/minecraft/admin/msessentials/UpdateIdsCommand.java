package com.minersstudios.whomine.command.impl.minecraft.admin.msessentials;

import com.minersstudios.wholib.paper.PaperCache;
import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.utility.MSLogger;
import com.minersstudios.wholib.paper.player.PlayerInfo;
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
