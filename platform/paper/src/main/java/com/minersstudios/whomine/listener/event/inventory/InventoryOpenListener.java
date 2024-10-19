package com.minersstudios.whomine.listener.event.inventory;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.inventory.CustomInventory;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(InventoryOpenEvent.class)
public final class InventoryOpenListener extends PaperEventListener {

    @CancellableHandler
    public void onInventoryOpen(final @NotNull PaperEventContainer<InventoryOpenEvent> container) {
        final InventoryOpenEvent event = container.getEvent();

        if (event.getInventory() instanceof final CustomInventory customInventory) {
            customInventory.doOpenAction(event);
        }
    }
}
