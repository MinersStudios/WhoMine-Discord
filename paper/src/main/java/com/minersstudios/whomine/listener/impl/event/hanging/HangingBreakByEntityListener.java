package com.minersstudios.whomine.listener.impl.event.hanging;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.api.utility.SharedConstants;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.jetbrains.annotations.NotNull;

public final class HangingBreakByEntityListener extends EventListener {

    public HangingBreakByEntityListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onHangingBreakByEntity(final @NotNull HangingBreakByEntityEvent event) {
        if (
                event.getEntity() instanceof final ItemFrame itemFrame
                && itemFrame.getScoreboardTags().contains(SharedConstants.INVISIBLE_ITEM_FRAME_TAG)
                && itemFrame.isVisible()
        ) {
            itemFrame.removeScoreboardTag(SharedConstants.INVISIBLE_ITEM_FRAME_TAG);
        }
    }
}
