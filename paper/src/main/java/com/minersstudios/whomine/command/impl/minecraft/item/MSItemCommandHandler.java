package com.minersstudios.whomine.command.impl.minecraft.item;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.custom.item.CustomItemType;
import com.minersstudios.whomine.api.utility.Font;
import com.minersstudios.whomine.utility.MSPlayerUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public final class MSItemCommandHandler extends PluginCommandExecutor {
    private static final List<String> TAB = Arrays.asList("reload", "give");

    public MSItemCommandHandler(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("msitem")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [параметры]")
                .description("Прочие команды")
                .permission("msitem.*")
                .permissionDefault(PermissionDefault.OP)
                .commandNode(
                        literal("msitem")
                        .then(literal("reload"))
                        .then(
                                literal("give")
                                .then(
                                        argument("id/nickname", StringArgumentType.word())
                                        .then(
                                                argument("item id", StringArgumentType.word())
                                                .then(argument("amount", IntegerArgumentType.integer()))
                                        )
                                )
                        )
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
                    case "reload" -> ReloadCommand.runCommand(this.getPlugin(), sender);
                    case "give" -> GiveCommand.runCommand(this.getPlugin(), sender, args);
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
        return switch (args.length) {
            case 1 -> TAB;
            case 2 -> args[0].equals("give")
                    ? MSPlayerUtils.getLocalPlayerNames(this.getPlugin())
                    : EMPTY_TAB;
            case 3 -> args[0].equals("give")
                    ? new ObjectArrayList<>(CustomItemType.keySet())
                    : EMPTY_TAB;
            default -> EMPTY_TAB;
        };
    }
}
