package com.minersstudios.wholib.paper.listener;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.event.EventContainer;
import com.minersstudios.wholib.event.handle.CancellableHandlerParams;
import com.minersstudios.wholib.event.EventListener;
import com.minersstudios.wholib.event.EventOrder;
import com.minersstudios.wholib.listener.Listener;
import com.minersstudios.wholib.listener.ListenerManager;
import com.minersstudios.wholib.listener.ListenerMap;
import com.minersstudios.wholib.packet.PacketBound;
import com.minersstudios.wholib.paper.event.PaperEventContainer;
import com.minersstudios.wholib.paper.event.PaperEventListener;
import com.minersstudios.wholib.packet.PacketType;
import com.minersstudios.wholib.paper.packet.PaperPacketContainer;
import com.minersstudios.wholib.paper.packet.PaperPacketListener;
import com.minersstudios.wholib.paper.utility.ApiConverter;
import com.minersstudios.wholib.paper.utility.MSLogger;
import org.bukkit.event.Event;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * Listener manager class.
 * <br>
 * This class is responsible for managing event and packet listeners.
 *
 * @see PaperEventListener
 * @see PaperPacketListener
 */
@SuppressWarnings("unused")
public final class PaperListenerManager extends ListenerManager<WhoMine> {

    private final ListenerMap<Class<? extends Event>, PaperEventListener> eventMap;
    private final ListenerMap<PacketType, PaperPacketListener> receivePacketMap;
    private final ListenerMap<PacketType, PaperPacketListener> sendPacketMap;

    public PaperListenerManager(final @NotNull WhoMine module) {
        super(module);

        this.eventMap = new ListenerMap<>();
        this.receivePacketMap = new ListenerMap<>();
        this.sendPacketMap = new ListenerMap<>();
    }

    /**
     * Returns the unmodifiable listener map containing all event listeners
     *
     * @return The unmodifiable listener map containing all event listeners
     */
    public @NotNull @UnmodifiableView ListenerMap<Class<? extends Event>, PaperEventListener> eventMap() {
        return this.eventMap.toUnmodifiableView();
    }

    /**
     * Returns the unmodifiable listener map containing all packet listeners
     *
     * @return The unmodifiable listener map containing all packet listeners
     */
    public @NotNull @Unmodifiable ListenerMap<PacketType, PaperPacketListener> packetMap() {
        final var map = new ListenerMap<PacketType, PaperPacketListener>();

        map.putAll(this.receivePacketMap);
        map.putAll(this.sendPacketMap);

        return map.toUnmodifiableView();
    }

    /**
     * Returns the unmodifiable view of the
     * {@link PacketBound#CLIENTBOUND clientbound} packet map
     *
     * @return The unmodifiable view of the
     *         {@link PacketBound#CLIENTBOUND clientbound} packet map
     */
    public @NotNull @UnmodifiableView ListenerMap<PacketType, PaperPacketListener> receivePacketMap() {
        return this.receivePacketMap.toUnmodifiableView();
    }

    /**
     * Returns the unmodifiable view of the
     * {@link PacketBound#SERVERBOUND serverbound} packet map
     *
     * @return The unmodifiable view of the
     *         {@link PacketBound#SERVERBOUND serverbound} packet map
     */
    public @NotNull @UnmodifiableView ListenerMap<PacketType, PaperPacketListener> sendPacketMap() {
        return this.sendPacketMap.toUnmodifiableView();
    }

    /**
     * Returns the event listeners for the given event
     *
     * @param event The event
     * @return The event listeners for the given event
     * @see #getEventListeners(Class)
     */
    public @Nullable @UnmodifiableView List<PaperEventListener> getEventListeners(final @NotNull Event event) {
        return this.getEventListeners(event.getClass());
    }

