package com.minersstudios.whomine.command.impl.minecraft.admin.msessentials;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.utility.MSLogger;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class UpdateMutesCommand {

    public static boolean runCommand(
            final @NotNull WhoMine plugin,
            final @NotNull CommandSender sender
    ) {
        final long time = System.currentTimeMillis();

        plugin.getCache().getMuteMap().reloadMutes();
        MSLogger.fine(
                sender,
                Translations.COMMAND_MSESSENTIALS_UPDATE_MUTES_SUCCESS.asTranslatable()
                .arguments(text(System.currentTimeMillis() - time))
        );

        return true;
    }
}
