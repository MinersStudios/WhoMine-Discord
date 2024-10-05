package com.minersstudios.whomine.event;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.CancellableEventContainer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a paper event container.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The main module that registers and handles the event</li>
 *     <li>The event itself</li>
 * </ul>
 *
 * @param <E> The event type
 *
 * @see PaperEventListener
 */
@SuppressWarnings("unused")
public class PaperEventContainer<E extends Event> extends CancellableEventContainer<WhoMine, E> {

    private PaperEventContainer(
            final @NotNull WhoMine module,
            final @NotNull E event
    ) {
        super(module, event);
    }

    @Override
    public boolean isCancelled() {
        return this.getEvent() instanceof final Cancellable cancellable
                && cancellable.isCancelled();
    }

    /**
     * Creates a new paper event container with the given module and event
     *
     * @param module The main module that registers and handles the event
     * @param event  The event associated with this container
     * @return A new container instance
     */
    public static <E extends Event> @NotNull PaperEventContainer<E> of(
            final @NotNull WhoMine module,
            final @NotNull E event
    ) {
        return new PaperEventContainer<>(module, event);
    }
}
