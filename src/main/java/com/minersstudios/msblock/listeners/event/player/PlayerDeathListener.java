package com.minersstudios.msblock.listeners.event.player;

import com.minersstudios.msblock.Cache;
import com.minersstudios.msblock.MSBlock;
import com.minersstudios.mscore.listener.event.AbstractMSListener;
import com.minersstudios.mscore.listener.event.MSListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public final class PlayerDeathListener extends AbstractMSListener<MSBlock> {

    @EventHandler
    public void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Cache cache = MSBlock.cache();

        cache.getDiggingMap().removeAll(player);
        cache.getStepMap().put(player, 0.0d);
    }
}
