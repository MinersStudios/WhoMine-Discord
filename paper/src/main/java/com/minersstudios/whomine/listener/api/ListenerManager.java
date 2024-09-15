package com.minersstudios.whomine.listener.api;

import com.google.common.collect.Sets;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.listener.impl.discord.CommandAutoCompleteInteractionListener;
import com.minersstudios.whomine.listener.impl.discord.MessageReceivedListener;
import com.minersstudios.whomine.listener.impl.discord.SlashCommandInteractionListener;
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
import com.minersstudios.whomine.api.packet.PacketEvent;
import com.minersstudios.whomine.api.packet.PacketType;
import com.minersstudios.whomine.api.status.StatusWatcher;
import com.minersstudios.whomine.packet.PaperPacketEvent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Listener manager class.
 * <br>
 * This class is responsible for managing various types of plugin listeners.
 *
 * @see DiscordListener
 * @see EventListener
 * @see PacketListener
 */
public final class ListenerManager {
    private final WhoMine plugin;
    private final List<DiscordListener> discordList;
    private final List<EventListener> eventList;
    private final Map<PacketType, List<PacketListener>> receivePacketMap;
    private final Map<PacketType, List<PacketListener>> sendPacketMap;

    /**
     * Constructs a new listener manager
     *
     * @param plugin Plugin instance
     */
    public ListenerManager(final @NotNull WhoMine plugin) {
        this.plugin = plugin;
        this.discordList = new ObjectArrayList<>();
        this.eventList = new ObjectArrayList<>();
        this.receivePacketMap = new Object2ObjectOpenHashMap<>();
        this.sendPacketMap = new Object2ObjectOpenHashMap<>();
    }

    /**
     * Returns an unmodifiable view of the discord listeners
     *
     * @return An unmodifiable view of the discord listeners
     */
    public @NotNull @UnmodifiableView Collection<DiscordListener> discordListeners() {
        return Collections.unmodifiableCollection(this.discordList);
    }

    /**
     * Returns an unmodifiable view of the event listeners
     *
     * @return An unmodifiable view of the event listeners
     */
    public @NotNull @UnmodifiableView Collection<EventListener> eventListeners() {
        return Collections.unmodifiableCollection(this.eventList);
    }

    /**
     * Returns an unmodifiable collection of packet listeners
     *
     * @return An unmodifiable collection of packet listeners
     */
    public @NotNull @Unmodifiable Collection<PacketListener> packetListeners() {
        final var map = new ObjectArrayList<PacketListener>();

        for (final var list : this.receivePacketMap.values()) {
            map.addAll(list);
        }

        for (final var list : this.sendPacketMap.values()) {
            map.addAll(list);
        }

        return Collections.unmodifiableCollection(map);
    }

    /**
     * Gets packet listeners for the specified packet type
     *
     * @param packetType Packet type
     * @return An unmodifiable collection of packet listeners for the specified
     *         packet type
     */
    public @NotNull @Unmodifiable Collection<PacketListener> packetListeners(final @NotNull PacketType packetType) {
        final var list =
                packetType.isServerbound()
                ? this.receivePacketMap.get(packetType)
                : this.sendPacketMap.get(packetType);

        return list != null
                ? Collections.unmodifiableCollection(list)
                : Collections.emptyList();
    }

    /**
     * Returns an unmodifiable collection of packet listeners
     *
     * @return An unmodifiable collection of packet listeners
     */
    public @NotNull @Unmodifiable Collection<PacketListener> receivePacketListeners() {
        final var collection = new ObjectArrayList<PacketListener>();

        for (final var list : this.receivePacketMap.values()) {
            collection.addAll(list);
        }

        return Collections.unmodifiableCollection(collection);
    }

    /**
     * Returns an unmodifiable collection of send packet listeners
     *
     * @return An unmodifiable collection of send packet listeners
     */
    public @NotNull @Unmodifiable Collection<PacketListener> sendPacketListeners() {
        final var collection = new ObjectArrayList<PacketListener>();

        for (final var list : this.sendPacketMap.values()) {
            collection.addAll(list);
        }

        return Collections.unmodifiableCollection(collection);
    }

    /**
     * Returns an unmodifiable view of the packet type set
     *
     * @return An unmodifiable view of the packet type set
     */
    public @NotNull @UnmodifiableView Set<PacketType> packetTypeSet() {
        return Sets.union(this.receivePacketMap.keySet(), this.sendPacketMap.keySet());
    }

