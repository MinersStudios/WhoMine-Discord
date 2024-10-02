package com.minersstudios.whomine.utility;

import com.minersstudios.whomine.custom.item.damageable.DamageableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static org.bukkit.EntityEffect.*;
import static org.bukkit.enchantments.Enchantment.UNBREAKING;

/**
 * Utility class for {@link ItemStack} and {@link ItemMeta}
 */
@SuppressWarnings("UnusedReturnValue")
public final class ItemUtils {

    @Contract(" -> fail")
    private ItemUtils() throws AssertionError {
        throw new AssertionError("Utility class");
    }

    /**
     * @param first  First {@link ItemStack}
     * @param second Second {@link ItemStack}
     * @return True if the CustomModelData and {@link Material} of the two items
     *         are the same
     */
    @Contract("null, null -> false")
    public static boolean isSimilarItemStacks(
            final @Nullable ItemStack first,
            final @Nullable ItemStack second
    ) {
        if (
                first == null
                || second == null
                || first.getType() != second.getType()
        ) {
            return false;
        }

        final ItemMeta firstMeta = first.getItemMeta();
        final ItemMeta secondMeta = second.getItemMeta();

        return firstMeta.hasCustomModelData()
                && secondMeta.hasCustomModelData()
                && firstMeta.getCustomModelData() == secondMeta.getCustomModelData();
    }

    /**
     * @param items Collection of items that will be checked
     * @param item  Item that will be checked for its presence in the list
     * @return True if the list contains the item
     * @see #isSimilarItemStacks(ItemStack, ItemStack)
     */
    public static boolean isContainsItem(
            final @NotNull Collection<ItemStack> items,
            final @Nullable ItemStack item
    ) {
        if (items.isEmpty()) {
            return false;
        }

        for (final var listItem : items) {
            if (isSimilarItemStacks(listItem, item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Deals one point of damage to the specified item
     *
     * @param holder Player, who is holding the item
     * @param item   The item to damage
     * @return False if the {@link ItemMeta} of the item is not an instance of
     *         {@link Damageable} or if the damage event is cancelled
     * @see #damageItem(Player, ItemStack, int)
     */
    @Contract("_, null -> false")
    public static boolean damageItem(
            final @NotNull Player holder,
            final @Nullable ItemStack item
    ) {
        return damageItem(holder, item, 1);
    }

    /**
     * Damages the specified item with specified damage
     *
     * @param holder         Player, who is holding the item
     * @param item           The item to damage
     * @param originalDamage Damage you want to inflict on the item
     * @return False if the {@link ItemMeta} of the item is not an instance of
     *         {@link Damageable} or if the damage event is cancelled
     * @see #damageItem(Player, EquipmentSlot, ItemStack, int)
     */
    @Contract("_, null, _ -> false")
    public static boolean damageItem(
            final @NotNull Player holder,
            final @Nullable ItemStack item,
            final int originalDamage
    ) {
        return damageItem(holder, null, item, originalDamage);
    }

    /**
     * Damages the specified item with specified damage
     *
     * @param holder         Player, who is holding the item
     * @param slot           Slot where the player is holding the item
     *                       (used for item break effect)
     * @param item           The item to damage
     * @param originalDamage Damage you want to inflict on the item
     * @return False if the {@link ItemMeta} of the item is not an instance of
     *         {@link Damageable} or if the damage event is cancelled
     */
    @Contract("_, _, null, _ -> false")
    public static boolean damageItem(
            final @NotNull Player holder,
            final @Nullable EquipmentSlot slot,
            final @Nullable ItemStack item,
            final int originalDamage
    ) {
        if (
                item == null
                || !(item.getItemMeta() instanceof final Damageable damageable)
        ) {
            return false;
        }

        int damage = 0;
        final DamageableItem damageableItem = DamageableItem.fromItemStack(item);

        if (damageableItem != null) {
            damageableItem.setRealDamage(damageableItem.getRealDamage() + originalDamage);

            if (
                    !damageable.hasEnchant(UNBREAKING)
                    || Math.random() < 1.0d / (damageable.getEnchantLevel(UNBREAKING) + 1.0d)
            ) {
                damage = originalDamage;

                damageableItem.saveForItemStack(item);
            }
        } else if (
                !damageable.hasEnchant(UNBREAKING)
                || Math.random() < 1.0d / (damageable.getEnchantLevel(UNBREAKING) + 1.0d)
        ) {
            damage = originalDamage;

            damageable.setDamage(damageable.getDamage() + originalDamage);
            item.setItemMeta(damageable);
        }

        if (damageableItem == null) {
            final PlayerItemDamageEvent event = new PlayerItemDamageEvent(holder, item, damage, originalDamage);

            holder.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
        }

        if (
                damageableItem != null
                ? damageableItem.getRealDamage() >= damageableItem.getMaxDamage()
                : damageable.getDamage() >= item.getType().getMaxDurability()
        ) {
            item.setAmount(item.getAmount() - 1);

            if (item.getType() == Material.SHIELD) {
                holder.playEffect(SHIELD_BREAK);
                return true;
            }

            switch (slot == null ? EquipmentSlot.HAND : slot) {
                case HEAD ->     holder.playEffect(BREAK_EQUIPMENT_HELMET);
                case CHEST ->    holder.playEffect(BREAK_EQUIPMENT_CHESTPLATE);
                case LEGS ->     holder.playEffect(BREAK_EQUIPMENT_LEGGINGS);
                case FEET ->     holder.playEffect(BREAK_EQUIPMENT_BOOTS);
                case OFF_HAND -> holder.playEffect(BREAK_EQUIPMENT_OFF_HAND);
                default ->       holder.playEffect(BREAK_EQUIPMENT_MAIN_HAND);
            }
        }

        return true;
    }
}