    /**
     * Returns the event listeners for the given event class
     *
     * @param eventClass The event class
     * @return The event listeners for the given event class
     * @see #getEventListeners(Event)
     */
    public @Nullable @UnmodifiableView List<PaperEventListener> getEventListeners(final @NotNull Class<? extends Event> eventClass) {
        return this.eventMap.get(eventClass);
    }

    /**
     * Returns the packet listeners for the given packet type
     *
     * @param type The packet type
     * @return The packet listeners for the given packet type
     */
    public @Nullable @UnmodifiableView List<PaperPacketListener> getPacketListeners(final @NotNull PacketType type) {
        return type.isServerbound()
               ? this.receivePacketMap.get(type)
               : this.sendPacketMap.get(type);
    }

    @Override
    public boolean register(final @NotNull Listener<?> listener) {
        final boolean result = switch (listener) {
            case PaperEventListener eventListener -> {
                final var eventClass = eventListener.getKey();

                if (this.eventMap.put(eventListener) == null) {
                    MSLogger.warning("Tried to register an event listener that is already registered: " + eventListener);

                    yield false;
                }

                for (final var executor : eventListener.executors()) {
                    final var params = executor.getParams();
                    final EventOrder order = params.getOrder();

                    this.getModule().getServer().getPluginManager().registerEvent(
                            eventClass,
                            eventListener,
                            ApiConverter.apiToBukkit(order),
                            (__, event) -> {
                                if (!eventClass.isAssignableFrom(event.getClass())) {
                                    return;
                                }

                                eventListener.call(
                                        order,
                                        PaperEventContainer.of(
                                                this.getModule(),
                                                event
                                        )
                                );
                            },
                            this.getModule(),
                            params instanceof final CancellableHandlerParams cancellable
                            && cancellable.isIgnoringCancelled()
                    );
                }

                yield true;
            }
            case PaperPacketListener packetListener -> {
                final PacketType type = packetListener.getKey();

                yield type.isServerbound()
                      ? this.receivePacketMap.put(packetListener) != null
                      : this.sendPacketMap.put(packetListener) != null;
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
            case PaperEventListener eventListener -> this.eventMap.remove(eventListener) != null;
            case PaperPacketListener packetListener -> {
                final PacketType type = packetListener.getKey();

                yield (type.isServerbound()
                       ? this.receivePacketMap.remove(packetListener)
                       : this.sendPacketMap.remove(packetListener)) != null;
            }
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
        this.unregisterAll(this.receivePacketMap);
        this.unregisterAll(this.sendPacketMap);
    }

    @Override
    public boolean isRegistered(final @NotNull Listener<?> registrable) {
        return switch (registrable) {
            case PaperEventListener eventListener -> this.eventMap.containsListener(eventListener);
            case PaperPacketListener packetListener -> {
                final PacketType type = packetListener.getKey();

                yield type.isServerbound()
                      ? this.receivePacketMap.containsListener(packetListener)
                      : this.sendPacketMap.containsListener(packetListener);
            }
            default -> false;
        };
    }

    /**
     * Calls the registered listeners for the given event
     *
     * @param container The event container
     */
    public void call(final @NotNull PaperEventContainer<? extends Event> container) {
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
            final @NotNull PaperEventContainer<? extends Event> container
    ) {
        this.rawCall(
                order, container,
                () -> this.getEventListeners(container.getEvent())
        );
    }

    /**
     * Calls the registered listeners for the given packet
     *
     * @param container The packet container
     */
    public void call(final @NotNull PaperPacketContainer container) {
        this.rawCall(
                container,
                () -> this.getPacketListeners(container.getEvent().getType())
        );
    }

    /**
     * Calls the registered listeners for the given packet with the given
     * order
     *
     * @param order     The event order
     * @param container The packet container
     */
    public void call(
            final @NotNull EventOrder order,
            final @NotNull PaperPacketContainer container
    ) {
        this.rawCall(
                order, container,
                () -> this.getPacketListeners(container.getEvent().getType())
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
