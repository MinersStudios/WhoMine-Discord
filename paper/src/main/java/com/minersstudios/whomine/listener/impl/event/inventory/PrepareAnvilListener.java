package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.decor.CustomDecorData;
import com.minersstudios.whomine.custom.item.CustomItem;
import com.minersstudios.whomine.custom.item.renameable.RenameableItem;
import com.minersstudios.whomine.custom.item.renameable.RenameableItemRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.MSCustomUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class PrepareAnvilListener extends EventListener {

    public PrepareAnvilListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPrepareAnvil(final @NotNull PrepareAnvilEvent event) {
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
                && renameableItem.isWhiteListed((OfflinePlayer) event.getViewers().get(0))
        ) {
            final ItemStack renamedItem = renameableItem.craftRenamed(resultItem, renameText);

            if (renamedItem != null) {
                event.setResult(renamedItem);
            }
        } else {
            final ItemMeta meta = resultItem.getItemMeta();
            final var custom = MSCustomUtils.getCustom(firstItem).orElse(null);
            ItemStack customStack = null;

            if (custom == null) {
                meta.setCustomModelData(null);
                resultItem.setItemMeta(meta);
                event.setResult(resultItem);
                return;
            } else if (custom instanceof final CustomBlockData data) {
                customStack = data.craftItemStack();
            } else if (custom instanceof final CustomItem item) {
                customStack = item.getItem().clone();
            } else if (custom instanceof final CustomDecorData<?> data) {
                customStack = data.getItem().clone();
            }

            assert customStack != null;

            final ItemMeta customMeta = customStack.getItemMeta();
            final PersistentDataContainer container = meta.getPersistentDataContainer();
            final PersistentDataContainer dataContainer = customMeta.getPersistentDataContainer();

            meta.setCustomModelData(customMeta.getCustomModelData());
            meta.lore(customMeta.lore());
            container.getKeys().forEach(container::remove);

            for (final var key : dataContainer.getKeys()) {
                final String keyStr = dataContainer.get(key, PersistentDataType.STRING);

                if (keyStr != null) {
                    container.set(key, PersistentDataType.STRING, keyStr);
                }
            }

            resultItem.setItemMeta(meta);
            event.setResult(resultItem);
        }
    }
}
