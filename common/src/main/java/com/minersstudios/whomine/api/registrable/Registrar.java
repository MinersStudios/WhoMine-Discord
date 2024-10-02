package com.minersstudios.whomine.api.registrable;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a registrar that can store and manage
 * {@link Registrable registrable} objects
 *
 * @param <R> The registrable object type
 *
 * @see Registrable
 */
public interface Registrar<R extends Registrable<?>> {

    /**
     * Registers the registrable object.
     * <p>
     * <b>IMPORTANT:</b> This method MUST handle the
     * {@link Registrable#onRegister()} method call of the registrable object.
     *
     * @param registrable The registrable object
     * @return True if the object has not yet been registered and its
     *         registration was successful
     */
    boolean register(final @NotNull R registrable);

    /**
     * Unregisters the registrable object.
     * <p>
     * <b>IMPORTANT:</b> This method MUST handle the
     * {@link Registrable#onUnregister()} method call of the registrable object.
     *
     * @param registrable The registrable object
     * @return True if the object has been registered and its deregistration
     *         was successful
     */
    boolean unregister(final @NotNull R registrable);

    /**
     * Unregisters all registrable objects.
     * <p>
     * <b>IMPORTANT:</b> This method MUST handle the
     * {@link Registrable#onUnregister()} method call of the registrable objects.
     */
    void unregisterAll();

    /**
     * Checks if the registrable object is registered
     *
     * @param registrable The registrable object
     * @return True if the object is registered
     */
    boolean isRegistered(final @NotNull R registrable);
}
