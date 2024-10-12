package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
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
