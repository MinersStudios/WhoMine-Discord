package com.minersstudios.whomine.listener.api;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.event.EventContainer;
import com.minersstudios.whomine.api.event.handle.CancellableHandlerParams;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.listener.Listener;
import com.minersstudios.whomine.api.listener.ListenerManager;
import com.minersstudios.whomine.api.listener.ListenerMap;
import com.minersstudios.whomine.api.packet.PacketBound;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.listener.impl.event.block.*;
import com.minersstudios.whomine.listener.impl.event.chat.AsyncChatListener;
import com.minersstudios.whomine.listener.impl.event.command.UnknownCommandListener;
import com.minersstudios.whomine.listener.impl.event.entity.*;
import com.minersstudios.whomine.listener.impl.event.hanging.HangingBreakByEntityListener;
import com.minersstudios.whomine.listener.impl.event.inventory.*;
import com.minersstudios.whomine.listener.impl.event.mechanic.*;
import com.minersstudios.whomine.listener.impl.event.player.*;
import com.minersstudios.whomine.listener.impl.event.server.ServerCommandListener;
import com.minersstudios.whomine.listener.impl.packet.player.PlayerActionListener;
import com.minersstudios.whomine.listener.impl.packet.player.PlayerUpdateSignListener;
import com.minersstudios.whomine.listener.impl.packet.player.SwingArmListener;
import com.minersstudios.whomine.api.packet.PacketType;
import com.minersstudios.whomine.packet.PaperPacketContainer;
import com.minersstudios.whomine.packet.PaperPacketListener;
import com.minersstudios.whomine.utility.ApiConverter;
import com.minersstudios.whomine.utility.MSLogger;
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
     * Bootstraps all listeners
     */
    public void bootstrap() {
        //<editor-fold desc="Discord listeners" defaultstate="collapsed">

        // TODO: Discord listener manager
        //module.getStatusHandler().addWatcher(
        //        StatusWatcher.builder()
        //        .statuses(MainModule.LOADED_DISCORD)
        //        .successRunnable(
        //                () -> {
        //                    this.register(new CommandAutoCompleteInteractionListener());
        //                    this.register(new MessageReceivedListener());
        //                    this.register(new SlashCommandInteractionListener());
        //                }
        //        )
        //        .build()
        //);
        //</editor-fold>

        //<editor-fold desc="Event listeners" defaultstate="collapsed">

        // Block listeners
        this.register(new BlockBreakListener());

        this.register(new BlockDamageListener());
        this.register(new BlockDropItemListener());
        this.register(new BlockExplodeListener());
        this.register(new BlockPistonExtendListener());
        this.register(new BlockPistonRetractListener());
        this.register(new BlockPlaceListener());
        this.register(new NotePlayListener());

        // Chat listeners
        this.register(new AsyncChatListener());

        // Command listeners
        this.register(new UnknownCommandListener());

        // Entity listeners
        this.register(new EntityChangeBlockListener());
        this.register(new EntityDamageByEntityListener());
        this.register(new EntityDamageListener());
        this.register(new EntityDismountListener());
        this.register(new EntityExplodeListener());

        // Hanging listeners
        this.register(new HangingBreakByEntityListener());

        // Inventory listeners
        this.register(new InventoryClickListener());
        this.register(new InventoryCloseListener());
        this.register(new InventoryCreativeListener());
        this.register(new InventoryDragListener());
        this.register(new InventoryOpenListener());
        this.register(new PrepareAnvilListener());
        this.register(new PrepareItemCraftListener());

        // Player listeners
        this.register(new AsyncPlayerPreLoginListener());
        this.register(new PlayerAdvancementDoneListener());
        this.register(new PlayerBucketEmptyListener());
        this.register(new PlayerChangedWorldListener());
        this.register(new PlayerCommandPreprocessListener());
        this.register(new PlayerDeathListener());
        this.register(new PlayerDropItemListener());
        this.register(new PlayerEditBookListener());
        this.register(new PlayerGameModeChangeListener());
        this.register(new PlayerInteractEntityListener());
        this.register(new PlayerInteractListener());
        this.register(new PlayerJoinListener());
        this.register(new PlayerKickListener());
        this.register(new PlayerMoveListener());
        this.register(new PlayerQuitListener());
        this.register(new PlayerResourcePackStatusListener());
        this.register(new PlayerSpawnLocationListener());
        this.register(new PlayerStopSpectatingEntityListener());
        this.register(new PlayerTeleportListener());

        // Server listeners
        this.register(new ServerCommandListener());

        // Mechanic listeners
        this.register(new BanSwordMechanic.EntityDamageByEntity());
        this.register(new BanSwordMechanic.InventoryClick());
        this.register(new CardBoxMechanic.InventoryMoveItem());
        this.register(new CardBoxMechanic.InventoryDrag());
        this.register(new CardBoxMechanic.InventoryClick());
        this.register(new CocaineMechanic.PlayerItemConsume());
        this.register(new DamageableItemMechanic.PlayerItemDamage());
        this.register(new DosimeterMechanic.PlayerSwapHandItems());
        this.register(new DosimeterMechanic.PlayerItemHeld());
        this.register(new DosimeterMechanic.InventoryClick());
        this.register(new DosimeterMechanic.PlayerDropItem());
        this.register(new DosimeterMechanic.PlayerQuit());
        this.register(new DosimeterMechanic.PlayerInteract());
        this.register(new PoopMechanic.PlayerInteract());

        //</editor-fold>

        //<editor-fold desc="Packet listeners" defaultstate="collapsed">

        this.register(new PlayerActionListener());
        this.register(new PlayerUpdateSignListener());
        this.register(new SwingArmListener());

        //</editor-fold>
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
