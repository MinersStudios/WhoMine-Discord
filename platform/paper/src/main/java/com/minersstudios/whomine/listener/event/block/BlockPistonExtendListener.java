package com.minersstudios.whomine.listener.event.block;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.MSDecorUtils;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
