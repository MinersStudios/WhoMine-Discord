package com.minersstudios.whomine.custom.decor.registry.christmas;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.custom.decor.CustomDecorDataImpl;
import com.minersstudios.whomine.custom.decor.DecorHitBox;
import com.minersstudios.whomine.custom.decor.Facing;
import com.minersstudios.whomine.world.sound.SoundGroup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

public final class TreeStar extends CustomDecorDataImpl<TreeStar> {

    TreeStar(final @NotNull WhoMine plugin) throws IllegalStateException {
        super(plugin);
    }

    @Override
    protected @NotNull Builder builder(final @NotNull WhoMine plugin) {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1259);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Новогодняя звезда"));
        itemStack.setItemMeta(itemMeta);

        final Builder builder = new Builder()
                .key("tree_star")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.NONE)
                        .size(1.0d, 2.20625d, 1.0d)
                        .modelOffsetY(0.23125d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.STONE)
                .itemStack(itemStack);

        return plugin.getConfiguration().isChristmas()
                ? builder.recipes(
                        unused -> RecipeEntry.fromBuilder(
                                RecipeBuilder.shaped()
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        " I ",
                                        "III",
                                        " B "
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('I', Material.GOLD_INGOT),
                                        RecipeChoiceEntry.material('B', Material.GOLD_BLOCK)
                                ),
                                true
                        )
                )
                : builder;
    }
}
