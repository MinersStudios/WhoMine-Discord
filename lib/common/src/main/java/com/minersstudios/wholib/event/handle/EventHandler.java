package com.minersstudios.wholib.event.handle;

import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.handler.HandlerParams;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * An annotation that marks a method as an event handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The order of the event</li>
 * </ul>
 *
 * @see EventOrder
 * @see HandlerParams
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {

    /**
     * Returns the order of the event.
     * <p>
     * <b>Handling occurs in the following order :</b>
     * <ol>
     *     <li>{@link EventOrder#LOWEST}</li>
     *     <li>{@link EventOrder#LOW}</li>
     *     <li>{@link EventOrder#NORMAL}</li>
     *     <li>{@link EventOrder#HIGH}</li>
     *     <li>{@link EventOrder#HIGHEST}</li>
     *     <li>{@link EventOrder#CUSTOM}</li>
     * </ol>
     *
     * @return The order of the event
     */
    @NotNull EventOrder value() default EventOrder.NORMAL;
}
