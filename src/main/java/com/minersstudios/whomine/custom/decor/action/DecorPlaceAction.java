package com.minersstudios.whomine.custom.decor.action;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.decor.event.CustomDecorPlaceEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an action to be performed when a custom decor is placed
 */
@FunctionalInterface
public interface DecorPlaceAction {
    DecorPlaceAction NONE = (plugin, event) -> {};

    /**
     * Performs this action on the given event
     *
     * @param plugin The plugin
     * @param event  Custom decor place event
     */
    void execute(
            final @NotNull WhoMine plugin,
            final @NotNull CustomDecorPlaceEvent event
    );

    /**
     * @param after The operation to perform after this operation
     * @return A composed {@code DecorPlaceAction} that performs in sequence this
     *         operation followed by the {@code after} operation
     * @throws NullPointerException If {@code after} is null
     */
    default @NotNull DecorPlaceAction andThen(final @NotNull DecorPlaceAction after) {
        return (plugin, event) -> {
            this.execute(plugin, event);
            after.execute(plugin, event);
        };
    }

    /**
     * @return Whether this action is set
     */
    default boolean isSet() {
        return this != NONE;
    }
}
