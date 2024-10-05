package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
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
