package com.minersstudios.whomine.listener.event.inventory;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.ItemUtils;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
