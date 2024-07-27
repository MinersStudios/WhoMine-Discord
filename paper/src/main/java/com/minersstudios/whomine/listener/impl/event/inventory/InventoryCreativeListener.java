package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.jetbrains.annotations.NotNull;

public final class InventoryCreativeListener extends EventListener {

    public InventoryCreativeListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryCreative(final @NotNull InventoryCreativeEvent event) {
        if (!event.getClick().isCreativeAction()) {
            return;
        }

        final Player player = (Player) event.getWhoClicked();
        final Block targetBlock = PlayerUtils.getTargetBlock(player);

        if (
                targetBlock == null
                || event.getCursor().getType() != Material.NOTE_BLOCK
                || !(targetBlock.getBlockData() instanceof final NoteBlock noteBlock)
        ) {
            return;
        }

        event.setCancelled(true);
        this.getPlugin().runTask(() ->
                player.getInventory().setItem(
                        event.getSlot(),
                        CustomBlockRegistry
                        .fromNoteBlock(noteBlock)
                        .orElse(CustomBlockData.defaultData())
                        .craftItemStack()
                )
        );
    }
}
