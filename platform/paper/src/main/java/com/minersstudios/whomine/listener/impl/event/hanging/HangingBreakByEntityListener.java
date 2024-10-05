package com.minersstudios.whomine.listener.impl.event.hanging;

import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.utility.SharedConstants;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(HangingBreakByEntityEvent.class)
public final class HangingBreakByEntityListener extends PaperEventListener {

    @CancellableHandler
    public void onHangingBreakByEntity(final @NotNull PaperEventContainer<HangingBreakByEntityEvent> container) {
        if (
                container.getEvent().getEntity() instanceof final ItemFrame itemFrame
                && itemFrame.getScoreboardTags().contains(SharedConstants.INVISIBLE_ITEM_FRAME_TAG)
                && itemFrame.isVisible()
        ) {
            itemFrame.removeScoreboardTag(SharedConstants.INVISIBLE_ITEM_FRAME_TAG);
        }
    }
}
