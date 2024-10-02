package com.minersstudios.whomine.command.impl.minecraft.player.roleplay;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.api.utility.Font;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.utility.MessageUtils.RolePlayActionType.ME;
import static com.minersstudios.whomine.utility.MessageUtils.RolePlayActionType.TODO;
import static com.minersstudios.whomine.utility.MessageUtils.sendRPEventMessage;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

public final class SpitCommand extends PluginCommandExecutor {

    public SpitCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("spit")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [речь]")
                .description("Покажи свою дерзость и плюнь кому-то в лицо")
                .playerOnly(true)
                .commandNode(
                        literal("spit")
                        .then(argument("speech", StringArgumentType.greedyString()))
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
        final Player player = (Player) sender;
        final World world = player.getWorld();
        final Location location = player.getLocation();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getModule(), player);

        if (playerInfo.isMuted()) {
            MSLogger.warning(
                    player,
                    Translations.COMMAND_MUTE_ALREADY_RECEIVER.asTranslatable()
            );

            return true;
        }

        world.spawnEntity(
                location.toVector().add(location.getDirection().multiply(0.8d)).toLocation(world).add(0.0d, 1.0d, 0.0d),
                EntityType.LLAMA_SPIT
        ).setVelocity(player.getEyeLocation().getDirection().multiply(1));
        world.playSound(location, Sound.ENTITY_LLAMA_SPIT, SoundCategory.PLAYERS, 1.0f, 1.0f);

        if (args.length > 0) {
            sendRPEventMessage(player, text(ChatUtils.extractMessage(args, 0)), text("плюнув"), TODO);

            return true;
        }

        sendRPEventMessage(player, playerInfo.getPlayerFile().getPronouns().getSpitMessage(), ME);

        return true;
    }
}
