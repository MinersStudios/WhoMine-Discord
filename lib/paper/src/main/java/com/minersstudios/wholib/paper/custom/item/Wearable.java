package com.minersstudios.wholib.paper.custom.item;

/**
 * Interface for custom items that players can wear on their head. When a player
 * clicks on a helmet slot in their inventory, the item will be placed on their
 * head. Or, if the player is shift-clicking an item in their inventory, the
 * item will be placed on their head if they are not already wearing a helmet.
 *
 * @see CustomItem
 */
public interface Wearable extends CustomItem {}
