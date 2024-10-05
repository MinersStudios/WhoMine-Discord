package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.MSDecorUtils;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(BlockPistonExtendEvent.class)
public final class BlockPistonExtendListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.CUSTOM, ignoreCancelled = true)
    public void onBlockPistonExtend(final @NotNull PaperEventContainer<BlockPistonExtendEvent> container) {
        final BlockPistonExtendEvent event = container.getEvent();

        for (final var block : event.getBlocks()) {
            if (MSDecorUtils.isCustomDecorMaterial(block.getType())) {
                event.setCancelled(true);

                break;
            }
        }
    }
}
