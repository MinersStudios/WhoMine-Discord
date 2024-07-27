package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.decor.CustomDecor;
import com.minersstudios.whomine.custom.decor.event.CustomDecorClickEvent;
import com.minersstudios.whomine.listener.api.EventListener;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerInteractAtEntityListener extends EventListener {

    public PlayerInteractAtEntityListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(final @NotNull PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof final Interaction interaction)) {
            return;
        }

        CustomDecor.fromInteraction(interaction)
        .ifPresent(
                customDecor -> {
                    final CustomDecorClickEvent clickEvent =
                            new CustomDecorClickEvent(
                                    customDecor,
                                    event.getPlayer(),
                                    event.getHand(),
                                    event.getClickedPosition(),
                                    interaction,
                                    CustomDecorClickEvent.ClickType.RIGHT_CLICK
                            );

                    interaction.getServer().getPluginManager().callEvent(clickEvent);

                    if (!clickEvent.isCancelled()) {
                        customDecor.getData().doClickAction(this.getPlugin(), clickEvent);
                    }
                }
        );
    }
}
