package com.minersstudios.whomine.listener.event.inventory;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.inventory.CustomInventory;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(InventoryCloseEvent.class)
public final class InventoryCloseListener extends PaperEventListener {

    @CancellableHandler
    public void onInventoryClose(final @NotNull PaperEventContainer<InventoryCloseEvent> container) {
        final InventoryCloseEvent event = container.getEvent();

        if (event.getInventory() instanceof final CustomInventory customInventory) {
            customInventory.doCloseAction(event);
        }
    }
}
