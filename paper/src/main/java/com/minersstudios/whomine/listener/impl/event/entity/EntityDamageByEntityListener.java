package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.decor.CustomDecor;
import com.minersstudios.whomine.custom.decor.event.CustomDecorClickEvent;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.utility.SharedConstants;
import org.bukkit.GameMode;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(EntityDamageByEntityEvent.class)
public final class EntityDamageByEntityListener extends PaperEventListener {

    @CancellableHandler
    public void onEntityDamageByEntityNormal(final @NotNull PaperEventContainer<EntityDamageByEntityEvent> container) {
        if (
                container.getEvent().getEntity() instanceof final ItemFrame itemFrame
                && itemFrame.getScoreboardTags().contains(SharedConstants.INVISIBLE_ITEM_FRAME_TAG)
                && !itemFrame.isVisible()
        ) {
            itemFrame.setVisible(true);
        }
    }

    @CancellableHandler(order = EventOrder.CUSTOM)
    public void onEntityDamageByEntityCustom(final @NotNull PaperEventContainer<EntityDamageByEntityEvent> container) {
        final EntityDamageByEntityEvent event = container.getEvent();

        if (
                !(event.getDamager() instanceof final Player player)
                || !(event.getEntity() instanceof final Interaction interaction)
        ) {
            return;
        }

        final GameMode gameMode = player.getGameMode();

        if (
                gameMode == GameMode.ADVENTURE
                || gameMode == GameMode.SPECTATOR
        ) {
            return;
        }

        final WhoMine module = container.getModule();

        if (
                (
                        player.isSneaking()
                        && gameMode == GameMode.SURVIVAL
                )
                || gameMode == GameMode.CREATIVE
        ) {
            CustomDecor.destroy(module, player, interaction);
        } else {
            CustomDecor.fromInteraction(interaction)
            .ifPresent(
                    customDecor -> {
                        final CustomDecorClickEvent clickEvent =
                                new CustomDecorClickEvent(
                                        customDecor,
                                        player,
                                        player.getHandRaised(),
                                        interaction.getLocation().toCenterLocation().toVector(),
                                        interaction,
                                        CustomDecorClickEvent.ClickType.LEFT_CLICK
                                );

                        player.getServer().getPluginManager().callEvent(clickEvent);

                        if (!clickEvent.isCancelled()) {
                            customDecor.getData().doClickAction(module, clickEvent);
                        }
                    }
            );
        }
    }
}