    /**
     * Returns an unmodifiable view of the receive packet type set
     *
     * @return An unmodifiable view of the receive packet type set
     */
    public @NotNull @UnmodifiableView Set<PacketType> receivePacketTypeSet() {
        return Collections.unmodifiableSet(this.receivePacketMap.keySet());
    }

    /**
     * Returns an unmodifiable view of the send packet type set
     *
     * @return An unmodifiable view of the send packet type set
     */
    public @NotNull @UnmodifiableView Set<PacketType> sendPacketTypeSet() {
        return Collections.unmodifiableSet(this.sendPacketMap.keySet());
    }

    /**
     * Returns whether the listener manager contains the specified discord
     * listener
     *
     * @param listener Listener to be checked
     * @return True if the listener manager contains the specified listener,
     *         false otherwise
     */
    @Contract("null -> false")
    public boolean containsDiscord(final @Nullable DiscordListener listener) {
        return listener != null
                && this.discordList.contains(listener);
    }

    /**
     * Returns whether the listener manager contains the specified event
     * listener
     *
     * @param listener Listener to be checked
     * @return True if the listener manager contains the specified listener,
     *         false otherwise
     */
    @Contract("null -> false")
    public boolean containsEvent(final @Nullable EventListener listener) {
        return listener != null
                && this.eventList.contains(listener);
    }

