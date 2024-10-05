package com.minersstudios.whomine.event;

import com.minersstudios.whomine.api.event.EventExecutor;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.event.handler.CancellableHandler;
import com.minersstudios.whomine.api.event.handler.CancellableHandlerParams;
import com.minersstudios.whomine.api.listener.ListenFor;
import com.minersstudios.whomine.api.throwable.ListenerException;
import com.minersstudios.whomine.listener.api.PaperListenerManager;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class that represents a paper event listener.
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
 *             {@link PaperListenerManager#register(com.minersstudios.whomine.api.listener.Listener)} method
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link #onUnregister()}</td>
 *         <td>
 *             Called when the listener is unregistered by a manager in the
 *             {@link PaperListenerManager#unregister(com.minersstudios.whomine.api.listener.Listener)} method
 *         </td>
 *     </tr>
 * </table>
 *
 * @see PaperEventContainer
 * @see EventExecutor
 */
@SuppressWarnings("unused")
public abstract class PaperEventListener
        extends EventListener<Class<? extends Event>, PaperEventContainer<? extends Event>>
        implements Listener {

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
    protected PaperEventListener() throws ClassCastException, ListenerException {
        super(CancellableHandler.class, CancellableHandlerParams::of);
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
    protected PaperEventListener(final @NotNull Class<? extends Event> key) throws ListenerException {
        super(key, CancellableHandler.class, CancellableHandlerParams::of);
    }
}
