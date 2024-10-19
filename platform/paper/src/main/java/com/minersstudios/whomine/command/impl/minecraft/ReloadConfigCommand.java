package com.minersstudios.whomine.command.impl.minecraft;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.utility.MSLogger;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class ReloadConfigCommand {

    public static boolean runCommand(
            final @NotNull WhoMine plugin,
            final @NotNull CommandSender sender
    ) {
        final long time = System.currentTimeMillis();

        plugin.getConfiguration().reload();
        MSLogger.fine(
                sender,
                Translations.COMMAND_MSCORE_RELOAD_CONFIG_SUCCESS.asTranslatable()
                .arguments(text(System.currentTimeMillis() - time))
        );

        return true;
    }
}
