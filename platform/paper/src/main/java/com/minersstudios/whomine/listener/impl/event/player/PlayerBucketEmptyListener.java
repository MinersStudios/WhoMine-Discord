package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.MSDecorUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerBucketEmptyEvent.class)
public final class PlayerBucketEmptyListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.CUSTOM, ignoreCancelled = true)
    public void onPlayerBucketEmpty(final @NotNull PaperEventContainer<PlayerBucketEmptyEvent> container) {
        final PlayerBucketEmptyEvent event = container.getEvent();
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
