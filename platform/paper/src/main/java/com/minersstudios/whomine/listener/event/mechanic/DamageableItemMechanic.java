package com.minersstudios.whomine.listener.event.mechanic;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.item.damageable.DamageableItem;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.paper.utility.ItemUtils;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class DamageableItemMechanic {

    @Contract(" -> fail")
    private DamageableItemMechanic() throws AssertionError {
        throw new AssertionError("Parent class");
    }

    @ListenFor(PlayerItemDamageEvent.class)
    public static final class PlayerItemDamage extends PaperEventListener {

        @CancellableHandler
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
