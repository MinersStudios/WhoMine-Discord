package com.minersstudios.whomine.command.impl.minecraft.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.menu.SkinsMenu;
import com.minersstudios.whomine.api.utility.Font;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SkinsCommand extends PluginCommandExecutor {

    public SkinsCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("skins")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command>")
                .description("Открывает меню с крафтами кастомных предметов/декора/блоков")
                .playerOnly(true)
                .commandNode(
                        LiteralArgumentBuilder.literal("skins").build()
                )
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
        this.getModule().openCustomInventory(
                SkinsMenu.class,
                (Player) sender
        );

        return true;
    }
}
