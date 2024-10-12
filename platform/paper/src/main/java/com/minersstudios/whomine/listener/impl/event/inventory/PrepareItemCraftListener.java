package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.ItemUtils;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
@ListenFor(PrepareItemCraftEvent.class)
public final class PrepareItemCraftListener extends PaperEventListener {

    @CancellableHandler
    public void onPrepareItemCraft(final @NotNull PaperEventContainer<PrepareItemCraftEvent> container) {
        final PrepareItemCraftEvent event = container.getEvent();
        final Recipe recipe = event.getRecipe();

        if (recipe == null) {
            return;
        }

        final ItemStack result = recipe.getResult();

        for (final var itemStack : event.getInventory().getMatrix()) {
            if (
                    CustomBlockRegistry.isCustomBlock(itemStack)
                    && (
                            (
                                    recipe instanceof final ShapedRecipe shapedRecipe
                                    && shapedRecipe.getIngredientMap().values().stream()
                                            .noneMatch(item -> ItemUtils.isSimilarItemStacks(item, itemStack))
                            )
                            || (
                                    !result.hasItemMeta()
                                    || !result.getItemMeta().hasCustomModelData()
                            )
                    )
            ) {
                event.getInventory().setResult(ItemStack.empty());
            }
        }
    }
}
