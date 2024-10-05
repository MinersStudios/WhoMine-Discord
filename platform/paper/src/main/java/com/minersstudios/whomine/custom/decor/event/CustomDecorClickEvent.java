package com.minersstudios.whomine.custom.decor.event;

import com.minersstudios.whomine.custom.decor.CustomDecor;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CustomDecorClickEvent extends CustomDecorEvent implements Cancellable {
    protected boolean cancelled;
    protected final Player player;
    protected final EquipmentSlot hand;
    protected final Vector clickedPosition;
    protected final Interaction clickedInteraction;
    protected final ClickType clickType;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * Constructs a new CustomBlockRightClickEvent
     *
     * @param customDecor        The custom decor involved in this event
     * @param player             The Player who right-clicked the custom decor
     * @param hand               The hand which was used to right-click
     *                           the custom decor
     * @param clickedPosition    The clicked position
     * @param clickedInteraction The clicked interaction
     * @param clickType          The click type
     */
    public CustomDecorClickEvent(
            final @NotNull CustomDecor customDecor,
            final @NotNull Player player,
            final @NotNull EquipmentSlot hand,
            final @NotNull Vector clickedPosition,
            final @NotNull Interaction clickedInteraction,
            final @NotNull ClickType clickType
    ) {
        super(customDecor);

        this.player = player;
        this.hand = hand;
        this.clickedPosition = clickedPosition;
        this.clickedInteraction = clickedInteraction;
        this.clickType = clickType;
    }

    /**
     * @return The Player who right-clicked the custom decor
     *         involved in this event
     */
    public @NotNull Player getPlayer() {
        return this.player;
    }

    /**
     * @return The hand which was used to right-click the custom decor
     */
    public @NotNull EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * @return The clicked position
     */
    public @NotNull Vector getClickedPosition() {
        return this.clickedPosition;
    }

    /**
     * @return The clicked interaction
     */
    public @NotNull Interaction getClickedInteraction() {
        return this.clickedInteraction;
    }

    /**
     * @return The click type
     */
    public @NotNull ClickType getClickType() {
        return this.clickType;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not be
     * executed in the server, but will still pass to other plugins.
     *
     * @param cancel True if you wish to cancel this event
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * @return True if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @return The handler list of this event
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * @return The handler list of this event
     */
    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Represents which type of click was used to interact with the custom decor
     * involved in this event
     */
    public enum ClickType {
        LEFT_CLICK, RIGHT_CLICK;

        /**
         * @return True, if the click type is left-click
         */
        public boolean isLeftClick() {
            return this == LEFT_CLICK;
        }

        /**
         * @return True, if the click type is right-click
         */
        public boolean isRightClick() {
            return this == RIGHT_CLICK;
        }
    }
}
