package com.minersstudios.whomine.listener.impl.event.mechanic;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.item.CustomItemType;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class BanSwordMechanic {

    @Contract(" -> fail")
    private BanSwordMechanic() throws AssertionError {
        throw new AssertionError("Parent class");
    }

    @ListenFor(EntityDamageByEntityEvent.class)
    public static final class EntityDamageByEntity extends PaperEventListener {

        @CancellableHandler(order = EventOrder.CUSTOM)
        public void onEntityDamageByEntity(final @NotNull PaperEventContainer<EntityDamageByEntityEvent> container) {
            final EntityDamageByEntityEvent event = container.getEvent();

            if (
                    !(event.getDamager() instanceof final Player damager)
                    || CustomItemType.fromItemStack(damager.getInventory().getItemInMainHand()) != CustomItemType.BAN_SWORD
            ) {
                return;
            }

            final Entity damagedEntity = event.getEntity();
            event.setCancelled(!damager.isOp() || damagedEntity instanceof Player);

            if (damager.isOp() && damagedEntity instanceof final Player damaged) {
                damager.performCommand("ban " + damaged.getName() + " 1000y Вы были поражены великим Бан-Мечём");
            }
        }
    }

    @ListenFor(InventoryClickEvent.class)
    public static final class InventoryClick extends PaperEventListener {

        @CancellableHandler(order = EventOrder.CUSTOM)
        public void onInventoryClick(final @NotNull PaperEventContainer<InventoryClickEvent> container) {
            final InventoryClickEvent event = container.getEvent();
            final ItemStack currentItem = event.getCurrentItem();

            if (
                    currentItem == null
                            || CustomItemType.fromItemStack(currentItem) != CustomItemType.BAN_SWORD
            ) {
                return;
            }

            currentItem.setAmount(0);
            event.setCancelled(true);
        }
    }
}
