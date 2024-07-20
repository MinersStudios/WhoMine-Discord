package com.minersstudios.whomine.custom.decor.registry.furniture.chair;

import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.utility.ChatUtils;
import com.minersstudios.whomine.custom.decor.CustomDecorDataImpl;
import com.minersstudios.whomine.custom.decor.DecorHitBox;
import com.minersstudios.whomine.custom.decor.DecorParameter;
import com.minersstudios.whomine.custom.decor.Facing;
import com.minersstudios.whomine.world.sound.SoundGroup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

public final class BarStool extends CustomDecorDataImpl<BarStool> {

    @Override
    protected @NotNull Builder builder() {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1036);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Барный стул"));
        itemStack.setItemMeta(itemMeta);

        return new Builder()
                .key("barstool")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.SOLID)
                        .size(1.0d, 1.0d, 1.0d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.CHAIN)
                .itemStack(itemStack)
                .recipes(
                        unused -> RecipeEntry.fromBuilder(
                                RecipeBuilder.shaped()
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "ILI",
                                        " I ",
                                        "III"
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('I', Material.IRON_INGOT),
                                        RecipeChoiceEntry.material('L', Material.LEATHER)
                                ),
                                true
                        )
                )
                .parameters(
                        DecorParameter.SITTABLE,
                        DecorParameter.PAINTABLE
                )
                .sitHeight(1.0d);
    }
}
