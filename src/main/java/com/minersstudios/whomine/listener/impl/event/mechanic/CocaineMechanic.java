package com.minersstudios.whomine.listener.impl.event.mechanic;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.item.CustomItemType;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

public final class CocaineMechanic extends EventListener {

    public CocaineMechanic(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClick(final @NotNull PlayerItemConsumeEvent event) {
        final ItemStack itemStack = event.getItem();

        if (
                !(itemStack.getItemMeta() instanceof PotionMeta)
                || CustomItemType.fromItemStack(itemStack) != CustomItemType.COCAINE
        ) {
            return;
        }

        this.getPlugin().runTask(
                () -> event.getPlayer().getInventory().getItem(event.getHand()).setAmount(0)
        );
    }
}
