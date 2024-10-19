package com.minersstudios.wholib.paper.custom.decor.action;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.custom.decor.event.CustomDecorClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an action to be performed when a custom decor is clicked
 */
@FunctionalInterface
public interface DecorClickAction {
    DecorClickAction NONE = (plugin, event) -> {};

    /**
     * Performs this action on the given event
     *
     * @param plugin The plugin
     * @param event  Custom decor click event
     */
    void execute(
            final @NotNull WhoMine plugin,
            final @NotNull CustomDecorClickEvent event
    );

    /**
     * @param after  The operation to perform after this operation
     * @return A composed {@code DecorClickAction} that performs in sequence this
     *         operation followed by the {@code after} operation
     * @throws NullPointerException If {@code after} is null
     */
    default @NotNull DecorClickAction andThen(final @NotNull DecorClickAction after) {
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
