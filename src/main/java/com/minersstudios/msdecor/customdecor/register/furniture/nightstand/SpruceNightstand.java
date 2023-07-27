package com.minersstudios.msdecor.customdecor.register.furniture.nightstand;

import com.google.common.collect.Lists;
import com.minersstudios.mscore.util.Badges;
import com.minersstudios.msdecor.MSDecor;
import com.minersstudios.msdecor.customdecor.CustomDecorData;
import com.minersstudios.msdecor.customdecor.SoundGroup;
import com.minersstudios.msdecor.customdecor.Typed;
import com.minersstudios.msdecor.customdecor.Wrenchable;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class SpruceNightstand implements Wrenchable {
    private @NotNull NamespacedKey namespacedKey;
    private @NotNull ItemStack itemStack;
    private @NotNull SoundGroup soundGroup;
    private @NotNull HitBox hitBox;
    private @Nullable Facing facing;
    private @Nullable List<Recipe> recipes;

    public SpruceNightstand() {
        this.namespacedKey = new NamespacedKey(MSDecor.getInstance(), "spruce_nightstand");
        this.itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        this.itemStack.setItemMeta(this.createItemStack(Type.DEFAULT).getItemMeta());
        this.soundGroup = new SoundGroup(
                "custom.block.wood.place", 0.5f, 1.0f,
                "custom.block.wood.break", 0.5f, 1.0f
        );
        this.hitBox = HitBox.SOLID_FRAME;
    }

    @Override
    public @Nullable List<Recipe> initRecipes() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(this.namespacedKey, this.createItemStack(Type.DEFAULT))
                .shape(
                        "PPP",
                        "PPP",
                        "P P"
                ).setIngredient('P', Material.SPRUCE_PLANKS);
        shapedRecipe.setGroup(this.namespacedKey.getNamespace() + "nightstand");
        this.recipes = Lists.newArrayList(shapedRecipe);
        return this.recipes;
    }

    @Override
    public @NotNull NamespacedKey getNamespacedKey() {
        return this.namespacedKey;
    }

    @Override
    public void setNamespacedKey(@NotNull NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public @NotNull SoundGroup getSoundGroup() {
        return this.soundGroup;
    }

    @Override
    public void setSoundGroup(@NotNull SoundGroup soundGroup) {
        this.soundGroup = soundGroup;
    }

    @Override
    public @NotNull HitBox getHitBox() {
        return this.hitBox;
    }

    @Override
    public void setHitBox(@NotNull HitBox hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public @Nullable Facing getFacing() {
        return this.facing;
    }

    @Override
    public void setFacing(@Nullable Facing facing) {
        this.facing = facing;
    }

    @Override
    public @Nullable List<Recipe> getRecipes() {
        return this.recipes;
    }

    @Override
    public void setRecipes(@Nullable List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public @NotNull CustomDecorData clone() {
        try {
            return (CustomDecorData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type @NotNull [] getTypes() {
        return Type.types;
    }

    public enum Type implements Typed.Type {
        //<editor-fold desc="Types">
        DEFAULT(1128),
        SECOND_LEFT(1129),
        SECOND_MIDDLE(1130),
        SECOND_RIGHT(1131),
        THIRD_LEFT(1132),
        THIRD_MIDDLE(1133),
        THIRD_RIGHT(1134);
        //</editor-fold>

        private final @NotNull NamespacedKey namespacedKey;
        private final @NotNull String itemName;
        private final int customModelData;
        private final @Nullable List<Component> lore;
        private final @NotNull HitBox hitBox;
        private final @Nullable Facing facing;

        private static final Type @NotNull [] types = values();

        Type(int customModelData) {
            this.namespacedKey = new NamespacedKey(MSDecor.getInstance(), this.name().toLowerCase(Locale.ROOT) + "_spruce_nightstand");
            this.itemName = "Еловая тумбочка";
            this.customModelData = customModelData;
            this.lore = Badges.WRENCHABLE_LORE_LIST;
            this.hitBox = HitBox.SOLID_FRAME;
            this.facing = Facing.FLOOR;
        }

        @Override
        public @NotNull NamespacedKey getNamespacedKey() {
            return this.namespacedKey;
        }

        @Override
        public @NotNull String getItemName() {
            return this.itemName;
        }

        @Override
        public int getCustomModelData() {
            return this.customModelData;
        }

        @Override
        public @Nullable List<Component> getLore() {
            return this.lore;
        }

        @Override
        public @NotNull HitBox getHitBox() {
            return this.hitBox;
        }

        @Override
        public @Nullable Facing getFacing() {
            return this.facing;
        }
    }
}
