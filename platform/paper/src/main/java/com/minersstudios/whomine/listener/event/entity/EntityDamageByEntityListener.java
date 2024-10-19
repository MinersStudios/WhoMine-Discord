package com.minersstudios.whomine.listener.event.entity;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.custom.decor.CustomDecor;
import com.minersstudios.wholib.paper.custom.decor.event.CustomDecorClickEvent;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.utility.SharedConstants;
import org.bukkit.GameMode;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
