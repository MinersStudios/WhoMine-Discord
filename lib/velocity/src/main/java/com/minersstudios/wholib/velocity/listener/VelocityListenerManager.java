package com.minersstudios.wholib.velocity.listener;

import com.minersstudios.wholib.velocity.WhoMine;
import com.minersstudios.wholib.event.EventContainer;
import com.minersstudios.wholib.event.EventListener;
import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.Listener;
import com.minersstudios.wholib.listener.ListenerManager;
import com.minersstudios.wholib.listener.ListenerMap;
import com.minersstudios.wholib.velocity.event.VelocityEventContainer;
import com.minersstudios.wholib.velocity.event.VelocityEventListener;
import com.velocitypowered.api.event.EventManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.function.Supplier;

/**
 * Listener manager class.
 * <br>
 * This class is responsible for managing event and packet listeners.
 *
 * @see VelocityEventListener
 */
@SuppressWarnings("unused")
public final class VelocityListenerManager extends ListenerManager<WhoMine> {

    private final ListenerMap<Class<?>, VelocityEventListener> eventMap;

    public VelocityListenerManager(final @NotNull WhoMine module) {
        super(module);

        this.eventMap = new ListenerMap<>();
    }

    /**
     * Returns the unmodifiable listener map containing all event listeners
     *
     * @return The unmodifiable listener map containing all event listeners
     */
    public @NotNull @UnmodifiableView ListenerMap<Class<?>, VelocityEventListener> eventMap() {
        return this.eventMap.toUnmodifiableView();
    }

    /**
     * Returns the event listeners for the given event
     *
     * @param event The event
     * @return The event listeners for the given event
     * @see #getEventListeners(Class)
     */
    public <E> @Nullable @UnmodifiableView List<VelocityEventListener> getEventListeners(final @NotNull E event) {
        return this.getEventListeners(event.getClass());
    }

    /**
     * Returns the event listeners for the given event class
     *
     * @param eventClass The event class
     * @return The event listeners for the given event class
     * @see #getEventListeners(Object)
     */
    public @Nullable @UnmodifiableView List<VelocityEventListener> getEventListeners(final @NotNull Class<?> eventClass) {
        return this.eventMap.get(eventClass);
    }

    @Override
    public boolean register(final @NotNull Listener<?> listener) {
        final boolean result = switch (listener) {
            case VelocityEventListener eventListener -> {
                final var eventClass = eventListener.getKey();

                if (this.eventMap.put(eventListener) == null) {
                    this.getModule().getLogger().warning("Tried to register an event listener that is already registered: " + eventListener);

                    yield false;
                }

                final WhoMine module = this.getModule();
                final EventManager eventManager = module.getServer().getEventManager();

                for (final var executor : eventListener.executors()) {
                    final var params = executor.getParams();
                    final EventOrder order = params.getOrder();

                    eventManager.register(
                            module,
                            eventClass,
                            (short) order.asNumber(),
                            (event) -> {
                                if (!eventClass.isAssignableFrom(event.getClass())) {
                                    return;
                                }

                                eventListener.call(
                                        order,
                                        VelocityEventContainer.of(
                                                this.getModule(),
                                                event
                                        )
                                );
                            }
                    );
                }

                yield true;
            }
            default -> throw new UnsupportedOperationException("Unknown listener type: " + listener.getClass());
        };

        if (result) {
            listener.onRegister();
        }

        return result;
    }

    @Override
    public boolean unregister(final @NotNull Listener<?> registrable) {
        final boolean result = switch (registrable) {
            case VelocityEventListener eventListener -> this.eventMap.remove(eventListener) != null;
            default -> false;
        };

        if (result) {
            registrable.onUnregister();
        }

        return result;
    }

    @Override
    public void unregisterAll() {
        this.unregisterAll(this.eventMap);
    }

    @Override
    public boolean isRegistered(final @NotNull Listener<?> registrable) {
        return switch (registrable) {
            case VelocityEventListener eventListener -> this.eventMap.containsListener(eventListener);
            default -> false;
        };
    }

    /**
     * Calls the registered listeners for the given event
     *
     * @param container The event container
     */
    public void call(final @NotNull VelocityEventContainer<?> container) {
        this.rawCall(
                container,
                () -> this.getEventListeners(container.getEvent())
        );
    }

    /**
     * Calls the registered listeners for the given event with the given
     * parameters
     *
     * @param order     The event order
     * @param container The event container
     */
    public void call(
            final @NotNull EventOrder order,
            final @NotNull VelocityEventContainer<?> container
    ) {
        this.rawCall(
                order, container,
                () -> this.getEventListeners(container.getEvent())
        );
    }

    private void unregisterAll(final @NotNull ListenerMap<?, ?> map) {
        for (final var listener : map.listeners()) {
            listener.onUnregister();
        }

        map.clear();
    }

    private <C extends EventContainer<?, ?>> void rawCall(
            final @NotNull C container,
            final @NotNull Supplier<List<? extends EventListener<?, C>>> supplier
    ) {
        final var listeners = supplier.get();

        if (listeners != null) {
            for (final var listener : listeners) {
                listener.call(container);
            }
        }
    }

    private <C extends EventContainer<?, ?>> void rawCall(
            final @NotNull EventOrder order,
            final @NotNull C container,
            final @NotNull Supplier<List<? extends EventListener<?, C>>> supplier
    ) {
        final var listeners = supplier.get();

        if (listeners != null) {
            for (final var listener : listeners) {
                listener.call(order, container);
            }
        }
    }
}
