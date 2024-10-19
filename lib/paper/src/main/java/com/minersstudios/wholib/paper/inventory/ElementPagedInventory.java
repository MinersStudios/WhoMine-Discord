package com.minersstudios.wholib.paper.inventory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;

/**
 * Builder for paged inventory with elements. Element slots are slots where
 * elements are located. Elements are buttons that change when the page index
 * changes, all elements are located in the element slots.
 *
 * @see CustomInventory
 * @see PagedCustomInventory
 */
public class ElementPagedInventory extends PagedCustomInventoryImpl<ElementPagedInventory> implements PagedCustomInventory {
    protected final @NotNull ArrayListMultimap<Integer, InventoryButton> elements;
    protected final int[] elementSlots;

    /**
     * Inventory with elements and pages
     *
     * @param title        Title of the inventory
     * @param verticalSize Vertical size of the inventory
     * @param elementSlots Slots of the elements in the inventory
     */
    protected ElementPagedInventory(
            final @NotNull Component title,
            final @Range(from = 1, to = 6) int verticalSize,
            final int @Range(from = 0, to = Integer.MAX_VALUE) ... elementSlots
    ) {
        super(title, verticalSize);

        this.elementSlots = elementSlots;
        this.elements = ArrayListMultimap.create();
    }

    /**
     * Used to update the pages of the inventory
     *
     * @return Element paged inventory
     */
    @Override
    public @NotNull ElementPagedInventory build() {
        this.updatePages();
        return this;
    }

    /**
     * @return Elements of the inventory
     */
    @Contract(" -> new")
    public @NotNull Multimap<Integer, InventoryButton> elements() {
        return ArrayListMultimap.create(this.elements);
    }

    /**
     * Set the elements of the inventory
     * <br>
     * <b>NOTE:</b> This will also update the pages and buttons
     *
     * @param elements New elements of the inventory
     * @return This inventory
     */
    public @NotNull ElementPagedInventory elements(final @NotNull List<InventoryButton> elements) {
        final int size = elements.size();

        this.elements.clear();
        this.setPagesCount((int) Math.ceil((double) size / this.elementSlots.length));

        for (int page = 0; page < this.pagesCount; ++page) {
            for (int element = 0; element < this.elementSlots.length; ++element) {
                final int index = element + (page * this.elementSlots.length);

                if (index >= size) break;

                this.elements.put(page, elements.get(index));
            }
        }

        this.updatePages();
        this.buttons(this.getPageContents(this.page));

        return this;
    }

    /**
     * Gets copy of the element slots
     *
     * @return element slot array
     */
    @Contract(" -> new")
    public int[] getElementSlots() {
        return this.elementSlots.clone();
    }

    /**
     * @param page Page index
     * @return Elements of the page
     */
    public @NotNull Int2ObjectMap<InventoryButton> getPageContents(final int page) {
        final var buttons = new Int2ObjectOpenHashMap<InventoryButton>(this.elementSlots.length);
        final var buttonList = this.elements.get(page);

        for (int i = 0; i < buttonList.size(); ++i) {
            buttons.put(this.elementSlots[i], buttonList.get(i));
        }

        for (final int slot : this.elementSlots) {
            buttons.putIfAbsent(slot, null);
        }

        return buttons;
    }

    /**
     * Creates an inventory page with the specified index and content
     *
     * @param page Page index
     * @return Page of the inventory
     */
    public @Nullable ElementPagedInventory createPage(final @Range(from = 0, to = Integer.MAX_VALUE) int page) {
        if (page >= this.pagesCount) {
            return null;
        }

        final ElementPagedInventory pagedInventory = this.clone();

        pagedInventory.setPageIndex(page);
        pagedInventory.buttons(this.getPageContents(page));

        return pagedInventory;
    }

    /**
     * Updates the pages of the inventory
     * <br>
     * <b>Warning:</b> This method is expensive and should only be called when
     *                 necessary
     */
    public void updatePages() {
        this.pages.clear();

        for (int page = 0; page < this.pagesCount; ++page) {
            this.pages.put(page, this.createPage(page));
        }

        this.updateStaticButtons();
    }

    /**
     * Sets the page count of the inventory
     *
     * @param pagesCount New page count
     */
    @Override
    protected void setPagesCount(final @Range(from = 0, to = Integer.MAX_VALUE) int pagesCount) {
        this.pagesCount = pagesCount;
    }
}
