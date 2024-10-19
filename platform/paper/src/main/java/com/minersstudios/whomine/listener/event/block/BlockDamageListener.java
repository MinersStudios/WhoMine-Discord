package com.minersstudios.whomine.listener.event.block;

import com.minersstudios.wholib.event.handle.CancellableHandler;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlock;
import com.minersstudios.wholib.paper.custom.block.CustomBlockData;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.custom.block.event.CustomBlockDamageEvent;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.BlockUtils;
import com.minersstudios.wholib.paper.world.sound.SoundGroup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(BlockDamageEvent.class)
public final class BlockDamageListener extends PaperEventListener {

    @CancellableHandler
    public void onBlockDamage(final @NotNull PaperEventContainer<BlockDamageEvent> container) {
        final BlockDamageEvent event = container.getEvent();

        final Block block = event.getBlock();
        final Material blockType = block.getType();
        final Location blockLocation = block.getLocation().toCenterLocation();

        if (
                blockType != Material.NOTE_BLOCK
                && BlockUtils.isWoodenSound(blockType)
        ) {
            SoundGroup.WOOD.playHitSound(blockLocation);
        }

        if (block.getBlockData() instanceof final NoteBlock noteBlock) {
            final Player player = event.getPlayer();
            final CustomBlockData customBlockData = CustomBlockRegistry.fromNoteBlock(noteBlock).orElse(CustomBlockData.defaultData());
            final CustomBlock customBlock = new CustomBlock(block, customBlockData);
            final CustomBlockDamageEvent damageEvent = new CustomBlockDamageEvent(customBlock, player, event.getItemInHand());

            container.getModule().getServer().getPluginManager().callEvent(damageEvent);

            if (!damageEvent.isCancelled()) {
                customBlockData.getSoundGroup().playHitSound(blockLocation);
            }
        }
    }
}
