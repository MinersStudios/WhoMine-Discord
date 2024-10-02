package com.minersstudios.whomine.api.event;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * An annotation that marks a class as a listener for an event
 *
 * @see EventListener
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ListenFor {

    /**
     * Returns the class of the event that the listener listens for
     *
     * @return The class of the event that the listener listens for
     */
    @NotNull Class<?> eventClass();
}
