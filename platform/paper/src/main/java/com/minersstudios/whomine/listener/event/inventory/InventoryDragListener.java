package com.minersstudios.whomine.listener.event.inventory;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.inventory.CustomInventory;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(InventoryDragEvent.class)
public final class InventoryDragListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.CUSTOM)
    public void onInventoryDrag(final @NotNull PaperEventContainer<InventoryDragEvent> container) {
        final InventoryDragEvent event = container.getEvent();
        if (!(event.getInventory() instanceof final CustomInventory customInventory)) {
            return;
        }

        for (final int slot : event.getRawSlots()) {
            if (slot >= 0 && slot < customInventory.getSize()) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
