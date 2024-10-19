package com.minersstudios.wholib.paper.gui;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.gui.GuiManager;
import com.minersstudios.wholib.paper.inventory.holder.AbstractInventoryHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class PaperGuiManager extends GuiManager<WhoMine> {

    private final Map<String, AbstractInventoryHolder> inventoryHolderMap;

    public PaperGuiManager(final @NotNull WhoMine module) {
        super(module);

        this.inventoryHolderMap = new Object2ObjectOpenHashMap<>();
    }

    public @NotNull @UnmodifiableView Map<String, AbstractInventoryHolder> getInventoryHolderMap() {
        return Collections.unmodifiableMap(this.inventoryHolderMap);
    }

    public @NotNull Optional<AbstractInventoryHolder> getInventoryHolder(final @NotNull String id) {
        return Optional.ofNullable(this.inventoryHolderMap.get(id));
    }

    public void open(
            final @NotNull String id,
            final @NotNull Player player
    ) {
        this.getInventoryHolder(id)
            .ifPresent(
                    holder -> holder.open(player)
            );
    }

    public void register(final @NotNull AbstractInventoryHolder holder) {
        this.inventoryHolderMap.put(holder.getId(), holder);
    }
}
