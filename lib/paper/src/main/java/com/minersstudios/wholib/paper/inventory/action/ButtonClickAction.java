package com.minersstudios.wholib.paper.inventory.action;

import com.minersstudios.wholib.paper.inventory.CustomInventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an action performed when the button is clicked
 */
@FunctionalInterface
public interface ButtonClickAction {

    /**
     * Do action when button is clicked
     *
     * @param event           Click event that triggered the action
     * @param singleInventory Custom inventory that is involved in this event
     */
    void doAction(
            final @NotNull InventoryClickEvent event,
            final @NotNull CustomInventory singleInventory
    );
}
