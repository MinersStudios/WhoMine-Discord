package com.github.minersstudios.msessentials.listeners.player;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.mscore.logger.MSLogger;
import com.github.minersstudios.msessentials.Config;
import com.github.minersstudios.msessentials.MSEssentials;
import com.github.minersstudios.msessentials.player.PlayerFile;
import com.github.minersstudios.msessentials.player.PlayerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import com.github.minersstudios.mscore.listener.AbstractMSListener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

@MSListener
public class AsyncPlayerPreLoginListener extends AbstractMSListener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(@NotNull AsyncPlayerPreLoginEvent event) {
        String hostAddress = event.getAddress().getHostAddress();
        String nickname = event.getName();
        Config config = MSEssentials.getConfiguration();
        PlayerInfo playerInfo = PlayerInfo.fromProfile(event.getUniqueId(), nickname);
        PlayerFile playerFile = playerInfo.getPlayerFile();
        OfflinePlayer offlinePlayer = playerInfo.getOfflinePlayer();
        TranslatableComponent leaveMessageFormat = Component.translatable("ms.format.leave.message").color(NamedTextColor.DARK_GRAY);

        if (
                playerFile.exists()
                && !playerFile.getIpList().contains(hostAddress)
        ) {
            playerFile.addIp(hostAddress);
            playerFile.save();

            MSLogger.warning(
                    Component.translatable(
                            "ms.info.player_added_ip",
                            playerInfo.getGrayIDGoldName(),
                            text(playerInfo.getNickname()),
                            text(hostAddress)
                    )
            );
        }

        if (!this.getPlugin().isLoadedCustoms()) {
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    leaveMessageFormat.args(
                            Component.translatable("ms.server_not_fully_loaded.title").style(Style.style(NamedTextColor.RED, TextDecoration.BOLD)),
                            Component.translatable("ms.server_not_fully_loaded.subtitle").color(NamedTextColor.GRAY)
                    )
            );
        }

        if (
                config.developerMode
                && !offlinePlayer.isOp()
        ) {
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    leaveMessageFormat.args(
                            Component.translatable("ms.pre_login.tech_works.title").style(Style.style(NamedTextColor.RED, TextDecoration.BOLD)),
                            Component.translatable("ms.pre_login.tech_works.subtitle").color(NamedTextColor.GRAY)
                    )
            );
        }

        if (!playerInfo.isWhiteListed()) {
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST,
                    leaveMessageFormat.args(
                            Component.translatable("ms.pre_login.whitelisted.title").style(Style.style(NamedTextColor.RED, TextDecoration.BOLD)),
                            Component.translatable("ms.pre_login.whitelisted.subtitle").color(NamedTextColor.GRAY)
                    )
            );
        }

        if (playerInfo.isBanned()) {
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                    leaveMessageFormat.args(
                            Component.translatable("ms.pre_login.banned.title").style(Style.style(NamedTextColor.RED, TextDecoration.BOLD)),
                            Component.translatable(
                                    "ms.pre_login.banned.subtitle",
                                    playerInfo.getBanReason(),
                                    playerInfo.getBannedTo(event.getAddress())
                            ).color(NamedTextColor.GRAY)
                    )
            );
        }
    }
}
