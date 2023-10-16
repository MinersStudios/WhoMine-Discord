package com.minersstudios.msitem.listeners.mechanic;

import com.minersstudios.mscore.listener.event.AbstractMSListener;
import com.minersstudios.mscore.listener.event.MSListener;
import com.minersstudios.msitem.item.CustomItemType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@MSListener
public class BanSwordMechanic extends AbstractMSListener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (
                !(event.getDamager() instanceof final Player damager)
                || CustomItemType.fromItemStack(damager.getInventory().getItemInMainHand()) != CustomItemType.BAN_SWORD
        ) return;

        final Entity damagedEntity = event.getEntity();
        event.setCancelled(!damager.isOp() || damagedEntity instanceof Player);

        if (damager.isOp() && damagedEntity instanceof final Player damaged) {
            damager.performCommand("ban " + damaged.getName() + " 1000y Вы были поражены великим Бан-Мечём");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final @NotNull InventoryClickEvent event) {
        final ItemStack currentItem = event.getCurrentItem();

        if (
                currentItem == null
                || CustomItemType.fromItemStack(currentItem) != CustomItemType.BAN_SWORD
        ) return;

        currentItem.setAmount(0);
        event.setCancelled(true);
    }
}
