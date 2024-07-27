package com.minersstudios.whomine.custom.decor.registry.decoration.home;

import com.minersstudios.whomine.api.annotation.Key;
import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.utility.ChatUtils;
import com.minersstudios.whomine.utility.SharedConstants;
import com.minersstudios.whomine.custom.decor.CustomDecorData;
import com.minersstudios.whomine.custom.decor.CustomDecorDataImpl;
import com.minersstudios.whomine.custom.decor.DecorHitBox;
import com.minersstudios.whomine.custom.decor.Facing;
import com.minersstudios.whomine.world.sound.SoundGroup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

public abstract class Piggybank<C extends CustomDecorData<C>> extends CustomDecorDataImpl<C> {

    protected final @NotNull Builder createBuilder(
            final @Key @NotNull String key,
            final int customModelData,
            final @NotNull String displayName,
            final @NotNull Material material
    ) {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(customModelData);
        itemMeta.displayName(ChatUtils.createDefaultStyledText(displayName));
        itemStack.setItemMeta(itemMeta);

        return new Builder()
                .key(key)
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.NONE)
                        .size(1.0d, 0.675d, 1.0d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.GLASS)
                .itemStack(itemStack)
                .recipes(
                        unused -> RecipeEntry.fromBuilder(
                                RecipeBuilder.shaped()
                                .group(SharedConstants.MSDECOR_NAMESPACE + ":piggybank")
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "  P",
                                        "PPP",
                                        "P P"
                                )
                                .ingredients(RecipeChoiceEntry.material('P', material)),
                                true
                        )
                );
    }

    public static final class Clay extends Piggybank<Clay> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "clay_piggybank",
                    1155,
                    "Керамическая копилка",
                    Material.CLAY
            );
        }
    }

    public static final class Diamond extends Piggybank<Diamond> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "diamond_piggybank",
                    1156,
                    "Алмазная копилка",
                    Material.DIAMOND_BLOCK
            );
        }
    }

    public static final class Emerald extends Piggybank<Emerald> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "emerald_piggybank",
                    1157,
                    "Изумрудная копилка",
                    Material.EMERALD_BLOCK
            );
        }
    }

    public static final class Gold extends Piggybank<Gold> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "gold_piggybank",
                    1158,
                    "Золотая копилка",
                    Material.GOLD_BLOCK
            );
        }
    }

    public static final class Iron extends Piggybank<Iron> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "iron_piggybank",
                    1159,
                    "Железная копилка",
                    Material.IRON_BLOCK
            );
        }
    }

    public static final class Netherite extends Piggybank<Netherite> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "netherite_piggybank",
                    1160,
                    "Незеритовая копилка",
                    Material.NETHERITE_BLOCK
            );
        }
    }
}
