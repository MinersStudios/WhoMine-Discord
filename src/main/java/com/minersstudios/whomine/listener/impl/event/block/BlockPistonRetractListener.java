package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.MSDecorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockPistonRetractListener extends EventListener {

    public BlockPistonRetractListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPistonRetract(final @NotNull BlockPistonRetractEvent event) {
        for (final var block : event.getBlocks()) {
            if (MSDecorUtils.isCustomDecorMaterial(block.getType())) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
