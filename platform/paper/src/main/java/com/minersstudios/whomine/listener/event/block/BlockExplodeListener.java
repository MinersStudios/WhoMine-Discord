package com.minersstudios.whomine.listener.event.block;

import com.minersstudios.wholib.event.handle.CancellableHandler;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlockData;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
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
