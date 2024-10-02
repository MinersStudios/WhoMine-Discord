package com.minersstudios.whomine.listener.impl.event.mechanic;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.custom.item.CustomItem;
import com.minersstudios.whomine.custom.item.registry.cards.CardsBicycle;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.ItemUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CardBoxMechanic {
    private static final List<ItemStack> CARDS = new ObjectArrayList<>();

    @Contract(" -> fail")
    private CardBoxMechanic() throws AssertionError {
        throw new AssertionError("Parent class");
    }

    @ListenFor(eventClass = InventoryMoveItemEvent.class)
    public static final class InventoryMoveItem extends PaperEventListener {

        @EventHandler(priority = EventOrder.CUSTOM)
        public void onInventoryMoveItem(final @NotNull PaperEventContainer<InventoryMoveItemEvent> container) {
            final InventoryMoveItemEvent event = container.getEvent();

            if (event.getDestination().getType() != InventoryType.SHULKER_BOX) {
                return;
            }

            CustomItem.fromItemStack(event.getItem())
            .filter(customItem -> customItem instanceof CardsBicycle)
            .ifPresent(
                    c -> event.setCancelled(true)
            );
        }
    }

    @ListenFor(eventClass = InventoryDragEvent.class)
    public static final class InventoryDrag extends PaperEventListener {

        @EventHandler(priority = EventOrder.CUSTOM)
        public void onInventoryDrag(final @NotNull PaperEventContainer<InventoryDragEvent> container) {
            final InventoryDragEvent event = container.getEvent();

            if (event.getInventory().getType() != InventoryType.SHULKER_BOX) {
                return;
            }

            CustomItem.fromItemStack(event.getOldCursor())
            .filter(customItem -> customItem instanceof CardsBicycle)
            .ifPresent(c -> event.setCancelled(true));
        }
    }

    @ListenFor(eventClass = InventoryClickEvent.class)
    public static final class InventoryClick extends PaperEventListener {

        @EventHandler(priority = EventOrder.CUSTOM)
        public void onInventoryClick(final @NotNull PaperEventContainer<InventoryClickEvent> container) {
            final InventoryClickEvent event = container.getEvent();
            final ItemStack cursorItem = event.getCursor();
            final ItemStack currentItem = event.getCurrentItem();
            final Inventory clickedInventory = event.getClickedInventory();

            if (
                    (
                            clickedInventory != null
                            && clickedInventory.getType() == InventoryType.SHULKER_BOX
                            && CustomItem.fromItemStack(cursorItem).orElse(null) instanceof CardsBicycle
                    )
                    || (
                            event.isShiftClick()
                            && event.getWhoClicked().getOpenInventory().getType() == InventoryType.SHULKER_BOX
                            && CustomItem.fromItemStack(currentItem).orElse(null) instanceof CardsBicycle
                    )
            ) {
                event.setCancelled(true);
            }

            if (
                    currentItem == null
                    || !event.isRightClick()
            ) {
                return;
            }

            if (
                    !cursorItem.getType().isAir()
                    && CustomItem.fromItemStack(currentItem).orElse(null) instanceof CardsBicycle
            ) {
                addCardToCardBox(event, currentItem, cursorItem);
            } else if (
                    !currentItem.getType().isAir()
                    && CustomItem.fromItemStack(cursorItem).orElse(null) instanceof CardsBicycle
            ) {
                addCardToCardBox(event, cursorItem, currentItem);
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void addCardToCardBox(
            final @NotNull InventoryClickEvent event,
            final @NotNull ItemStack cardBoxItem,
            final @NotNull ItemStack cardItem
    ) {
        if (CARDS.isEmpty()) {
            CARDS.addAll(CardsBicycle.Blue.cardItems());
            CARDS.addAll(CardsBicycle.Red.cardItems());
        }

        if (ItemUtils.isContainsItem(CARDS, cardItem)) {
            final BundleMeta bundleMeta = (BundleMeta) cardBoxItem.getItemMeta();
            final var itemStacks = new ObjectArrayList<ItemStack>();

            itemStacks.add(cardItem);
            itemStacks.addAll(bundleMeta.getItems());

            if (itemStacks.size() <= 54) {
                bundleMeta.setItems(itemStacks);
                cardBoxItem.setItemMeta(bundleMeta);
                cardItem.setAmount(0);
            }
        }

        event.setCancelled(true);
    }
}
