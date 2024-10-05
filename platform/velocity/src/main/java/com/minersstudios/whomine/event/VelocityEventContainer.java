package com.minersstudios.whomine.event;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.EventContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a velocity event container.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The main module that registers and handles the event</li>
 *     <li>The event itself</li>
 * </ul>
 *
 * @param <E> The event type
 *
 * @see VelocityEventListener
 */
@SuppressWarnings("unused")
public class VelocityEventContainer<E> extends EventContainer<WhoMine, E> {

    private VelocityEventContainer(
            final @NotNull WhoMine module,
            final @NotNull E event
    ) {
        super(module, event);
    }

    /**
     * Creates a new paper event container with the given module and event
     *
     * @param module The main module that registers and handles the event
     * @param event  The event associated with this container
     * @return A new container instance
     */
    public static <E> @NotNull VelocityEventContainer<E> of(
            final @NotNull WhoMine module,
            final @NotNull E event
    ) {
        return new VelocityEventContainer<>(module, event);
    }
}
