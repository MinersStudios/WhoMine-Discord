package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

public final class EntityExplodeListener extends EventListener {

    public EntityExplodeListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityExplode(final @NotNull EntityExplodeEvent event) {
        final World world = event.getLocation().getWorld();

        for (final var block : event.blockList()) {
            if (block.getType() == Material.NOTE_BLOCK) {
                block.setType(Material.AIR);
                world.dropItemNaturally(
                        block.getLocation(),
                        CustomBlockRegistry
                        .fromBlockData(block.getBlockData())
                        .orElse(CustomBlockData.defaultData())
                        .craftItemStack()
                );
            }
        }
    }
}
