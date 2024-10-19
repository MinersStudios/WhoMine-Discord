package com.minersstudios.wholib.event;

import com.minersstudios.wholib.event.handle.HandlerExecutor;
import com.minersstudios.wholib.executor.Executable;
import com.minersstudios.wholib.module.AbstractModuleComponent;
import com.minersstudios.wholib.module.Module;
import com.minersstudios.wholib.module.ModuleComponent;
import com.minersstudios.wholib.packet.PacketContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event container that can be executed by
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
public abstract class EventContainer<M extends Module, E>
        extends AbstractModuleComponent<M>
        implements ModuleComponent<M>, Executable {

    private final E event;

    /**
     * Event container constructor
     *
     * @param module The module that registers and handles the event
     * @param event  The event associated with this event container
     */
    protected EventContainer(
            final @NotNull M module,
            final @NotNull E event
    ) {
        super(module);

        this.event = event;
    }

    /**
     * Returns the event
     *
     * @return The event
     */
    public @NotNull E getEvent() {
        return this.event;
    }
}
