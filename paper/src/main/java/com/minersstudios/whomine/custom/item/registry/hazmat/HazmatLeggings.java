package com.minersstudios.whomine.custom.item.registry.hazmat;

import com.minersstudios.whomine.api.annotation.Key;
import com.minersstudios.whomine.inventory.recipe.builder.RecipeBuilder;
import com.minersstudios.whomine.inventory.recipe.choice.RecipeChoiceEntry;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.utility.ChatUtils;
import com.minersstudios.whomine.custom.item.CustomItemImpl;
import com.minersstudios.whomine.custom.item.CustomItemType;
import com.minersstudios.whomine.custom.item.damageable.Damageable;
import com.minersstudios.whomine.custom.item.damageable.DamageableItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class HazmatLeggings extends CustomItemImpl implements Damageable {
    private static final @Key String KEY;
    private static final ItemStack ITEM_STACK;

    /** Max durability of this item */
    public static final int MAX_DURABILITY = 150;

    static {
        KEY = "hazmat_leggings";
        ITEM_STACK = new ItemStack(Material.LEATHER_LEGGINGS);
        final LeatherArmorMeta meta = (LeatherArmorMeta) ITEM_STACK.getItemMeta();

        meta.displayName(ChatUtils.createDefaultStyledText("Антирадиационные штаны"));
        meta.setCustomModelData(1);
        meta.setColor(Color.fromRGB(15712578));
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addAttributeModifier(
                Attribute.GENERIC_ARMOR,
                new AttributeModifier(UUID.randomUUID(), "armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS)
        );
        meta.addAttributeModifier(
                Attribute.GENERIC_ARMOR_TOUGHNESS,
                new AttributeModifier(UUID.randomUUID(), "armor_toughness", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS)
        );
        ITEM_STACK.setItemMeta(meta);
    }

    public HazmatLeggings() {
        super(KEY, ITEM_STACK);
    }

    @Contract(" -> new")
    @Override
    public @NotNull @Unmodifiable List<RecipeEntry> initRecipes() {
        return Collections.singletonList(
                RecipeEntry.fromBuilder(
                        RecipeBuilder.shaped()
                        .namespacedKey(this.namespacedKey)
                        .result(this.itemStack)
                        .shape(
                                "TTT",
                                "T T",
                                "T T"
                        )
                        .ingredients(
                                RecipeChoiceEntry.itemStack('T', CustomItemType.ANTI_RADIATION_TEXTILE.getCustomItem().getItem())
                        ),
                        true
                )
        );
    }

    @Override
    public @NotNull DamageableItem buildDamageable() {
        return new DamageableItem(
                Material.LEATHER_LEGGINGS.getMaxDurability(),
                MAX_DURABILITY
        );
    }
}
