package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlock;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.custom.block.event.CustomBlockDamageEvent;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.BlockUtils;
import com.minersstudios.whomine.world.sound.SoundGroup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockDamageListener extends EventListener {

    public BlockDamageListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockDamage(final @NotNull BlockDamageEvent event) {
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

            this.getPlugin().getServer().getPluginManager().callEvent(damageEvent);

            if (!damageEvent.isCancelled()) {
                customBlockData.getSoundGroup().playHitSound(blockLocation);
            }
        }
    }
}
