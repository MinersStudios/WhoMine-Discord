package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public final class InventoryOpenListener extends EventListener {

    public InventoryOpenListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryOpen(final @NotNull InventoryOpenEvent event) {
        if (event.getInventory() instanceof final CustomInventory customInventory) {
            customInventory.doOpenAction(event);
        }
    }
}
