package com.minersstudios.whomine.custom.decor.registry.furniture.lamp;

import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.api.utility.ChatUtils;
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

public final class SmallLamp extends CustomDecorDataImpl<SmallLamp> {

    @Override
    protected @NotNull Builder builder() {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1144);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Маленькая лампа"));
        itemStack.setItemMeta(itemMeta);

        return new Builder()
                .key("small_lamp")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.LIGHT)
                        .size(0.625d, 1.0625d, 0.625d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.WOOD)
                .itemStack(itemStack)
                .recipes(
                        unused -> RecipeEntry.fromBuilder(
                                RecipeBuilder.shaped()
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "L",
                                        "S"
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('S', Material.STICK),
                                        RecipeChoiceEntry.material('L', Material.LEATHER)
                                ),
                                true
                        )
                )
                .parameters(
                        DecorParameter.LIGHTABLE,
                        DecorParameter.PAINTABLE
                )
                .lightLevels(0, 15)
                .clickAction((plugin, event) -> BigLamp.playClick(event));
    }
}
