package com.minersstudios.msblock.commands;

import com.minersstudios.msblock.MSBlock;
import com.minersstudios.msblock.customblock.CustomBlockRegistry;
import com.minersstudios.mscore.logger.MSLogger;
import com.minersstudios.mscore.plugin.MSPlugin;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Keyed;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class ReloadCommand {
    private static final TranslatableComponent RELOAD_SUCCESS = translatable("ms.command.msblock.reload.success");

    public static boolean runCommand(@NotNull CommandSender sender) {
        long time = System.currentTimeMillis();
        Server server = sender.getServer();
        Iterator<Recipe> crafts = server.recipeIterator();

        while (crafts.hasNext()) {
            Recipe recipe = crafts.next();

            if (
                    recipe instanceof Keyed keyed
                    && CustomBlockRegistry.NAMESPACE.equals(keyed.getKey().getNamespace())
            ) {
                server.removeRecipe(keyed.getKey());
            }
        }

        MSPlugin.getGlobalCache().customBlockRecipes.clear();
        CustomBlockRegistry.unregisterAll();
        MSBlock.getConfiguration().reload();
        MSLogger.fine(sender, RELOAD_SUCCESS.args(text(System.currentTimeMillis() - time)));
        return true;
    }
}
