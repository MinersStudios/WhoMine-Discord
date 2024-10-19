package com.minersstudios.whomine.listener.event.hanging;

import com.minersstudios.wholib.event.handle.CancellableHandler;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.utility.SharedConstants;
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
