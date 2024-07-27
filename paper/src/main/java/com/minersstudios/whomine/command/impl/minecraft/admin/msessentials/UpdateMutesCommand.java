package com.minersstudios.whomine.command.impl.minecraft.admin.msessentials;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.locale.Translations;
import com.minersstudios.whomine.utility.MSLogger;
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
