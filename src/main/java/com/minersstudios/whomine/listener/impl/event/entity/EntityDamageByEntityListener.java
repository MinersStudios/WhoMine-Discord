package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.decor.CustomDecor;
import com.minersstudios.whomine.custom.decor.event.CustomDecorClickEvent;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.SharedConstants;
import org.bukkit.GameMode;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public final class EntityDamageByEntityListener extends EventListener {

    public EntityDamageByEntityListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (
                event.getEntity() instanceof final ItemFrame itemFrame
                && itemFrame.getScoreboardTags().contains(SharedConstants.INVISIBLE_ITEM_FRAME_TAG)
                && !itemFrame.isVisible()
        ) {
            itemFrame.setVisible(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityMonitor(final @NotNull EntityDamageByEntityEvent event) {
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

        if (
                (
                        player.isSneaking()
                        && gameMode == GameMode.SURVIVAL
                )
                || gameMode == GameMode.CREATIVE
        ) {
            CustomDecor.destroy(this.getPlugin(), player, interaction);
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
                            customDecor.getData().doClickAction(this.getPlugin(), clickEvent);
                        }
                    }
            );
        }
    }
}
