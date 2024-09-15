package com.minersstudios.whomine.command.impl.minecraft.player.roleplay;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.custom.decor.CustomDecorType;
import com.minersstudios.whomine.locale.Translations;
import com.minersstudios.whomine.world.location.MSPosition;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.utility.BlockUtils;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.api.utility.Font;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

import static com.minersstudios.whomine.utility.MessageUtils.RolePlayActionType.ME;
import static com.minersstudios.whomine.utility.MessageUtils.RolePlayActionType.TODO;
import static com.minersstudios.whomine.utility.MessageUtils.sendRPEventMessage;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.kyori.adventure.text.Component.text;

public final class FartCommand extends PluginCommandExecutor {
    private final SecureRandom random;

    public FartCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("fart")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command> [речь]")
                .description("Пукни вкусно на публику")
                .playerOnly(true)
                .commandNode(
                        literal("fart")
                        .then(argument("speech", StringArgumentType.greedyString()))
                        .build()
                )
                .build()
        );

        this.random = new SecureRandom();
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        final Player player = (Player) sender;
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getPlugin(), player);

        if (playerInfo.isMuted()) {
            MSLogger.warning(
                    player,
                    Translations.COMMAND_MUTE_ALREADY_RECEIVER.asTranslatable()
            );

            return true;
        }

        final Location location = player.getLocation();
        final World world = player.getWorld();
        boolean withPoop =
                this.random.nextInt(10) == 0
                && location.clone().subtract(0.0d, 0.5d, 0.0d).getBlock().getType().isSolid()
                && BlockUtils.isReplaceable(location.clone().getBlock().getType());

        for (final var nearbyEntity : world.getNearbyEntities(location.getBlock().getLocation().add(0.5d, 0.5d, 0.5d), 0.5d, 0.5d, 0.5d)) {
            final EntityType entityType = nearbyEntity.getType();

            if (
                    entityType != EntityType.ITEM
                    && entityType != EntityType.PLAYER
            ) {
                withPoop = false;

                break;
            }
        }

        world.playSound(location.add(0, 0.4, 0), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1);
        world.spawnParticle(Particle.DUST, location, 15, 0.0D, 0.0D, 0.0D, 0.5D, new Particle.DustOptions(Color.fromBGR(33, 54, 75), 10));

        if (withPoop) {
            CustomDecorType.POOP.getCustomDecorData()
            .place(
                    this.getPlugin(),
                    MSPosition.of(location.toBlockLocation()),
                    player,
                    BlockFace.UP,
                    null,
                    ChatUtils.createDefaultStyledText("Какашка " + ChatUtils.serializeLegacyComponent(playerInfo.getDefaultName()))
            );
        }

        if (args.length > 0) {
            sendRPEventMessage(
                    player,
                    text(ChatUtils.extractMessage(args, 0)),
                    text(withPoop ? "пукнув с подливой" : "пукнув"),
                    TODO
            );
            return true;
        }

        sendRPEventMessage(
                player,
                playerInfo.getPlayerFile().getPronouns()
                        .getFartMessage()
                        .append(text(withPoop ? " с подливой" : "")),
                ME
        );

        return true;
    }
}
