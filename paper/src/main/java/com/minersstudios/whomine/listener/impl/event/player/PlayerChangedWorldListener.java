package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.utility.MSPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerChangedWorldListener extends EventListener {

    public PlayerChangedWorldListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChangedWorld(final @NotNull PlayerChangedWorldEvent event) {
        MSPlayerUtils.hideNameTag(
                this.getPlugin(),
                event.getPlayer()
        );
    }
}
