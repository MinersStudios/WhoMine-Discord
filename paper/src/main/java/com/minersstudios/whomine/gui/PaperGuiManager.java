package com.minersstudios.whomine.gui;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.gui.GuiManager;
import com.minersstudios.whomine.inventory.holder.AbstractInventoryHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class PaperGuiManager extends GuiManager<WhoMine> {

    private final Map<Class<? extends AbstractInventoryHolder>, AbstractInventoryHolder> inventoryHolderMap;

    public PaperGuiManager(final @NotNull WhoMine module) {
        super(module);

        this.inventoryHolderMap = new Object2ObjectOpenHashMap<>();
    }

    /**
     * Returns an unmodifiable view of the inventory holder map
     *
     * @return An unmodifiable view of the inventory holder map
     */
    public @NotNull @UnmodifiableView Map<Class<? extends AbstractInventoryHolder>, AbstractInventoryHolder> getInventoryHolderMap() {
        return Collections.unmodifiableMap(this.inventoryHolderMap);
    }

    /**
     * Gets the inventory holder of the specified class if present, otherwise an
     * empty optional will be returned
     *
     * @param clazz Class of the inventory holder
     * @return An optional containing the inventory holder if present, otherwise
     *         an empty optional
     */
    public @NotNull Optional<AbstractInventoryHolder> getInventoryHolder(final @NotNull Class<? extends AbstractInventoryHolder> clazz) {
        return Optional.ofNullable(this.inventoryHolderMap.get(clazz));
    }

    /**
     * Opens a custom inventory for the given player if the custom inventory is
     * registered to the plugin
     *
     * @param clazz  Class of the custom inventory holder
     * @param player Player to open the custom inventory
     * @see AbstractInventoryHolder
     */
    public void open(
            final @NotNull Class<? extends AbstractInventoryHolder> clazz,
            final @NotNull Player player
    ) {
        this.getInventoryHolder(clazz)
            .ifPresent(
                    holder -> holder.open(player)
            );
    }

    public void register(final @NotNull AbstractInventoryHolder holder) {
        this.inventoryHolderMap.put(holder.getClass(), holder);
    }
}
