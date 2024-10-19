package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.MSDecorUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
