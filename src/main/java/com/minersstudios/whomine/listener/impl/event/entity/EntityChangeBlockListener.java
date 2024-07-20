package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.MSDecorUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class EntityChangeBlockListener extends EventListener {

    public EntityChangeBlockListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityChangeBlock(final @NotNull EntityChangeBlockEvent event) {
        final Block block = event.getBlock();

        if (
                event.getEntity() instanceof FallingBlock
                && MSDecorUtils.isCustomDecor(block)
        ) {
            event.setCancelled(true);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(event.getTo()));
        }
    }
}
