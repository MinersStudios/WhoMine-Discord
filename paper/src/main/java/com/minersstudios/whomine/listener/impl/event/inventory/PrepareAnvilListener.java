package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.decor.CustomDecorData;
import com.minersstudios.whomine.custom.item.CustomItem;
import com.minersstudios.whomine.custom.item.renameable.RenameableItem;
import com.minersstudios.whomine.custom.item.renameable.RenameableItemRegistry;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.MSCustomUtils;
import org.bukkit.OfflinePlayer;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

@ListenFor(PrepareAnvilEvent.class)
public final class PrepareAnvilListener extends PaperEventListener {

    @CancellableHandler
    public void onPrepareAnvil(final @NotNull PaperEventContainer<PrepareAnvilEvent> container) {
        final PrepareAnvilEvent event = container.getEvent();
        final ItemStack resultItem = event.getResult();
        final ItemStack firstItem = event.getInventory().getFirstItem();
        @SuppressWarnings("UnstableApiUsage")
        final String renameText = event.getView().getRenameText();

        if (
                resultItem == null
                || firstItem == null
        ) {
            return;
        }

        final RenameableItem renameableItem = RenameableItemRegistry.fromRename(renameText, resultItem).orElse(null);

        if (
                renameableItem != null
                && renameableItem.isWhiteListed((OfflinePlayer) event.getViewers().getFirst())
        ) {
            final ItemStack renamedItem = renameableItem.craftRenamed(resultItem, renameText);

            if (renamedItem != null) {
                event.setResult(renamedItem);
            }
        } else {
            final ItemMeta meta = resultItem.getItemMeta();
            final var custom = MSCustomUtils.getCustom(firstItem).orElse(null);
            ItemStack customStack = null;

            switch (custom) {
                case null -> {
                    meta.setCustomModelData(null);
                    resultItem.setItemMeta(meta);
                    event.setResult(resultItem);
                    return;
                }
                case final CustomBlockData data -> customStack = data.craftItemStack();
                case final CustomItem item -> customStack = item.getItem().clone();
                case final CustomDecorData<?> data -> customStack = data.getItem().clone();
                default -> {
                }
            }

            assert customStack != null;

            final ItemMeta customMeta = customStack.getItemMeta();
            final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            final PersistentDataContainer customDataContainer = customMeta.getPersistentDataContainer();

            meta.setCustomModelData(customMeta.getCustomModelData());
            meta.lore(customMeta.lore());
            dataContainer.getKeys().forEach(dataContainer::remove);

            for (final var key : customDataContainer.getKeys()) {
                final String keyStr = customDataContainer.get(key, PersistentDataType.STRING);

                if (keyStr != null) {
                    dataContainer.set(key, PersistentDataType.STRING, keyStr);
                }
            }

            resultItem.setItemMeta(meta);
            event.setResult(resultItem);
        }
    }
}
