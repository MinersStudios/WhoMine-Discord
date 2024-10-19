package com.minersstudios.whomine.command.impl.minecraft.item;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.menu.RenamesMenu;
import com.minersstudios.wholib.utility.Font;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public final class RenamesCommand extends PluginCommandExecutor {

    public RenamesCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("renames")
                .aliases(
                        "optifine",
                        "rename",
                        "renameables"
                )
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command>")
                .description("Открывает меню с переименованиями предметов")
                .playerOnly(true)
                .commandNode(literal("renames").build())
                .build()
        );
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        RenamesMenu.open((Player) sender);

        return true;
    }
}
