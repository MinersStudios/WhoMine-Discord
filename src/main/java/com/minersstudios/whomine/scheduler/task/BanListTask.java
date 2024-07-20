package com.minersstudios.whomine.scheduler.task;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.player.collection.PlayerInfoMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Server;
import org.bukkit.ban.ProfileBanList;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public final class BanListTask implements Runnable {
    private final Server server;
    private final PlayerInfoMap playerInfoMap;
    private final Set<BanEntry<PlayerProfile>> ignoreBanSet;

    public BanListTask(final @NotNull WhoMine plugin) {
        this.server = plugin.getServer();
        this.playerInfoMap = plugin.getCache().getPlayerInfoMap();
        this.ignoreBanSet = new ObjectOpenHashSet<>();
    }

    @Override
    public void run() {
        final ProfileBanList banList = this.server.getBanList(BanList.Type.PROFILE);
        final Set<BanEntry<PlayerProfile>> entries = banList.getEntries();
        final Instant currentInstant = Instant.now();

        for (final var banEntry : entries) {
            final Date expiration = banEntry.getExpiration();

            if (
                    expiration != null
                    && !this.ignoreBanSet.contains(banEntry)
                    && expiration.toInstant().isBefore(currentInstant)
            ) {
                final PlayerProfile profile = banEntry.getBanTarget();
                final UUID uuid = profile.getId();
                final String name = profile.getName();

                if (
                        uuid == null
                        || name == null
                ) {
                    this.ignoreBanSet.add(banEntry);
                } else {
                    this.playerInfoMap.get(uuid, name).pardon(null);
                }
            }
        }
    }
}
