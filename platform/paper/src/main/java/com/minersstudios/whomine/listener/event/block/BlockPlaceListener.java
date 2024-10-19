package com.minersstudios.whomine.listener.event.block;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlock;
import com.minersstudios.wholib.paper.custom.block.CustomBlockData;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.BlockUtils;
import com.minersstudios.wholib.paper.utility.MSDecorUtils;
import com.minersstudios.wholib.paper.world.sound.SoundGroup;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(BlockPlaceEvent.class)
public final class BlockPlaceListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.CUSTOM)
    public void onBlockPlace(final @NotNull PaperEventContainer<BlockPlaceEvent> container) {
        final BlockPlaceEvent event = container.getEvent();
        final Player player = event.getPlayer();
        final Block block = event.getBlockPlaced();
        final Material blockType = block.getType();

        if (
                (
                        !BlockUtils.isReplaceable(block)
                        || MSDecorUtils.isCustomDecorMaterial(event.getBlockReplacedState().getType())
                )
                && MSDecorUtils.isCustomDecor(block)
        ) {
            event.setCancelled(true);
        }

        if (
                blockType == Material.NOTE_BLOCK
                || CustomBlockRegistry.isCustomBlock(player.getInventory().getItemInMainHand())
        ) {
            event.setCancelled(true);
        }

        if (
                blockType != Material.NOTE_BLOCK
                && BlockUtils.isWoodenSound(blockType)
        ) {
            SoundGroup.WOOD.playPlaceSound(block.getLocation().toCenterLocation());
        }

        if (blockType == Material.NOTE_BLOCK) {
            new CustomBlock(block, CustomBlockData.defaultData())
                    .place(container.getModule(), player, event.getHand());
        }
    }
}
