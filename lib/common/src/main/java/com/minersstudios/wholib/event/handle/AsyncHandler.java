package com.minersstudios.wholib.event.handle;

import com.minersstudios.wholib.event.EventOrder;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * An annotation that marks a method as an async event handler.
 * <p>
 * <b>It contains :</b>
 * <ul>
 *     <li>The order of the event</li>
 *     <li>
 *         Whether the handler must be called asynchronously
 *     </li>
 * </ul>
 *
 * @see EventOrder
 * @see AsyncHandlerParams
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsyncHandler {

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
     * Returns whether the handler must be called asynchronously.
     * <p>
     * The default value is {@value AsyncHandlerParams#DEFAULT_ASYNC}.
     *
     * @return Whether the handler must be called asynchronously
     */
    boolean async() default AsyncHandlerParams.DEFAULT_ASYNC;
}
