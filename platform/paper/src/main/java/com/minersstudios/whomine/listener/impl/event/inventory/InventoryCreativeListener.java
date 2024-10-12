package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(InventoryCreativeEvent.class)
public final class InventoryCreativeListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.CUSTOM)
    public void onInventoryCreative(final @NotNull PaperEventContainer<InventoryCreativeEvent> container) {
        final InventoryCreativeEvent event = container.getEvent();

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
        container.getModule().runTask(() ->
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