    /**
     * Returns whether the listener manager contains the specified packet
     * listener
     *
     * @param listener Listener to be checked
     * @return True if the listener manager contains the specified listener,
     *         false otherwise
     */
    @Contract("null -> false")
    public boolean containsPacket(final @Nullable PacketListener listener) {
        if (listener == null) {
            return false;
        }

        for (final var list : this.receivePacketMap.values()) {
            if (list.contains(listener)) {
                return true;
            }
        }

        for (final var list : this.sendPacketMap.values()) {
            if (list.contains(listener)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the listener manager contains the specified packet type
     *
     * @param packetType Packet type to be checked
     * @return True if the listener manager contains the specified packet type,
     *         false otherwise
     */
    public boolean containsPacketType(final @Nullable PacketType packetType) {
        return packetType != null
                && (
                        packetType.isServerbound()
                        ? this.receivePacketMap.containsKey(packetType)
                        : this.sendPacketMap.containsKey(packetType)
                );
    }

    /**
     * Calls a packet event to all registered packet listeners with the
     * whitelist containing the packet type of the event
     *
     * @param event Packet event to be called
     * @see PacketEvent
     */
    public void callPacketReceiveEvent(final @NotNull PaperPacketEvent event) {
        final PacketType packetType = event.getPacketContainer().getType();

        if (this.containsPacketType(packetType)) {
            for (final var listener : this.packetListeners(packetType)) {
                listener.onPacketReceive(event);
            }
        }
    }

    /**
     * Calls a packet event to all registered packet listeners with the
     * whitelist containing the packet type of the event
     *
     * @param event Packet event to be called
     * @see PacketEvent
     */
    public void callPacketSendEvent(final @NotNull PaperPacketEvent event) {
        final PacketType packetType = event.getPacketContainer().getType();

        if (this.containsPacketType(packetType)) {
            for (final var listener : this.packetListeners(packetType)) {
                listener.onPacketSend(event);
            }
        }
    }

    /**
     * Bootstraps all listeners
     */
    public void bootstrap() {
        //<editor-fold desc="Discord listeners" defaultstate="collapsed">
        this.plugin.getStatusHandler().addWatcher(
                StatusWatcher.builder()
                .statuses(WhoMine.LOADED_DISCORD)
                .successRunnable(
                        () -> {
                            new CommandAutoCompleteInteractionListener(this.plugin).register();
                            new MessageReceivedListener(this.plugin).register();
                            new SlashCommandInteractionListener(this.plugin).register();
                        }
                )
                .build()
        );
        //</editor-fold>

        //<editor-fold desc="Event listeners" defaultstate="collapsed">

        // Block listeners
        new BlockBreakListener(this.plugin).register();
        new BlockDamageListener(this.plugin).register();
        new BlockDropItemListener(this.plugin).register();
        new BlockExplodeListener(this.plugin).register();
        new BlockPistonExtendListener(this.plugin).register();
        new BlockPistonRetractListener(this.plugin).register();
        new BlockPlaceListener(this.plugin).register();
        new NotePlayListener(this.plugin).register();

        // Chat listeners
        new AsyncChatListener(this.plugin).register();

        // Command listeners
        new UnknownCommandListener(this.plugin).register();

        // Entity listeners
        new EntityChangeBlockListener(this.plugin).register();
        new EntityDamageByEntityListener(this.plugin).register();
        new EntityDamageListener(this.plugin).register();
        new EntityDismountListener(this.plugin).register();
        new EntityExplodeListener(this.plugin).register();

        // Hanging listeners
        new HangingBreakByEntityListener(this.plugin).register();

        // Inventory listeners
        new InventoryClickListener(this.plugin).register();
        new InventoryCloseListener(this.plugin).register();
        new InventoryCreativeListener(this.plugin).register();
        new InventoryDragListener(this.plugin).register();
        new InventoryOpenListener(this.plugin).register();
        new PrepareAnvilListener(this.plugin).register();
        new PrepareItemCraftListener(this.plugin).register();

        // Player listeners
        new AsyncPlayerPreLoginListener(this.plugin).register();
        new PlayerAdvancementDoneListener(this.plugin).register();
        new PlayerBucketEmptyListener(this.plugin).register();
        new PlayerChangedWorldListener(this.plugin).register();
        new PlayerCommandPreprocessListener(this.plugin).register();
        new PlayerDeathListener(this.plugin).register();
        new PlayerDropItemListener(this.plugin).register();
        new PlayerEditBookListener(this.plugin).register();
        new PlayerGameModeChangeListener(this.plugin).register();
        new PlayerInteractEntityListener(this.plugin).register();
        new PlayerInteractListener(this.plugin).register();
        new PlayerJoinListener(this.plugin).register();
        new PlayerKickListener(this.plugin).register();
        new PlayerMoveListener(this.plugin).register();
        new PlayerQuitListener(this.plugin).register();
        new PlayerResourcePackStatusListener(this.plugin).register();
        new PlayerSpawnLocationListener(this.plugin).register();
        new PlayerSpawnLocationListener(this.plugin).register();
        new PlayerStopSpectatingEntityListener(this.plugin).register();
        new PlayerTeleportListener(this.plugin).register();

        // Server listeners
        new ServerCommandListener(this.plugin).register();

        // Mechanic listeners
        new BanSwordMechanic(this.plugin).register();
        new CardBoxMechanic(this.plugin).register();
        new CocaineMechanic(this.plugin).register();
        new DamageableItemMechanic(this.plugin).register();
        new DosimeterMechanic(this.plugin).register();
        new PoopMechanic(this.plugin).register();

        //</editor-fold>

        //<editor-fold desc="Packet listeners" defaultstate="collapsed">
        new PlayerActionListener(this.plugin).register();
        new PlayerUpdateSignListener(this.plugin).register();
        new SwingArmListener(this.plugin).register();
        //</editor-fold>
    }

    void registerDiscord(final @NotNull DiscordListener listener) throws IllegalStateException {
        if (this.discordList.contains(listener)) {
            throw new IllegalStateException("Listener is already registered");
        }

        this.plugin.getDiscordManager().getJda()
        .ifPresent(
                jda -> {
                    synchronized (this.discordList) {
                        this.discordList.add(listener);
                    }

                    jda.addEventListener(listener);
                }
        );
    }

    void registerEvent(final @NotNull EventListener listener) throws IllegalStateException {
        if (this.eventList.contains(listener)) {
            throw new IllegalStateException("Listener is already registered");
        }

        synchronized (this.eventList) {
            this.eventList.add(listener);
        }

        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    void registerPacket(final @NotNull PacketListener listener) throws IllegalStateException, IllegalArgumentException {
        if (this.containsPacket(listener)) {
            throw new IllegalStateException("Listener is already registered");
        }

        final var receiveWhiteList = listener.getReceiveWhiteList();
        final var sendWhiteList = listener.getSendWhiteList();
        final int receiveSize = receiveWhiteList.size();
        final int sendSize = sendWhiteList.size();

        if (
                receiveSize == 0
                && sendSize == 0
        ) {
            throw new IllegalArgumentException("Packet listener must have at least one packet type in the whitelist");
        }

        if (receiveSize != 0) {
            for (final var packetType : receiveWhiteList) {
                synchronized (this.receivePacketMap) {
                    this.receivePacketMap
                    .computeIfAbsent(packetType, unused -> new ObjectArrayList<>())
                    .add(listener);
                }
            }
        }

        if (sendSize != 0) {
            for (final var packetType : sendWhiteList) {
                synchronized (this.sendPacketMap) {
                    this.sendPacketMap
                    .computeIfAbsent(packetType, unused -> new ObjectArrayList<>())
                    .add(listener);
                }
            }
        }
    }
}
