package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.locale.Translations.*;

public class PlayerKickListener extends EventListener {
    private static final TranslatableComponent SERVER_RESTARTING =
            FORMAT_LEAVE_MESSAGE.asTranslatable()
            .arguments(
                    ON_DISABLE_MESSAGE_TITLE.asTranslatable().color(NamedTextColor.RED).decorate(TextDecoration.BOLD),
                    ON_DISABLE_MESSAGE_SUBTITLE.asTranslatable().color(NamedTextColor.GRAY)
            )
            .color(NamedTextColor.DARK_GRAY);

    public PlayerKickListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerKick(final @NotNull PlayerKickEvent event) {
        if (event.getCause() == PlayerKickEvent.Cause.RESTART_COMMAND) {
            event.reason(SERVER_RESTARTING);
        }
    }
}
