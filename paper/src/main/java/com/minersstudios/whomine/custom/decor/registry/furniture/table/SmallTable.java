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

public abstract class SmallTable<C extends CustomDecorData<C>> extends CustomDecorDataImpl<C> {

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
                                .group(SharedConstants.MSDECOR_NAMESPACE + ":small_table")
                                .category(CraftingBookCategory.BUILDING)
                                .shape(
                                        "PPP",
                                        "PAP"
                                )
                                .ingredients(
                                        RecipeChoiceEntry.material('P', planksMaterial),
                                        RecipeChoiceEntry.material('A', Material.AIR)
                                ),
                                true
                        )
                );
    }

    public static final class Acacia extends SmallTable<Acacia> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "acacia_small_table",
                    1070,
                    "Маленький акациевый стол",
                    Material.ACACIA_PLANKS
            );
        }
    }

    public static final class Birch extends SmallTable<Birch> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "birch_small_table",
                    1072,
                    "Маленький берёзовый стол",
                    Material.BIRCH_PLANKS
            );
        }
    }

    public static final class Cherry extends SmallTable<Cherry> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "cherry_small_table",
                    1384,
                    "Маленький вишнёвый стол",
                    Material.CHERRY_PLANKS
            );
        }
    }

    public static final class Crimson extends SmallTable<Crimson> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "crimson_small_table",
                    1074,
                    "Маленький багровый стол",
                    Material.CRIMSON_PLANKS
            );
        }
    }

    public static final class DarkOak extends SmallTable<DarkOak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "dark_oak_small_table",
                    1076,
                    "Маленький стол из тёмного дуба",
                    Material.DARK_OAK_PLANKS
            );
        }
    }

    public static final class Jungle extends SmallTable<Jungle> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "jungle_small_table",
                    1078,
                    "Маленький тропический стол",
                    Material.JUNGLE_PLANKS
            );
        }
    }

    public static final class Mangrove extends SmallTable<Mangrove> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "mangrove_small_table",
                    1201,
                    "Маленький мангровый стол",
                    Material.MANGROVE_PLANKS
            );
        }
    }

    public static final class Oak extends SmallTable<Oak> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "oak_small_table",
                    1080,
                    "Маленький дубовый стол",
                    Material.OAK_PLANKS
            );
        }
    }

    public static final class Spruce extends SmallTable<Spruce> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "spruce_small_table",
                    1082,
                    "Маленький еловый стол",
                    Material.SPRUCE_PLANKS
            );
        }
    }

    public static final class Warped extends SmallTable<Warped> {

        @Override
        protected @NotNull Builder builder() {
            return this.createBuilder(
                    "warped_small_table",
                    1084,
                    "Маленький искажённый стол",
                    Material.WARPED_PLANKS
            );
        }
    }
}
