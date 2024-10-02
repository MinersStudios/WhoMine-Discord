package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.MSDecorUtils;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(eventClass = BlockPistonRetractEvent.class)
public final class BlockPistonRetractListener extends PaperEventListener {

    @EventHandler(priority = EventOrder.CUSTOM, ignoreCancelled = true)
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
