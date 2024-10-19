package com.minersstudios.wholib.paper.inventory;

import com.minersstudios.wholib.paper.inventory.action.InventoryAction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.minecraft.world.Container;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * CustomInventoryImpl is an abstract class that provides a base implementation
 * for custom inventories. It extends CraftInventoryCustom and implements the
 * CustomInventory interface.
 *
 * @param <S> Self-type, the specific implementation of the custom inventory.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue", "unchecked" })
abstract class CustomInventoryImpl<S extends CustomInventory> extends CraftInventoryCustom implements CustomInventory {
    protected @NotNull Int2ObjectMap<InventoryButton> buttons;
    protected @Nullable InventoryAction<InventoryOpenEvent> openAction;
    protected @Nullable InventoryAction<InventoryCloseEvent> closeAction;
    protected @Nullable InventoryAction<InventoryClickEvent> clickAction;
    protected @Nullable InventoryAction<InventoryClickEvent> bottomClickAction;
    protected @NotNull List<Object> args;
    protected final int size;

    protected static final int LAST_SLOT = 53;

    protected CustomInventoryImpl(
            final @NotNull Component title,
            final @Range(from = 1, to = 6) int verticalSize
    ) {
        super(null, verticalSize * 9, title);

        this.size = verticalSize * 9;
        this.buttons = new Int2ObjectOpenHashMap<>(this.size);
        this.args = new ObjectArrayList<>();
    }

    @Override
    public @NotNull String getTitle() {
        return super.getTitle();
    }

    @Override
    public @NotNull Component title() {
        return super.title();
    }

    @Override
    public @NotNull Map<Integer, InventoryButton> buttons() {
        return this.buttons;
    }

    @Override
    public @NotNull S buttons(final @NotNull Map<Integer, InventoryButton> buttons) throws IllegalArgumentException {
        buttons.forEach(this::buttonAt);
        return (S) this;
    }

    @Override
    public boolean hasButtons() {
        return !this.buttons.isEmpty();
    }

    @Override
    public @Nullable InventoryButton buttonAt(final @Range(from = 0, to = LAST_SLOT) int slot) {
        return this.buttons.getOrDefault(slot, null);
    }

    @Override
    public @NotNull S buttonAt(
            final @Range(from = 0, to = LAST_SLOT) int slot,
            final @Nullable InventoryButton button
    ) throws IllegalArgumentException {
        this.validateSlot(slot);
        this.buttons.put(slot, button);
        this.setItem(
                slot,
                button == null
                ? ItemStack.empty()
                : button.item()
        );

        return (S) this;
    }

    @Override
    public @NotNull List<Object> args() {
        return this.args;
    }

    @Override
    public @NotNull S args(final @NotNull List<Object> args) {
        this.args = args;
        return (S) this;
    }

    @Override
    public @Nullable InventoryAction<InventoryOpenEvent> openAction() {
        return this.openAction;
    }

    @Override
    public @NotNull S openAction(final @Nullable InventoryAction<InventoryOpenEvent> openAction) {
        this.openAction = openAction;
        return (S) this;
    }

    @Override
    public @Nullable InventoryAction<InventoryCloseEvent> closeAction() {
        return this.closeAction;
    }

    @Override
    public @NotNull S closeAction(final @Nullable InventoryAction<InventoryCloseEvent> closeAction) {
        this.closeAction = closeAction;
        return (S) this;
    }

    @Override
    public @Nullable InventoryAction<InventoryClickEvent> clickAction() {
        return this.clickAction;
    }

    @Override
    public @NotNull S clickAction(final @Nullable InventoryAction<InventoryClickEvent> clickAction) {
        this.clickAction = clickAction;
        return (S) this;
    }

    @Override
    public @Nullable InventoryAction<InventoryClickEvent> bottomClickAction() {
        return this.bottomClickAction;
    }

    @Override
    public @NotNull S bottomClickAction(final @Nullable InventoryAction<InventoryClickEvent> bottomClickAction) {
        this.bottomClickAction = bottomClickAction;
        return (S) this;
    }

    @Override
    public void doOpenAction(final @NotNull InventoryOpenEvent event) {
        if (this.openAction != null) {
            this.openAction.doAction(event, this.clone());
        }
    }

    @Override
    public void doCloseAction(final @NotNull InventoryCloseEvent event) {
        if (this.closeAction != null) {
            this.closeAction.doAction(event, this.clone());
        }
    }

    @Override
    public void doClickAction(final @NotNull InventoryClickEvent event) {
        if (this.clickAction != null) {
            this.clickAction.doAction(event, this.clone());
        }
    }

    @Override
    public void doBottomClickAction(final @NotNull InventoryClickEvent event) {
        if (this.bottomClickAction != null) {
            this.bottomClickAction.doAction(event, this.clone());
        }
    }

    @Override
    public void open(final @NotNull Player player) {
        player.openInventory(this);
    }

    @Override
    public void validateSlot(final int slot) throws IllegalArgumentException {
        if (slot < 0 || slot >= this.getSize()) {
            throw new IllegalArgumentException("Slot must be between 0 and " + (this.getSize() - 1));
        }
    }

    @Override
    public @NotNull S clone() {
        try {
            final var clone = (CustomInventoryImpl<S>) super.clone();
            final Container newContainer = new CraftInventoryCustom(null, this.getSize(), this.title()).getInventory();
            final Field inventoryField = CraftInventory.class.getDeclaredField("inventory");

            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);

            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            unsafe.putObject(clone, unsafe.objectFieldOffset(inventoryField), newContainer);

            clone.buttons = new Int2ObjectOpenHashMap<>(this.buttons);
            clone.setContents(this.getContents());

            return (S) clone;
        } catch (final Exception e) {
            throw new AssertionError("An error occurred while cloning '" + this + "'", e);
        }
    }
}
