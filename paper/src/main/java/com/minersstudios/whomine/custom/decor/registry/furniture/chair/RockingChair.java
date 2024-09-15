package com.minersstudios.whomine.custom.decor.registry.furniture.chair;

import com.minersstudios.whomine.api.annotation.Path;
import com.minersstudios.whomine.custom.decor.*;
import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.api.utility.SharedConstants;
import com.minersstudios.whomine.world.sound.SoundGroup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;

public abstract class RockingChair<C extends CustomDecorData<C>> extends CustomDecorDataImpl<C> {

    protected final @NotNull Builder createBuilder(
            final @Path @NotNull String key,
            final int customModelData,
            final @NotNull String displayName,
            final @NotNull Material planksMaterial
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
                                   .type(DecorHitBox.Type.SOLID)
                                   .size(1.0d, 1.0d, 1.0d)
                                   .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.WOOD)
                .itemStack(itemStack)
                .recipes(
                        unused -> RecipeEntry.fromBuilder(
                                RecipeBuilder.shaped()
                                .group(SharedConstants.MSDECOR_NAMESPACE + ":rocking_chair")
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "P  ",
                                        "PPP",
                                        "PPP"
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('P', planksMaterial)
                                ),
                                true
                        )
                )
                .parameters(DecorParameter.SITTABLE)
                .sitHeight(0.5d);
    }

    public static final class Acacia extends RockingChair<Acacia> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "acacia_rocking_chair",
                    1038,
                    "Акациевое кресло-качалка",
                    Material.ACACIA_PLANKS
            );
        }
    }

    public static final class Birch extends RockingChair<Birch> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "birch_rocking_chair",
                    1040,
                    "Берёзовое кресло-качалка",
                    Material.BIRCH_PLANKS
            );
        }
    }

    public static final class Cherry extends RockingChair<Cherry> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "cherry_rocking_chair",
                    1380,
                    "Вишнёвое кресло-качалка",
                    Material.CHERRY_PLANKS
            );
        }
    }

    public static final class Crimson extends RockingChair<Crimson> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "crimson_rocking_chair",
                    1042,
                    "Багровое кресло-качалка",
                    Material.CRIMSON_PLANKS
            );
        }
    }

    public static final class DarkOak extends RockingChair<DarkOak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "dark_oak_rocking_chair",
                    1044,
                    "Кресло-качалка из тёмного дуба",
                    Material.DARK_OAK_PLANKS
            );
        }
    }

    public static final class Jungle extends RockingChair<Jungle> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "jungle_rocking_chair",
                    1046,
                    "Тропическое кресло-качалка",
                    Material.JUNGLE_PLANKS
            );
        }
    }

    public static final class Mangrove extends RockingChair<Mangrove> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "mangrove_rocking_chair",
                    1197,
                    "Мангровое кресло-качалка",
                    Material.MANGROVE_PLANKS
            );
        }
    }

    public static final class Oak extends RockingChair<Oak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "oak_rocking_chair",
                    1048,
                    "Дубовое кресло-качалка",
                    Material.OAK_PLANKS
            );
        }
    }

    public static final class Spruce extends RockingChair<Spruce> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "spruce_rocking_chair",
                    1050,
                    "Еловое кресло-качалка",
                    Material.SPRUCE_PLANKS
            );
        }
    }

    public static final class Warped extends RockingChair<Warped> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "warped_rocking_chair",
                    1052,
                    "Искажённое кресло-качалка",
                    Material.WARPED_PLANKS
            );
        }
    }
}
