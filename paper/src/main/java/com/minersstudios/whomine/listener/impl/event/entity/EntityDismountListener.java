package com.minersstudios.whomine.listener.impl.event.entity;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.api.EventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDismountEvent;
import org.jetbrains.annotations.NotNull;

public final class EntityDismountListener extends EventListener {

    public EntityDismountListener(final @NotNull WhoMine plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDismount(final @NotNull EntityDismountEvent event) {
        if (event.getEntity() instanceof final Player player) {
            PlayerInfo
            .fromOnlinePlayer(this.getPlugin(), player)
            .unsetSitting();
        }
    }
}
