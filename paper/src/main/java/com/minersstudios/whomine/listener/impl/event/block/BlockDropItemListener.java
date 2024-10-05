package com.minersstudios.whomine.listener.impl.event.block;

import com.minersstudios.whomine.api.event.EventHandler;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.custom.item.renameable.RenameableItemRegistry;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.utility.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Tag;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@ListenFor(BlockDropItemEvent.class)
public final class BlockDropItemListener extends PaperEventListener {

    @EventHandler
    public void onBlockDropItem(final @NotNull PaperEventContainer<BlockDropItemEvent> container) {
        final BlockDropItemEvent event = container.getEvent();
        final var items = event.getItems();

        if (items.size() != 1) {
            return;
        }

        final Item entity = items.getFirst();
        final ItemStack item = entity.getItemStack();

        if (!Tag.SHULKER_BOXES.isTagged(item.getType())) {
            return;
        }

        final ItemMeta meta = item.getItemMeta();
        final Component displayName = meta.displayName();

        if (displayName == null) {
            return;
        }

        final String serialized = ChatUtils.serializePlainComponent(displayName);

        RenameableItemRegistry.fromRename(serialized, item)
        .ifPresent(renameableItem -> {
            final ItemStack renameableItemStack = renameableItem.craftRenamed(item, serialized);

            if (renameableItemStack != null) {
                entity.setItemStack(renameableItemStack);
            }
        });
    }
}
