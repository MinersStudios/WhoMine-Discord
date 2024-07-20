package com.minersstudios.whomine.command.impl.minecraft.block;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.locale.Translations;
import com.minersstudios.whomine.utility.MSLogger;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class ReloadCommand {

    public static boolean runCommand(
            final @NotNull WhoMine plugin,
            final @NotNull CommandSender sender
    ) {
        final long time = System.currentTimeMillis();

        for (final var data : CustomBlockRegistry.customBlockDataCollection()) {
            data.unregisterRecipes(plugin);
        }

        CustomBlockRegistry.unregisterAll();
        plugin.getConfiguration().reload();
        MSLogger.fine(
                sender,
                Translations.COMMAND_MSBLOCK_RELOAD_SUCCESS.asTranslatable()
                .arguments(text(System.currentTimeMillis() - time))
        );

        return true;
    }
}
