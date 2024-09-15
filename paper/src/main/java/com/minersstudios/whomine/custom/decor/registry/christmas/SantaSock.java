package com.minersstudios.whomine.custom.decor.registry.christmas;

import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.WhoMine;
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

public final class SantaSock extends CustomDecorDataImpl<SantaSock> {

    SantaSock(final @NotNull WhoMine plugin) throws IllegalStateException {
        super(plugin);
    }

    @Override
    protected @NotNull Builder builder(final @NotNull WhoMine plugin) {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1186);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Носок санты"));
        itemStack.setItemMeta(itemMeta);

        final Builder builder = new Builder()
                .key("santa_sock")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.NONE)
                        .facings(Facing.WALL)
                        .size(0.7875d, 0.7875d, 0.7875d)
                        .build()
                )
                .facings(Facing.WALL)
                .soundGroup(SoundGroup.WOOL)
                .itemStack(itemStack)
                .parameters(DecorParameter.PAINTABLE);

        return plugin.getConfiguration().isChristmas()
                ? builder.recipes(
                        unused -> RecipeEntry.fromBuilder(
                                RecipeBuilder.shaped()
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "LA",
                                        "LA",
                                        "LL"
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('L', Material.LEATHER),
                                        RecipeChoiceEntry.material('A', Material.AIR)
                                ),
                                true
                        )
                )
                : builder;
    }
}
