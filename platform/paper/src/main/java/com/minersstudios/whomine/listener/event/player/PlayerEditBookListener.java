package com.minersstudios.whomine.listener.event.player;

import com.minersstudios.wholib.listener.ListenFor;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import com.minersstudios.wholib.event.handle.CancellableHandler;
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
