package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockExplodeListener extends EventListener {

    public BlockExplodeListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockExplode(final @NotNull BlockExplodeEvent event) {
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
