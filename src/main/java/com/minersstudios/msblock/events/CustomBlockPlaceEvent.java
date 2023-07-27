package com.minersstudios.msblock.events;

import com.minersstudios.msblock.customblock.CustomBlock;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class CustomBlockPlaceEvent extends CustomBlockEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected final BlockState replacedBlockState;
    protected final Player player;
    protected final EquipmentSlot hand;
    protected boolean cancelled;

    /**
     * Constructs a new CustomBlockPlaceEvent
     *
     * @param customBlock        The custom block involved in this event
     * @param replacedBlockState The BlockState for the block which was
     *                           replaced
     * @param player             The player who placed the custom block
     *                           involved in this event
     * @param hand               Main or off-hand, depending on which hand
     *                           was used to place the custom block
     */
    public CustomBlockPlaceEvent(
            @NotNull CustomBlock customBlock,
            @NotNull BlockState replacedBlockState,
            @NotNull Player player,
            @NotNull EquipmentSlot hand
    ) {
        super(customBlock);
        this.player = player;
        this.replacedBlockState = replacedBlockState;
        this.hand = hand;
    }

    /**
     * @return The Player who placed the custom block
     *         involved in this event
     */
    public @NotNull Player getPlayer() {
        return this.player;
    }

    /**
     * @return The BlockState for the block which was replaced.
     *         Material type air mostly.
     */
    public @NotNull BlockState getBlockReplacedState() {
        return this.replacedBlockState;
    }

    /**
     * @return Main or off-hand, depending on which hand
     *         was used to place the custom block
     */
    public @NotNull EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Sets the cancellation state of this event. A cancelled
     * event will not be executed in the server, but will still
     * pass to other plugins.
     *
     * @param cancel True if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
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
}
