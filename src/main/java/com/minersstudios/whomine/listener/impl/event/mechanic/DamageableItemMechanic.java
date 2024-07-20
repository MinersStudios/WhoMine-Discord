package com.minersstudios.whomine.listener.impl.event.mechanic;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.item.damageable.DamageableItem;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class DamageableItemMechanic extends EventListener {

    public DamageableItemMechanic(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerItemDamage(final @NotNull PlayerItemDamageEvent event) {
        final ItemStack itemStack = event.getItem();

        if (DamageableItem.fromItemStack(itemStack) != null) {
            event.setCancelled(true);
            ItemUtils.damageItem(event.getPlayer(), event.getItem(), event.getDamage());
        }
    }
}
