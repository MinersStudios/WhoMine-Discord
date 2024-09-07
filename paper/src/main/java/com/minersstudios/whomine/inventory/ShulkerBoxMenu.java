package com.minersstudios.whomine.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public final class ShulkerBoxMenu extends AbstractContainerMenu {
    private final Container container;
    private InventoryView bukkitEntity;
    private final Inventory playerInventory;

    private static final int CONTAINER_SIZE = 27;

    public ShulkerBoxMenu(
            final int syncId,
            final @NotNull Inventory playerInventory,
            final @NotNull Container inventory
    ) throws IllegalArgumentException {
        super(MenuType.SHULKER_BOX, syncId);

        checkContainerSize(inventory, CONTAINER_SIZE);

        this.container = inventory;
        this.playerInventory = playerInventory;
        int j;
        int k;

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new ShulkerBoxSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public @NotNull InventoryView getBukkitView() {
        return this.bukkitEntity == null
                ? this.bukkitEntity = new CraftInventoryView<>(
                        this.playerInventory.player.getBukkitEntity(),
                        new CraftInventory(this.container),
                        this
                )
                : this.bukkitEntity;
    }

    @Override
    public boolean stillValid(final @NotNull Player player) {
        return !this.checkReachable || this.container.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(
            final @NotNull Player player,
            final int index
    ) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            final ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();

            if (index < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(slotItem, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(final @NotNull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}

