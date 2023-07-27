package com.minersstudios.msdecor.customdecor.register.halloween;

import com.minersstudios.mscore.util.ChatUtils;
import com.minersstudios.mscore.util.MSDecorUtils;
import com.minersstudios.msdecor.MSDecor;
import com.minersstudios.msdecor.customdecor.CustomDecorData;
import com.minersstudios.msdecor.customdecor.Sittable;
import com.minersstudios.msdecor.customdecor.SoundGroup;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkeletonHand implements Sittable {
    private @NotNull NamespacedKey namespacedKey;
    private @NotNull ItemStack itemStack;
    private @NotNull SoundGroup soundGroup;
    private @NotNull HitBox hitBox;
    private @Nullable Facing facing;
    private @Nullable List<Recipe> recipes;
    private double height;

    public SkeletonHand() {
        this.namespacedKey = new NamespacedKey(MSDecor.getInstance(), "skeleton_hand");
        this.itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setCustomModelData(1189);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Рука скелета"));
        itemMeta.getPersistentDataContainer().set(
                MSDecorUtils.CUSTOM_DECOR_TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING,
                this.getNamespacedKey().getKey()
        );
        this.itemStack.setItemMeta(itemMeta);
        this.soundGroup = new SoundGroup(
                "block.bone.place", 0.75f, 1.0f,
                "block.bone.break", 0.75f, 1.0f
        );
        this.hitBox = HitBox.FRAME;
        this.facing = Facing.FLOOR;
        this.height = 0.75d;
    }

    @Override
    public @Nullable List<Recipe> initRecipes() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(this.namespacedKey, this.itemStack)
                .shape("BBB", "BBB", " B ")
                .setIngredient('B', Material.BONE);
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
    public double getHeight() {
        return this.height;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }
}
