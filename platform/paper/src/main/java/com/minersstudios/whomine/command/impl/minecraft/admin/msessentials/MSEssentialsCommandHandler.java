package com.minersstudios.whomine.command.impl.minecraft.admin.msessentials;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.wholib.utility.Font;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public final class MSEssentialsCommandHandler extends PluginCommandExecutor {
    private static final List<String> TAB = Arrays.asList("reload", "updateids", "updatemutes");

    public MSEssentialsCommandHandler(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("msessentials")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [параметры]")
                .description("Прочие команды")
                .permission("msessentials.*")
                .permissionDefault(PermissionDefault.OP)
                .permissionChildren(
                        CommandData.permissionEntry("msessentials.player.*", true),
                        CommandData.permissionEntry("msessentials.ban", true),
                        CommandData.permissionEntry("msessentials.mute", true),
                        CommandData.permissionEntry("msessentials.kick", true),
                        CommandData.permissionEntry("msessentials.maplocation", true),
                        CommandData.permissionEntry("msessentials.whitelist", true),
                        CommandData.permissionEntry("msessentials.teleporttolastdeathlocation", true),
                        CommandData.permissionEntry("msessentials.worldteleport", true),
                        CommandData.permissionEntry("msessentials.setserverspawn", true)
                )
                .commandNode(
                        literal("msessentials")
                        .then(literal("reload"))
                        .then(literal("updateids"))
                        .then(literal("updatemutes"))
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
        return args.length != 0
                && switch (args[0]) {
                    case "reload" ->      ReloadCommand.runCommand(this.getModule(), sender);
                    case "updateids" ->   UpdateIdsCommand.runCommand(this.getModule(), sender);
                    case "updatemutes" -> UpdateMutesCommand.runCommand(this.getModule(), sender);
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
