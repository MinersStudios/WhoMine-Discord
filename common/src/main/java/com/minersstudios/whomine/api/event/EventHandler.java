package com.minersstudios.whomine.api.event;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * An annotation that marks a method as an event handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The order of the event</li>
 *     <li>
 *         Whether to ignore an event call if it is canceled by a lower order
 *         event in this or another event listener
 *     </li>
 * </ul>
 *
 * @see EventOrder
 * @see EventHandlerParams
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
    @NotNull EventOrder order() default EventOrder.NORMAL;

    /**
     * Returns whether to ignore an event call if it is canceled.
     * <p>
     * If set to {@code true}, the event call will be ignored if it is canceled
     * by a lower order event in this or another event listener.
     *
     * @return Whether to ignore an event call if it is canceled
     */
    boolean ignoreCancelled() default false;
}
