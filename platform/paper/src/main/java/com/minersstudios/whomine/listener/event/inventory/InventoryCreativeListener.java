package com.minersstudios.whomine.listener.event.inventory;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlockData;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
