package com.minersstudios.whomine.listener.event.inventory;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.custom.item.CustomItem;
import com.minersstudios.wholib.paper.custom.item.Wearable;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.inventory.CustomInventory;
import com.minersstudios.wholib.paper.inventory.InventoryButton;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.utility.MSLogger;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

import static net.kyori.adventure.text.Component.text;

@ListenFor(InventoryClickEvent.class)
public final class InventoryClickListener extends PaperEventListener {
    private static final int HELMET_SLOT = 39;
    private static final Set<InventoryType> IGNORABLE_INVENTORY_TYPES = EnumSet.of(
            //<editor-fold desc="Ignorable inventory types" defaultstate="collapsed">
            InventoryType.CARTOGRAPHY,
            InventoryType.BREWING,
            InventoryType.BEACON,
            InventoryType.BLAST_FURNACE,
            InventoryType.FURNACE,
            InventoryType.SMOKER,
            InventoryType.GRINDSTONE,
            InventoryType.STONECUTTER,
            InventoryType.SMITHING,
            InventoryType.LOOM,
            InventoryType.MERCHANT,
            InventoryType.ENCHANTING
            //</editor-fold>
    );

    @CancellableHandler(order = EventOrder.CUSTOM)
    public void onInventoryClick(final @NotNull PaperEventContainer<InventoryClickEvent> container) {
        final InventoryClickEvent event = container.getEvent();
        final WhoMine module = container.getModule();

        final Inventory clickedInventory = event.getClickedInventory();
        final ClickType clickType = event.getClick();
        final ItemStack currentItem = event.getCurrentItem();
        final boolean isShiftClick = event.isShiftClick();

        if (clickedInventory != null) {
            if (event.getView().getTopInventory() instanceof final CustomInventory customInventory) {
                if (
                        clickedInventory.getType() == InventoryType.PLAYER
                        && (clickType.isShiftClick() || clickType == ClickType.DOUBLE_CLICK)
                ) {
                    event.setCancelled(true);
                }

                if (clickedInventory instanceof CustomInventory) {
                    final InventoryButton inventoryButton = customInventory.buttonAt(event.getSlot());

                    if (inventoryButton != null) {
                        inventoryButton.doClickAction(event, customInventory);
                    }

                    customInventory.doClickAction(event);

                    if (
                            customInventory.clickAction() == null
                            && !clickType.isCreativeAction()
                    ) {
                        event.setCancelled(true);
                    }
                } else if (clickedInventory instanceof PlayerInventory) {
                    customInventory.doBottomClickAction(event);
                }
            }

            final Player player = (Player) event.getWhoClicked();

            if (module.getCache().getWorldDark().isInWorldDark(player)) {
                event.setCancelled(true);
            }

            if (currentItem != null) {
                final int slot = event.getSlot();
                final ItemStack cursorItem = event.getCursor();

                if (
                        slot == HELMET_SLOT
                        && event.getSlotType() == InventoryType.SlotType.ARMOR
                        && currentItem.isEmpty()
                        && !cursorItem.isEmpty()
                ) {
                    player.setItemOnCursor(null);
                    module.runTask(
                            () -> player.getInventory().setHelmet(cursorItem)
                    );
                }

                if (!currentItem.isEmpty()) {
                    boolean remove = currentItem.getType() == Material.BEDROCK;

                    if (!remove) {
                        for (final var enchantment : currentItem.getEnchantments().keySet()) {
                            remove = currentItem.getEnchantmentLevel(enchantment) > enchantment.getMaxLevel();
                        }
                    }

                    if (remove) {
                        clickedInventory.setItem(slot, new ItemStack(Material.AIR));
                        MSLogger.warning(
                                Translations.INFO_PLAYER_ITEM_REMOVED.asTranslatable()
                                .arguments(
                                        player.name(),
                                        text(currentItem.toString())
                                )
                        );
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (
                IGNORABLE_INVENTORY_TYPES.contains(event.getInventory().getType())
                && isShiftClick
                && CustomBlockRegistry.isCustomBlock(currentItem)
        ) {
            event.setCancelled(true);
        }

        final Player player = (Player) event.getWhoClicked();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack cursorItem = event.getCursor();

        if (
                event.getSlot() == HELMET_SLOT
                && event.getSlotType() == InventoryType.SlotType.ARMOR
                && !cursorItem.getType().isAir()
        ) {
            CustomItem.fromItemStack(currentItem, Wearable.class)
            .ifPresent(w -> {
                assert currentItem != null;

                if (currentItem.getEnchantments().containsKey(Enchantment.BINDING_CURSE)) {
                    return;
                }

                module.runTask(() -> {
                    inventory.setHelmet(cursorItem);
                    player.setItemOnCursor(currentItem);
                });
            });
        }

        if (
                clickedInventory != null
                && currentItem != null
                && isShiftClick
                && clickedInventory.getType() == InventoryType.PLAYER
                && player.getOpenInventory().getType() == InventoryType.CRAFTING
                && inventory.getHelmet() == null
        ) {
            CustomItem.fromItemStack(currentItem, Wearable.class)
            .ifPresent(w -> {
                event.setCancelled(true);
                module.runTask(() -> {
                    inventory.setHelmet(currentItem);
                    currentItem.setAmount(0);
                });
            });
        }
    }
}
