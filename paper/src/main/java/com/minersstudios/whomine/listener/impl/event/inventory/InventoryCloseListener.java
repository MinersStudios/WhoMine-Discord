package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public final class InventoryCloseListener extends EventListener {

    public InventoryCloseListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        if (event.getInventory() instanceof final CustomInventory customInventory) {
            customInventory.doCloseAction(event);
        }
    }
}
