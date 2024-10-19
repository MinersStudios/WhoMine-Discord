package com.minersstudios.whomine.listener.packet.player;

import com.minersstudios.wholib.event.handle.CancellableHandler;
import com.minersstudios.wholib.paper.inventory.SignMenu;
import com.minersstudios.wholib.packet.registry.PlayPackets;
import com.minersstudios.wholib.paper.packet.PaperPacketContainer;
import com.minersstudios.wholib.paper.packet.PaperPacketListener;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerUpdateSignListener extends PaperPacketListener {

    public PlayerUpdateSignListener() {
        super(PlayPackets.SERVER_UPDATE_SIGN);
    }

    @CancellableHandler
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
