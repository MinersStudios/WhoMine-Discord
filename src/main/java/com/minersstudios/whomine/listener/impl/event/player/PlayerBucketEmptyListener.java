package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.MSDecorUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerBucketEmptyListener extends EventListener {

    public PlayerBucketEmptyListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBucketEmpty(final @NotNull PlayerBucketEmptyEvent event) {
        final Block block = event.getBlock();

        if (
                block.getType() == Material.NOTE_BLOCK
                || CustomBlockRegistry.isCustomBlock(event.getPlayer().getInventory().getItemInMainHand())
                || (
                        event.getBucket() == Material.LAVA_BUCKET
                        && MSDecorUtils.isCustomDecor(block)
                )
        ) {
            event.setCancelled(true);
        }
    }
}
