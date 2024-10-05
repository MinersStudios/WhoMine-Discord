package com.minersstudios.whomine.api.event;

import com.google.common.base.Joiner;
import com.minersstudios.whomine.api.listener.Listener;
import com.minersstudios.whomine.api.listener.ListenerManager;
import com.minersstudios.whomine.api.registrable.Registrable;
import com.minersstudios.whomine.api.throwable.ListenerException;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * An abstract class that represents an event listener.
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
 *             {@link ListenerManager#register(Registrable)} method
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link #onUnregister()}</td>
 *         <td>
 *             Called when the listener is unregistered by a manager in the
 *             {@link ListenerManager#unregister(Registrable)} method
 *         </td>
 *     </tr>
 * </table>
 *
 * @param <K> The key type of the event listener
 * @param <C> The event container type of the event listener
 *
 * @see EventContainer
 * @see EventExecutor
 */
@SuppressWarnings("unused")
public abstract class EventListener<K, C extends EventContainer<?, ?>>
        implements Listener<K> {

    private final K key;
    private final Map<EventOrder, EventExecutor> executorMap;

    /**
     * Constructs a new event listener.
     * <p>
     * This constructor will automatically retrieve all event handlers from the
     * listener class and key from the {@link ListenFor} annotation.
     *
     * @throws ClassCastException If the event class is not a subclass of
     *                            annotated event class
     * @throws ListenerException  If the listener has duplicate event handlers
     *                            for the same order, or if the listener does
     *                            not have a {@link ListenFor} annotation
     */
    @SuppressWarnings("unchecked")
    protected EventListener() throws ClassCastException, ListenerException {
        this.key = (K) this.getListenFor().value();
        this.executorMap = this.retrieveExecutors();
    }

    /**
     * Constructs a new event listener with the specified key.
     * <p>
     * This constructor will automatically retrieve all event handlers from the
     * listener class.
     *
     * @param key The key of the event listener
     * @throws ListenerException If the listener has duplicate event handlers
     *                           for the same order
     */
    protected EventListener(final @NotNull K key) throws ListenerException {
        this.key = key;
        this.executorMap = this.retrieveExecutors();
    }

    @Override
    public @NotNull K getKey() {
        return this.key;
    }

    /**
     * Returns an unmodifiable set of event priorities
     *
     * @return An unmodifiable set of event priorities
     */
    public @NotNull @Unmodifiable Set<EventOrder> priorities() {
        return Collections.unmodifiableSet(this.executorMap.keySet());
    }

    /**
     * Returns an unmodifiable list of event executors
     *
     * @return An unmodifiable list of event executors
     */
    public @NotNull @Unmodifiable Collection<EventExecutor> executors() {
        return Collections.unmodifiableCollection(this.executorMap.values());
    }

    /**
     * Returns a hash code value for this event listener
     *
     * @return A hash code value for this event listener
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.key.hashCode();
        result = prime * result + this.executorMap.hashCode();

        return result;
    }

    /**
     * Returns whether the event listener contains the specified order
     *
     * @param order The event order
     * @return True if the event listener contains the specified order
     */
    public boolean containsOrder(final @NotNull EventOrder order) {
        return this.executorMap.containsKey(order);
    }

    /**
     * Returns whether the event listener contains the specified executor
     *
     * @param executor The event executor
     * @return True if the event listener contains the specified executor
     */
    public boolean containsExecutor(final @NotNull EventExecutor executor) {
        return this.executorMap.containsValue(executor);
    }

    /**
     * Indicates whether some other object is "equal to" this event listener
     *
     * @param obj The reference object with which to compare
     * @return True if this event listener is the same as the obj argument
     */
    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof final EventListener<?, ?> listener
                        && this.key.equals(listener.key)
                        && this.executorMap.equals(listener.executorMap)
                );
    }

    /**
     * Returns a string representation of this event listener
     *
     * @return A string representation of this event listener
     */
    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{' +
                "key=" + this.key +
                (
                        this.executorMap == null
                        ? '}'
                        : ", executors=[" + Joiner.on(", ").join(this.executorMap.values()) + "]}"
                );
    }

    /**
     * Calls all event executors with the specified event container
     *
     * @param container The event container
     */
    public void call(final @NotNull C container) {
        for (final var executor : this.executorMap.values()) {
            if (this.isIgnoringCancelled(executor.getParams(), container)) {
                continue;
            }

            executor.execute(this, container);
        }
    }

    /**
     * Calls the event executor with the specified event container and order
     *
     * @param order     The event order
     * @param container The event container
     */
    public void call(
            final @NotNull EventOrder order,
            final @NotNull C container
    ) {
        final EventExecutor executor = this.executorMap.get(order);

        if (
                executor != null
                && !this.isIgnoringCancelled(executor.getParams(), container)
        ) {
            executor.execute(this, container);
        }
    }

    private @NotNull ListenFor getListenFor() throws ListenerException {
        final ListenFor annotation = this.getClass().getAnnotation(ListenFor.class);

        if (annotation == null) {
            throw new ListenerException("Listener must have " + ListenFor.class.getSimpleName() + " annotation");
        }

        return annotation;
    }

    private @NotNull Map<EventOrder, EventExecutor> retrieveExecutors() throws ListenerException {
        final var map = new Object2ObjectAVLTreeMap<EventOrder, EventExecutor>(EventOrder::compareTo);

        for (final var method : this.getClass().getMethods()) {
            final var parameterTypes = method.getParameterTypes();

            if (
                    parameterTypes.length != 1
                    || parameterTypes[0].isAssignableFrom(EventContainer.class)
            ) {
                continue;
            }

            final EventHandler handler = method.getAnnotation(EventHandler.class);

            if (handler != null) {
                final EventOrder order = handler.order();

                if (map.containsKey(order)) {
                    throw new ListenerException("Duplicate event handler for " + order + " order in " + this);
                }

                map.put(
                        order,
                        EventExecutor.of(method, handler)
                );
            }
        }

        return map;
    }

    private boolean isIgnoringCancelled(
            final @NotNull EventHandlerParams params,
            final @NotNull C container
    ) {
        return params.isIgnoringCancelled() && container.isCancelled();
    }
}
