package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.block.BlockExplodeEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(BlockExplodeEvent.class)
public final class BlockExplodeListener extends PaperEventListener {

    @CancellableHandler
    public void onBlockExplode(final @NotNull PaperEventContainer<BlockExplodeEvent> container) {
        final BlockExplodeEvent event = container.getEvent();
        final World world = event.getBlock().getWorld();

        for (final var block : event.blockList()) {
            if (block.getType() == Material.NOTE_BLOCK) {
                block.setType(Material.AIR);
                world.dropItemNaturally(
                        block.getLocation(),
                        CustomBlockRegistry
                        .fromNoteBlock((NoteBlock) block.getBlockData())
                        .orElse(CustomBlockData.defaultData())
                        .craftItemStack()
                );
            }
        }
    }
}
