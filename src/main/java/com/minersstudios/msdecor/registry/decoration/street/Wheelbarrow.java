package com.minersstudios.msdecor.registry.decoration.street;

import com.minersstudios.mscore.inventory.recipe.RecipeBuilder;
import com.minersstudios.mscore.inventory.recipe.ShapedRecipeBuilder;
import com.minersstudios.mscore.utility.ChatUtils;
import com.minersstudios.mscore.sound.SoundGroup;
import com.minersstudios.msdecor.api.CustomDecorDataImpl;
import com.minersstudios.msdecor.api.DecorHitBox;
import com.minersstudios.msdecor.api.Facing;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class Wheelbarrow extends CustomDecorDataImpl<Wheelbarrow> {

    @Override
    protected @NotNull Builder builder() {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1142);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Тачка"));
        itemStack.setItemMeta(itemMeta);

        return new Builder()
                .key("wheelbarrow")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.SOLID)
                        .size(1.0d, 1.0d, 1.0d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.ANVIL)
                .itemStack(itemStack)
                .recipes(
                        Map.entry(
                                RecipeBuilder.shapedBuilder()
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "S S",
                                        " C ",
                                        " I "
                                )
                                .ingredients(
                                        ShapedRecipeBuilder.material('S', Material.STICK),
                                        ShapedRecipeBuilder.material('C', Material.CAULDRON),
                                        ShapedRecipeBuilder.material('I', Material.IRON_INGOT)
                                ),
                                true
                        )
                );
    }
}
