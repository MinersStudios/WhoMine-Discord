package com.minersstudios.whomine.custom.decor.registry.christmas;

import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.WhoMine;
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

public final class Snowman extends CustomDecorDataImpl<Snowman> {

    Snowman(final @NotNull WhoMine plugin) throws IllegalStateException {
        super(plugin);
    }

    @Override
    protected @NotNull Builder builder(final @NotNull WhoMine plugin) {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1187);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Снеговик"));
        itemStack.setItemMeta(itemMeta);

        final ItemStack broken = itemStack.clone();
        final ItemMeta brokenMeta = broken.getItemMeta();

        brokenMeta.setCustomModelData(1188);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Сломанный снеговик"));
        broken.setItemMeta(brokenMeta);

        final Builder builder0 = new Builder()
                .key("snowman")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.NONE)
                        .size(0.925d, 2.203125d, 0.925d)
                        .modelOffsetY(0.075d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.SNOW)
                .itemStack(itemStack)
                .parameters(
                        DecorParameter.WRENCHABLE,
                        DecorParameter.PAINTABLE
                )
                .types(
                        builder -> new Type(builder, "broken", broken)
                )
                .dropsType(true);

        return plugin.getConfiguration().isChristmas()
                ? builder0.recipes(
                        unused -> RecipeEntry.fromBuilder(
                                RecipeBuilder.shaped()
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        " L ",
                                        "SBS",
                                        " B "
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('L', Material.LEATHER),
                                        RecipeChoiceEntry.material('S', Material.STICK),
                                        RecipeChoiceEntry.material('B', Material.SNOW_BLOCK)
                                ),
                                true
                        )
                )
                : builder0;
    }
}
