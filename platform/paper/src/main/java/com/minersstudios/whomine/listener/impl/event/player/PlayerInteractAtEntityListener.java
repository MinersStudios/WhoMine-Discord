package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.custom.decor.CustomDecor;
import com.minersstudios.whomine.custom.decor.event.CustomDecorClickEvent;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import org.bukkit.entity.Interaction;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jetbrains.annotations.NotNull;

@ListenFor(PlayerInteractAtEntityEvent.class)
public final class PlayerInteractAtEntityListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerInteractAtEntity(final @NotNull PaperEventContainer<PlayerInteractAtEntityEvent> container) {
        final PlayerInteractAtEntityEvent event = container.getEvent();

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
                        customDecor.getData().doClickAction(container.getModule(), clickEvent);
                    }
                }
        );
    }
}
