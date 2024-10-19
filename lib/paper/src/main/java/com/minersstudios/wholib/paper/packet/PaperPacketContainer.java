package com.minersstudios.wholib.paper.packet;

import com.minersstudios.wholib.packet.PacketContainer;
import com.minersstudios.wholib.paper.WhoMine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a paper packet container.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The main module that registers and handles the packet event</li>
 *     <li>The packet event itself</li>
 * </ul>
 *
 * @see PaperPacketEvent
 * @see PaperPacketListener
 */
@SuppressWarnings("unused")
public final class PaperPacketContainer
        extends PacketContainer<WhoMine, PaperPacketEvent> {

    private PaperPacketContainer(
            final @NotNull WhoMine module,
            final @NotNull PaperPacketEvent packet
    ) {
        super(module, packet);
    }

    @Override
    public boolean isCancelled() {
        return this.getEvent().isCancelled();
    }

    /**
     * Creates a new paper packet container with the given module and event
     *
     * @param module The main module that registers and handles the event
     * @param packet The packet event associated with this container
     * @return A new container instance
     */
    @Contract("_, _ -> new")
    public static @NotNull PaperPacketContainer of(
            final @NotNull WhoMine module,
            final @NotNull PaperPacketEvent packet
    ) {
        return new PaperPacketContainer(module, packet);
    }
}
