package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import com.minersstudios.wholib.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.wholib.locale.Translations.*;

@ListenFor(PlayerKickEvent.class)
public class PlayerKickListener extends PaperEventListener {
    private static final TranslatableComponent SERVER_RESTARTING =
            FORMAT_LEAVE_MESSAGE.asTranslatable()
            .arguments(
                    ON_DISABLE_MESSAGE_TITLE.asTranslatable().color(NamedTextColor.RED).decorate(TextDecoration.BOLD),
                    ON_DISABLE_MESSAGE_SUBTITLE.asTranslatable().color(NamedTextColor.GRAY)
            )
            .color(NamedTextColor.DARK_GRAY);

    @CancellableHandler
    public void onPlayerKick(final @NotNull PaperEventContainer<PlayerKickEvent> container) {
        final PlayerKickEvent event = container.getEvent();

        if (event.getCause() == PlayerKickEvent.Cause.RESTART_COMMAND) {
            event.reason(SERVER_RESTARTING);
        }
    }
}
