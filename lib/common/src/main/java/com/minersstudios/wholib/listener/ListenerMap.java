package com.minersstudios.wholib.listener;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Represents a listener map that can store {@link Listener listeners}
 *
 * @param <K> The key type
 * @param <L> The listener type
 *
 * @see Listener
 * @see ListenerManager
 */
@SuppressWarnings("unused")
public class ListenerMap<K, L extends Listener<K>> {

    private final Object2ObjectMap<K, List<L>> delegate;

    /**
     * Constructs a new listener map with the default initial capacity and load
     * factor
     */
    public ListenerMap() {
        this(Hash.DEFAULT_INITIAL_SIZE);
    }

    /**
     * Constructs a new listener map with the given initial capacity and the
     * default load factor.
     * <p>
     * Also copies the entries from the given map.
     *
     * @param map The map to copy
     */
    public ListenerMap(final @NotNull ListenerMap<K, L> map) {
        this(map, Hash.DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new listener map with the given load factor and the initial
     * capacity of the given map.
     * <p>
     * Also copies the entries from the given map.
     *
     * @param map        The map to copy
     * @param loadFactor The load factor
     */
    public ListenerMap(
            final @NotNull ListenerMap<K, L> map,
            final float loadFactor
    ) {
        this(map.keyCount(), loadFactor);

        this.putAll(map);
    }

    /**
     * Constructs a new listener map with the given initial capacity and the
     * default load factor
     *
     * @param initialCapacity The initial capacity
     */
    public ListenerMap(final int initialCapacity) {
        this(initialCapacity, Hash.DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new listener map with the given initial capacity and load
     * factor
     *
     * @param initialCapacity The initial capacity
     * @param loadFactor      The load factor
     */
    public ListenerMap(
            final int initialCapacity,
            final float loadFactor
    ) {
        this.delegate = new Object2ObjectOpenHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * Returns an unmodifiable view of the key set
     *
     * @return An unmodifiable view of the key set
     */
    public @NotNull @UnmodifiableView Set<K> keySet() {
        return this.delegate.keySet();
    }

    /**
     * Returns an unmodifiable collection of all listeners in the map
     *
     * @return An unmodifiable collection of all listeners in the map
     */
    public @NotNull @Unmodifiable Collection<L> listeners() {
        final var listeners = new ObjectArrayList<L>();

        for (final var list : this.delegate.values()) {
            listeners.addAll(list);
        }

        return ObjectLists.unmodifiable(listeners);
    }

    /**
     * Returns an unmodifiable view of the entry set
     *
     * @return An unmodifiable view of the entry set
     */
    public @NotNull @UnmodifiableView Set<Map.Entry<K, List<L>>> entrySet() {
        return this.delegate.entrySet();
    }

    /**
     * Returns an unmodifiable list of listeners for the given key
     *
     * @param key The key
     * @return An unmodifiable list of listeners for the given key
     */
    @Contract("null -> null")
    public @Nullable @Unmodifiable List<L> get(final @Nullable K key) {
        final var listeners = this.delegate.get(key);

        return listeners == null
               ? null
               : Collections.unmodifiableList(listeners);
    }

    /**
     * Returns an unmodifiable list of listeners for the given key or the
     * default list if the key is not present
     *
     * @param key         The key
     * @param defaultList The default list
     * @return An unmodifiable list of listeners for the given key or the
     *         default list if the key is not present
     */
    @Contract("_, null -> null; null, !null -> param2")
    public @UnknownNullability @UnmodifiableView List<L> getOrDefault(
            final @Nullable K key,
            final @Nullable List<L> defaultList
    ) {
        final var result = this.delegate.getOrDefault(key, defaultList);

        return result == null
               ? null
               : Collections.unmodifiableList(result);
    }

    /**
     * Puts the given listener in the map
     *
     * @param listener The listener to put
     * @return An unmodifiable list of listeners for the key of the given
     *         listener or {@code null} if the listener is already present in
     *         the map
     */
    public @Nullable @UnmodifiableView List<L> put(final @NotNull L listener) {
        final var key = listener.getKey();
        var listeners = this.delegate.get(key);

        if (listeners == null) {
            listeners = new ObjectArrayList<>();

            this.delegate.put(key, listeners);
        } else if (listeners.contains(listener)) {
            return null;
        }

        listeners.add(listener);

        return Collections.unmodifiableList(listeners);
    }

    /**
     * Puts all listeners in the given list in the map
     *
     * @param listeners The listeners to put
     * @see #put(Listener)
     */
    public void putAll(final @NotNull List<L> listeners) {
        for (final var listener : listeners) {
            this.put(listener);
        }
    }

    /**
     * Puts all listeners in the given map in the map
     *
     * @param map The map to put
     * @see #put(Listener)
     */
    public void putAll(final @NotNull ListenerMap<K, L> map) {
        for (final var entry : map.entrySet()) {
            this.delegate.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Removes all listeners for the given key
     *
     * @param key The key
     * @return An unmodifiable list of removed listeners or {@code null} if the
     *         key is not present in the map
     */
    @Contract("null -> null")
    public @Nullable @Unmodifiable List<L> remove(final @Nullable K key) {
        final var listeners = this.delegate.remove(key);

        return listeners == null
               ? null
               : Collections.unmodifiableList(listeners);
    }

    /**
     * Removes the given listener from the map
     *
     * @param listener The listener to remove
     * @return An unmodifiable list of listeners for the key of the given
     *         listener or {@code null} if the listener is not present in the
     *         map
     */
    public @Nullable @UnmodifiableView List<L> remove(final @NotNull L listener) {
        final var listeners = this.delegate.get(listener.getKey());

        if (listeners == null) {
            return null;
        }

        final int size = listeners.size();

        if (size <= 1) {
            this.delegate.remove(listener.getKey());

            return Collections.emptyList();
        }

        listeners.remove(listener);

        return Collections.unmodifiableList(listeners);
    }

    /**
     * Returns the total count of keys in the map
     *
     * @return The total count of keys in the map
     */
    public int keyCount() {
        return this.delegate.size();
    }

    /**
     * Returns the total count of listeners in the map
     *
     * @return The total count of listeners in the map
     */
    public int listenerCount() {
        int count = 0;

        for (final var listeners : this.delegate.values()) {
            count += listeners.size();
        }

        return count;
    }

    /**
     * Returns whether the map is empty
     *
     * @return Whether the map is empty
     */
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    /**
     * Returns whether the map contains the given key
     *
     * @param key The key
     * @return Whether the map contains the given key
     */
    @Contract("null -> false")
    public boolean containsKey(final @Nullable K key) {
        return this.delegate.containsKey(key);
    }

    /**
     * Returns whether the map contains the given listener
     *
     * @param listener The listener
     * @return Whether the map contains the given listener
     */
    @Contract("null -> false")
    public boolean containsListener(final @Nullable L listener) {
        if (
                listener == null
                || this.keyCount() == 0
        ) {
            return false;
        }

        final var listeners = this.delegate.get(listener.getKey());

        return listeners != null
                && listeners.contains(listener);
    }

    /**
     * Returns an unmodifiable view of the map
     *
     * @return An unmodifiable view of the map
     */
    @Contract(" -> new")
    public @NotNull ListenerMap<K, L> toUnmodifiableView() {
        return new View<>(this);
    }

    /**
     * Removes all keys and listeners from the map
     */
    public void clear() {
        this.delegate.clear();
    }

    /**
     * Iterates over all keys and listeners in the map
     *
     * @param action The action to perform
     */
    public void forEach(final @NotNull BiConsumer<? super K, ? super L> action) {
        this.delegate.forEach(
                (key, listeners) ->
                        listeners.forEach(
                                listener -> action.accept(key, listener)
                        )
        );
    }

    /**
     * Replaces the given old listener with the new listener
     *
     * @param oldListener The old listener
     * @param newListener The new listener
     * @return Whether the replacement was successful
     * @throws IllegalArgumentException If the keys of the listeners do not
     *                                  match
     */
    public boolean replace(
            final @NotNull L oldListener,
            final @NotNull L newListener
    ) throws IllegalArgumentException {
        this.validateKey(oldListener.getKey(), newListener.getKey());

        final var listeners = this.delegate.get(oldListener.getKey());

        if (listeners == null) {
            return false;
        }

        final int index = listeners.indexOf(oldListener);

        if (index == -1) {
            return false;
        }

        listeners.set(index, newListener);

        return true;
    }

    /**
     * Replaces all listeners in the map with the result of the given function
     *
     * @param function The function to apply
     */
    public void replaceAll(final @NotNull BiFunction<? super K, ? super L, ? extends L> function) {
        this.delegate.replaceAll(
                (key, listeners) -> {
                    final var result = new ObjectArrayList<L>();

                    for (final var listener : listeners) {
                        result.add(function.apply(key, listener));
                    }

                    return result;
                }
        );
    }

    private void validateKey(
            final @NotNull K first,
            final @NotNull K second
    ) throws IllegalArgumentException {
        if (!first.equals(second)) {
            throw new IllegalArgumentException("Listener keys must match, got " + first + " and " + second);
        }
    }

    private static class View<K, L extends Listener<K>> extends ListenerMap<K, L> {

        private final ListenerMap<K, L> delegate;

        private View(final @NotNull ListenerMap<K, L> delegate) {
            this.delegate = delegate;
        }

        @Override
        public @NotNull @UnmodifiableView Set<K> keySet() {
            return this.delegate.keySet();
        }

        @Override
        public @NotNull @Unmodifiable Collection<L> listeners() {
            return this.delegate.listeners();
        }

        @Override
        public @NotNull @UnmodifiableView Set<Map.Entry<K, List<L>>> entrySet() {
            return this.delegate.entrySet();
        }

        @Override
        public @Nullable @UnmodifiableView List<L> get(final @Nullable K key) {
            return this.delegate.get(key);
        }

        @Override
        public @UnknownNullability @UnmodifiableView List<L> getOrDefault(
                final @Nullable K key,
                final @Nullable List<L> defaultList
        ) {
            return this.delegate.getOrDefault(key, defaultList);
        }

        @Contract("_ -> fail")
        @Override
        public @Nullable @UnmodifiableView List<L> put(final @NotNull L listener) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }

        @Contract("_ -> fail")
        @Override
        public void putAll(final @NotNull List<L> listeners) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }

        @Contract("_ -> fail")
        @Override
        public void putAll(final @NotNull ListenerMap<K, L> map) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }

        @Contract("_ -> fail")
        @Override
        public @Nullable @Unmodifiable List<L> remove(final @Nullable K key) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }

        @Contract("_ -> fail")
        @Override
        public @Nullable @UnmodifiableView List<L> remove(final @NotNull L listener) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }

        @Override
        public int keyCount() {
            return this.delegate.keyCount();
        }

        @Override
        public int listenerCount() {
            return this.delegate.listenerCount();
        }

        @Override
        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        @Override
        public boolean containsKey(final @Nullable K key) {
            return this.delegate.containsKey(key);
        }

        @Override
        public boolean containsListener(final @Nullable L listener) {
            return this.delegate.containsListener(listener);
        }

        @Contract(" -> fail")
        @Override
        public void clear() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }

        @Override
        public void forEach(final @NotNull BiConsumer<? super K, ? super L> action) {
            this.delegate.forEach(action);
        }

        @Contract("_, _ -> fail")
        @Override
        public boolean replace(
                final @NotNull L oldListener,
                final @NotNull L newListener
        ) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }

        @Contract("_ -> fail")
        @Override
        public void replaceAll(final @NotNull BiFunction<? super K, ? super L, ? extends L> function) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("View does not support modification");
        }
    }
}
