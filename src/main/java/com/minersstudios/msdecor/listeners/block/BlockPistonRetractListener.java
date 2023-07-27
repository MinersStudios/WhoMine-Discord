package com.minersstudios.msdecor.listeners.block;

import com.minersstudios.mscore.listener.event.MSListener;
import com.minersstudios.mscore.util.MSDecorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import com.minersstudios.mscore.listener.event.AbstractMSListener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class BlockPistonRetractListener extends AbstractMSListener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPistonRetract(@NotNull BlockPistonRetractEvent event) {
        for (var block : event.getBlocks()) {
            if (MSDecorUtils.isCustomDecorMaterial(block.getType())) {
                event.setCancelled(true);
            }
        }
    }
}
