package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public final class PlayerEditBookListener extends EventListener {

    public PlayerEditBookListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerEditBook(final @NotNull PlayerEditBookEvent event) {
        if (!event.isSigning()) {
            return;
        }

        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(this.getPlugin(), event.getPlayer());
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
