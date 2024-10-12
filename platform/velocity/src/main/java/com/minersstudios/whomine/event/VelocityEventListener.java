package com.minersstudios.whomine.event;

import com.minersstudios.whomine.api.event.handle.HandlerExecutor;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.event.handle.AsyncHandler;
import com.minersstudios.whomine.api.event.handle.AsyncHandlerParams;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.api.listener.Listener;
import com.minersstudios.whomine.api.throwable.ListenerException;
import com.minersstudios.whomine.listener.api.VelocityListenerManager;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class that represents a velocity event listener.
 * <table>
 *     <caption>Available optional overridable methods</caption>
 *     <tr>
 *         <th>Method</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #onRegister()}</td>
 *         <td>
 *             Called when the listener is registered by a manager in the
 *             {@link VelocityListenerManager#register(Listener)} method
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link #onUnregister()}</td>
 *         <td>
 *             Called when the listener is unregistered by a manager in the
 *             {@link VelocityListenerManager#unregister(Listener)} method
 *         </td>
 *     </tr>
 * </table>
 *
 * @see VelocityEventContainer
 * @see HandlerExecutor
 */
@SuppressWarnings("unused")
public abstract class VelocityEventListener
        extends EventListener<Class<?>, VelocityEventContainer<?>> {

    /**
     * Constructs a new event listener.
     * <p>
     * This constructor will automatically retrieve all event handlers from the
     * listener class and event class from the {@link ListenFor} annotation.
     *
     * @throws ClassCastException If the event class is not a subclass of
     *                            annotated event class
     * @throws ListenerException  If the listener has duplicate event handlers
     *                            for the same order, or if the listener does
     *                            not have a {@link ListenFor} annotation
     */
    protected VelocityEventListener() throws ClassCastException, ListenerException {
        super(AsyncHandler.class, AsyncHandlerParams::of);
    }

    /**
     * Constructs a new event listener with the specified event class.
     * <p>
     * This constructor will automatically retrieve all event handlers from the
     * listener class.
     *
     * @param key The key of the event listener
     * @throws ListenerException If the listener has duplicate event handlers
     *                           for the same order
     */
    protected VelocityEventListener(final @NotNull Class<?> key) throws ListenerException {
        super(key, AsyncHandler.class, AsyncHandlerParams::of);
    }
}
