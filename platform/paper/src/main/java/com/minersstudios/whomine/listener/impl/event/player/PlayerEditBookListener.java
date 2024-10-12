package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

@ListenFor(PlayerEditBookEvent.class)
public final class PlayerEditBookListener extends PaperEventListener {

    @CancellableHandler
    public void onPlayerEditBook(final @NotNull PaperEventContainer<PlayerEditBookEvent> container) {
        final PlayerEditBookEvent event = container.getEvent();

        if (!event.isSigning()) {
            return;
        }

        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(container.getModule(), event.getPlayer());
        final BookMeta bookMeta = event.getNewBookMeta();
        final String title = bookMeta.getTitle();
        final boolean isAnon =
                title != null
                && title.startsWith("*");

        event.setNewBookMeta(bookMeta
                .author(
                        isAnon
                        ? Translations.BOOK_ANONYMOUS.asTranslatable()
                        : playerInfo.getDefaultName()
                ).title(
                        isAnon
                        ? text(title.substring(1))
                        : bookMeta.title()
                )
        );
    }
}
