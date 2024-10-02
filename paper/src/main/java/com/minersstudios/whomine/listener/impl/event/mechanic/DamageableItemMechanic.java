package com.minersstudios.whomine.listener.impl.event.mechanic;

import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.custom.item.damageable.DamageableItem;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.utility.ItemUtils;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class DamageableItemMechanic {

    @Contract(" -> fail")
    private DamageableItemMechanic() throws AssertionError {
        throw new AssertionError("Parent class");
    }

    @ListenFor(eventClass = PlayerItemDamageEvent.class)
    public static final class PlayerItemDamage extends PaperEventListener {

        @EventHandler
        public void onPlayerItemDamage(final @NotNull PaperEventContainer<PlayerItemDamageEvent> container) {
            final PlayerItemDamageEvent event = container.getEvent();
            final ItemStack itemStack = event.getItem();

            if (DamageableItem.fromItemStack(itemStack) != null) {
                event.setCancelled(true);
                ItemUtils.damageItem(event.getPlayer(), event.getItem(), event.getDamage());
            }
        }
    }
}
