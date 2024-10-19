package com.minersstudios.whomine.listener.event.entity;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlockData;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import org.bukkit.Material;
import org.bukkit.World;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(EntityExplodeEvent.class)
public final class EntityExplodeListener extends PaperEventListener {

    @CancellableHandler
    public void onEntityExplode(final @NotNull PaperEventContainer<EntityExplodeEvent> container) {
        final EntityExplodeEvent event = container.getEvent();
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
