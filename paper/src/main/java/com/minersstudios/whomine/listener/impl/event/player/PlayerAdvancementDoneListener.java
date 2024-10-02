package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static net.kyori.adventure.text.Component.*;

@ListenFor(eventClass = PlayerAdvancementDoneEvent.class)
public final class PlayerAdvancementDoneListener extends PaperEventListener {

    @EventHandler
    public void onPlayerAdvancementDone(final @NotNull PaperEventContainer<PlayerAdvancementDoneEvent> container) {
        final PlayerAdvancementDoneEvent event = container.getEvent();
        final AdvancementDisplay advancementDisplay = event.getAdvancement().getDisplay();

        if (
                advancementDisplay == null
                || event.message() == null
        ) {
            return;
        }

        final AdvancementDisplay.Frame frame = advancementDisplay.frame();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(container.getModule(), event.getPlayer());
        final Component title = advancementDisplay.title();
        final Component description = advancementDisplay.description();

        // BOO
        event.message(
                space()
                .append(translatable(
                "chat.type.advancement." + frame.name().toLowerCase(Locale.ROOT),
                playerInfo.getDefaultName(),
                text("[").append(title).append(text("]")).color(frame.color())
                .hoverEvent(HoverEvent.showText(title.append(newline().append(description)).color(frame.color()))))
                .color(NamedTextColor.GRAY))
        );
    }
}
