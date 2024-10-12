package com.minersstudios.whomine.api.event;

import com.minersstudios.whomine.api.event.handle.HandlerExecutor;
import com.minersstudios.whomine.api.module.Module;
import com.minersstudios.whomine.api.packet.PacketContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a cancellable event container that can be executed by
 * {@link HandlerExecutor handler executors}
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The module that registers and handles the event</li>
 *     <li>The event itself</li>
 * </ul>
 *
 * @param <M> The module type, that registers and handles the event
 * @param <E> The event type
 *
 * @see EventListener
 * @see PacketContainer
 */
public abstract class CancellableEventContainer<M extends Module, E>
        extends EventContainer<M, E> {

    /**
     * Cancellable event container constructor
     *
     * @param module The module that registers and handles the event
     * @param event  The event associated with this event container
     */
    protected CancellableEventContainer(
            final @NotNull M module,
            final @NotNull E event
    ) {
        super(module, event);
    }

    /**
     * Returns whether the event has been cancelled
     *
     * @return Whether the event has been cancelled
     */
    public abstract boolean isCancelled();
}
