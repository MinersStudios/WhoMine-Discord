package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(InventoryCloseEvent.class)
public final class InventoryCloseListener extends PaperEventListener {

    @EventHandler
    public void onInventoryClose(final @NotNull PaperEventContainer<InventoryCloseEvent> container) {
        final InventoryCloseEvent event = container.getEvent();

        if (event.getInventory() instanceof final CustomInventory customInventory) {
            customInventory.doCloseAction(event);
        }
    }
}
