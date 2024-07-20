package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerMoveListener extends EventListener {

    public PlayerMoveListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMove(final @NotNull PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Block block = player.getLocation().subtract(0.0d, 0.15d, 0.0d).getBlock();

        if (this.getPlugin().getCache().getWorldDark().isInWorldDark(event.getFrom())) {
            event.setCancelled(true);
        }

        if (
                player.getGameMode() != GameMode.SPECTATOR
                && !player.isFlying()
                && !player.isSneaking()
        ) {
            final double distance = event.getFrom().distance(event.getTo());

            if (
                    distance != 0.0d
                    && this.getPlugin().getCache().getStepMap().addDistance(player, distance)
                    && BlockUtils.isWoodenSound(block.getType())
            ) {
                final Location stepLocation = block.getLocation().toCenterLocation();

                CustomBlockRegistry.fromBlockData(block.getBlockData())
                .orElse(CustomBlockData.defaultData())
                .getSoundGroup().playStepSound(stepLocation);
            }
        }
    }
}
