package com.minersstudios.whomine.command.impl.minecraft;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.utility.Font;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public final class MSCoreCommandHandler extends PluginCommandExecutor {
    private static final List<String> TAB = Collections.singletonList("reloadconfig");

    public MSCoreCommandHandler(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("mscore")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [параметры]")
                .description("Прочие команды")
                .permission("mscore.*")
                .permissionDefault(PermissionDefault.OP)
                .commandNode(
                        literal("mscore")
                        .then(literal("reloadconfig"))
                        .build()
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
        return switch (args[0]) {
            case "reloadconfig" -> ReloadConfigCommand.runCommand(this.getPlugin(), sender);
            default -> false;
        };
    }

    @Override
    public @NotNull List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        return args.length == 1 ? TAB : EMPTY_TAB;
    }
}
