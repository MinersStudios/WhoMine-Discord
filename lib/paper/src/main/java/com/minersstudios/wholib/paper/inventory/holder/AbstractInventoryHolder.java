package com.minersstudios.wholib.paper.inventory.holder;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.inventory.CustomInventory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

/**
 * This class used for extending custom inventories with the
 * {@link InventoryHolder} annotation
 *
 * @see InventoryHolder
 */
public abstract class AbstractInventoryHolder {

    private final String id;
    private CustomInventory customInventory;
    private WhoMine plugin;

    /**
     * Creates a new abstract inventory holder with the given id
     *
     * @param id The id of this inventory holder
     */
    public AbstractInventoryHolder(final @NotNull String id) {
        this.id = id;
    }

    /**
     * @return The id of this inventory holder
     */
    public final @NotNull String getId() {
        return this.id;
    }

    /**
     * @return The custom inventory, that this custom inventory is registered to
     */
    public final @UnknownNullability CustomInventory getCustomInventory() {
        return this.customInventory;
    }

    /**
     * @return The plugin, that this custom inventory is registered to
     */
    public final @UnknownNullability WhoMine getPlugin() throws IllegalStateException {
        if (!this.isRegistered()) {
            throw new IllegalStateException("Custom inventory " + this + " not registered!");
        }

        return this.plugin;
    }

    /**
     * @return Whether this custom inventory is registered or not
     */
    public final boolean isRegistered() {
        return this.plugin != null;
    }

    /**
     * Registers this custom inventory to the given plugin
     *
     * @param plugin The plugin to register this custom inventory to
     * @throws IllegalStateException If this custom inventory is already
     *                               registered
     */
    @ApiStatus.Internal
    public final void register(final @NotNull WhoMine plugin) throws IllegalStateException {
        if (this.isRegistered()) {
            throw new IllegalStateException("Custom inventory " + this + " already registered!");
        }

        this.plugin = plugin;
        this.customInventory = this.createCustomInventory();

        plugin.getGuiManager().register(this);
    }

    /**
     * Creates a new custom inventory
     *
     * @return The created custom inventory
     */
    protected abstract @Nullable CustomInventory createCustomInventory();

    /**
     * Opens this custom inventory for the given player
     *
     * @param player The player to open this custom inventory for
     */
    public abstract void open(final @NotNull Player player);

    /**
     * @return A string representation of this custom inventory
     */
    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + "{plugin=" + this.plugin + '}';
    }
}
