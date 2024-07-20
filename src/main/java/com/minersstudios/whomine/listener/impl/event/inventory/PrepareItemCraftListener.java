package com.minersstudios.whomine.listener.impl.event.inventory;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.CustomBlockRegistry;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public final class PrepareItemCraftListener extends EventListener {

    public PrepareItemCraftListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent event) {
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
