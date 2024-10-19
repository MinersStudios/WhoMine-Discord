package com.minersstudios.whomine.listener.event.block;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.MSDecorUtils;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(BlockPistonRetractEvent.class)
public final class BlockPistonRetractListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.CUSTOM, ignoreCancelled = true)
    public void onBlockPistonRetract(final @NotNull PaperEventContainer<BlockPistonRetractEvent> container) {
        final BlockPistonRetractEvent event = container.getEvent();

        for (final var block : event.getBlocks()) {
            if (MSDecorUtils.isCustomDecorMaterial(block.getType())) {
                event.setCancelled(true);

                break;
            }
        }
    }
}
