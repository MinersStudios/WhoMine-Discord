package com.minersstudios.whomine.custom.decor.registry.furniture.table;

import com.minersstudios.whomine.api.annotation.Path;
import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.api.utility.SharedConstants;
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

public abstract class BigTable<C extends CustomDecorData<C>> extends CustomDecorDataImpl<C> {

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
                                .group(SharedConstants.MSDECOR_NAMESPACE + ":big_table")
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "PPP",
                                        "P P",
                                        "P P"
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('P', planksMaterial)
                                ),
                                true
                        )
                );
    }

    public static final class Acacia extends BigTable<Acacia> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "acacia_big_table",
                    1054,
                    "Акациевый стол",
                    Material.ACACIA_PLANKS
            );
        }
    }

    public static final class Birch extends BigTable<Birch> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "birch_big_table",
                    1056,
                    "Берёзовый стол",
                    Material.BIRCH_PLANKS
            );
        }
    }

    public static final class Cherry extends BigTable<Cherry> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "cherry_big_table",
                    1382,
                    "Вишнёвый стол",
                    Material.CHERRY_PLANKS
            );
        }
    }

    public static final class Crimson extends BigTable<Crimson> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "crimson_big_table",
                    1058,
                    "Багровый стол",
                    Material.CRIMSON_PLANKS
            );
        }
    }

    public static final class DarkOak extends BigTable<DarkOak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "dark_oak_big_table",
                    1060,
                    "Стол из тёмного дуба",
                    Material.DARK_OAK_PLANKS
            );
        }
    }

    public static final class Jungle extends BigTable<Jungle> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "jungle_big_table",
                    1062,
                    "Тропический стол",
                    Material.JUNGLE_PLANKS
            );
        }
    }

    public static final class Mangrove extends BigTable<Mangrove> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "mangrove_big_table",
                    1199,
                    "Мангровый стол",
                    Material.MANGROVE_PLANKS
            );
        }
    }

    public static final class Oak extends BigTable<Oak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "oak_big_table",
                    1064,
                    "Дубовый стол",
                    Material.OAK_PLANKS
            );
        }
    }

    public static final class Spruce extends BigTable<Spruce> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "spruce_big_table",
                    1066,
                    "Еловый стол",
                    Material.SPRUCE_PLANKS
            );
        }
    }

    public static final class Warped extends BigTable<Warped> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "warped_big_table",
                    1068,
                    "Искажённый стол",
                    Material.WARPED_PLANKS
            );
        }
    }
}
