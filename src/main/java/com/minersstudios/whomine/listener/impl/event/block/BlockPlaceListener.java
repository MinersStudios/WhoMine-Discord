package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlock;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.BlockUtils;
import com.minersstudios.whomine.utility.MSDecorUtils;
import com.minersstudios.whomine.world.sound.SoundGroup;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public final class BlockPlaceListener extends EventListener {

    public BlockPlaceListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final @NotNull BlockPlaceEvent event) {
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
                    .place(this.getPlugin(), player, event.getHand());
        }
    }
}
