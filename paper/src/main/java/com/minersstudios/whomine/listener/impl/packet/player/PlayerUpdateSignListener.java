package com.minersstudios.whomine.listener.impl.packet.player;

import com.minersstudios.whomine.api.event.EventHandler;
import com.minersstudios.whomine.inventory.SignMenu;
import com.minersstudios.whomine.api.packet.registry.PlayPackets;
import com.minersstudios.whomine.packet.PaperPacketContainer;
import com.minersstudios.whomine.packet.PaperPacketListener;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerUpdateSignListener extends PaperPacketListener {

    public PlayerUpdateSignListener() {
        super(PlayPackets.SERVER_UPDATE_SIGN);
    }

    @EventHandler
    public void onEvent(final @NotNull PaperPacketContainer container) {
        final var event = container.getEvent();
        final Player player = event.getConnection().getPlayer().getBukkitEntity();
        final SignMenu menu = SignMenu.getSignMenu(player);

        if (
                menu != null
                && event.getPacket() instanceof final ServerboundSignUpdatePacket packet
        ) {
            if (!menu.getResponse().test(player, packet.getLines())) {
                container.getModule().runTaskLater(() -> menu.open(player), 2L);
            } else {
                menu.close(player);
            }
        }
    }
}
