package com.minersstudios.whomine.listener.impl.event.chat;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.chat.ChatType;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.utility.MessageUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.handle.CancellableHandler;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

import static com.minersstudios.whomine.api.locale.Translations.COMMAND_MUTE_ALREADY_RECEIVER;
import static com.minersstudios.whomine.api.locale.Translations.WARNING_YOU_CANT_DO_THIS_NOW;
import static net.kyori.adventure.text.Component.text;

@ListenFor(AsyncChatEvent.class)
public final class AsyncChatListener extends PaperEventListener {

    @CancellableHandler(order = EventOrder.LOW, ignoreCancelled = true)
    public void onAsyncChat(final @NotNull PaperEventContainer<AsyncChatEvent> container) {
        final AsyncChatEvent event = container.getEvent();
        final WhoMine module = container.getModule();

        event.setCancelled(true);

        final Player player = event.getPlayer();
        final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(module, player);

        if (
                playerInfo.isInWorldDark()
                || !playerInfo.isAuthenticated()
        ) {
            MSLogger.warning(
                    player,
                    WARNING_YOU_CANT_DO_THIS_NOW.asTranslatable()
            );

            return;
        }

        if (
                playerInfo.isMuted()
                && playerInfo.getMutedTo().isBefore(Instant.now())
        ) {
            playerInfo.unmute(player.getServer().getConsoleSender());
        }

        if (playerInfo.isMuted()) {
            MSLogger.warning(
                    player,
                    COMMAND_MUTE_ALREADY_RECEIVER.asTranslatable()
            );

            return;
        }

        String message = ChatUtils.serializeLegacyComponent(event.originalMessage());

        if (message.startsWith("!")) {
            message = message.substring(1).trim();

            if (!message.isEmpty()) {
                MessageUtils.sendMessageToChat(playerInfo, null, ChatType.GLOBAL, text(message));
            }
        } else if (message.startsWith("*")) {
            message = message.substring(1).trim();

            if (message.startsWith("*")) {
                message = message.substring(1).trim();

                if (message.startsWith("*")) {
                    message = message.substring(1).trim();

                    if (!message.isEmpty()) {
                        MessageUtils.sendRPEventMessage(player, text(message), MessageUtils.RolePlayActionType.IT);
                    }
                } else if (!message.isEmpty()) {
                    MessageUtils.sendRPEventMessage(player, text(message), MessageUtils.RolePlayActionType.DO);
                }
            } else if (message.contains("*")) {
                final String action = message.substring(message.indexOf('*') + 1).trim();
                final String speech = message.substring(0, message.indexOf('*')).trim();

                if (action.isEmpty() || speech.isEmpty()) {
                    MSLogger.severe(player, "Используй: * [речь] * [действие]");
                } else {
                    MessageUtils.sendRPEventMessage(player, text(speech), text(action), MessageUtils.RolePlayActionType.TODO);
                }
            } else if (!message.isEmpty()) {
                MessageUtils.sendRPEventMessage(player, text(message), MessageUtils.RolePlayActionType.ME);
            }
        } else {
            MessageUtils.sendMessageToChat(playerInfo, player.getLocation(), ChatType.LOCAL, text(message));
            module.getCache().getChatBuffer().receiveMessage(player, message + " ");
        }
    }
}
