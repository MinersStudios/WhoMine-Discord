package com.minersstudios.whomine.command.impl.minecraft.player;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.wholib.paper.discord.BotHandler;
import com.minersstudios.wholib.paper.discord.DiscordManager;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import com.minersstudios.wholib.utility.ChatUtils;
import com.minersstudios.wholib.utility.Font;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.minersstudios.wholib.locale.Translations.*;
import static com.minersstudios.wholib.utility.SharedConstants.DISCORD_LINK;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;

public final class DiscordCommand extends PluginCommandExecutor {
    private static final List<String> TAB = Arrays.asList("link", "unlink");
    private static final TranslatableComponent DISCORD_MESSAGE =
            COMMAND_DISCORD.asTranslatable()
            .arguments(
                    text(DISCORD_LINK)
                    .hoverEvent(showText(COMMAND_HOVER_SUGGEST.asTranslatable().color(NamedTextColor.GRAY)))
                    .clickEvent(ClickEvent.openUrl(DISCORD_LINK)),
                    text("/discord link")
                    .hoverEvent(showText(COMMAND_HOVER_RUN.asTranslatable().color(NamedTextColor.GRAY)))
                    .clickEvent(ClickEvent.runCommand("/discord link"))
            );

    public DiscordCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("discord")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [параметры]")
                .description("Дискорд команды")
                .playerOnly(true)
                .commandNode(
                        literal("discord")
                        .then(literal("link"))
                        .then(literal("unlink"))
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
        final WhoMine plugin = this.getModule();
        final Player player = (Player) sender;

        if (args.length > 0) {
            switch (args[0]) {
                case "link" -> plugin.getGuiManager().open("discord_link_code", player);
                case "unlink" -> {
                    final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
                    final long id = playerInfo.unlinkDiscord();

                    if (id == -1L) {
                        MSLogger.warning(
                                sender,
                                COMMAND_DISCORD_UNLINK_NO_LINKS.asTranslatable()
                        );

                        return true;
                    }

                    final DiscordManager discordModule = plugin.getDiscordModule();

                    discordModule.retrieveUser(id)
                    .ifPresent(user -> {
                        discordModule.sendEmbeds(
                                user,
                                BotHandler.craftEmbed(
                                        ChatUtils.serializePlainComponent(
                                                COMMAND_DISCORD_UNLINK_DISCORD_SUCCESS
                                                .asComponent(
                                                        playerInfo.getDefaultName(),
                                                        text(player.getName())
                                                )
                                        )
                                )
                        );
                        MSLogger.fine(
                                player,
                                COMMAND_DISCORD_UNLINK_MINECRAFT_SUCCESS.asTranslatable()
                                .arguments(text(user.getName()))
                        );
                    });
                }
                default -> {
                    return false;
                }
            }
        } else {
            MSLogger.warning(sender, DISCORD_MESSAGE);
        }

        return true;
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
