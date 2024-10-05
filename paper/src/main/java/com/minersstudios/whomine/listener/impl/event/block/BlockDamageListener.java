package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.api.event.EventHandler;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.custom.block.CustomBlock;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.custom.block.event.CustomBlockDamageEvent;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.BlockUtils;
import com.minersstudios.whomine.world.sound.SoundGroup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(BlockDamageEvent.class)
public final class BlockDamageListener extends PaperEventListener {

    @EventHandler
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
