package com.minersstudios.whomine.listener.event.mechanic;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.item.CustomItemType;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class CocaineMechanic {

    @Contract(" -> fail")
    private CocaineMechanic() throws AssertionError {
        throw new AssertionError("Parent class");
    }

    @ListenFor(PlayerItemConsumeEvent.class)
    public static final class PlayerItemConsume extends PaperEventListener {

        @CancellableHandler
        public void onInventoryClick(final @NotNull PaperEventContainer<PlayerItemConsumeEvent> container) {
            final PlayerItemConsumeEvent event = container.getEvent();
            final ItemStack itemStack = event.getItem();

            if (
                    !(itemStack.getItemMeta() instanceof PotionMeta)
                    || CustomItemType.fromItemStack(itemStack) != CustomItemType.COCAINE
            ) {
                return;
            }

            container.getModule().runTask(
                    () -> event.getPlayer()
                               .getInventory()
                               .getItem(event.getHand())
                               .setAmount(0)
            );
        }
    }
}
