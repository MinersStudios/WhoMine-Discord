package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

public final class InventoryDragListener extends EventListener {

    public InventoryDragListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryDrag(final @NotNull InventoryDragEvent event) {
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
