package com.minersstudios.wholib.registrable;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that can be registered with {@link Registrar registrars}.
 * <p>
 * The key of the registrable object is used to identify the object in the
 * registrar and must be unique.
 * <table>
 *     <caption>Available optional overridable methods</caption>
 *     <tr>
 *         <th>Method</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #onRegister()}</td>
 *         <td>
 *             Called when the object is registered by a registrar in the
 *             {@link Registrar#register(Registrable)} method
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link #onUnregister()}</td>
 *         <td>
 *             Called when the object is unregistered by a registrar in the
 *             {@link Registrar#unregister(Registrable)} method
 *         </td>
 *     </tr>
 * </table>
 *
 * @param <K> The key type of the registrable object
 *
 * @see Registrar
 */
public interface Registrable<K> {

    /**
     * Returns the key of the registrable object
     *
     * @return The key of the registrable object
     */
    @NotNull K getKey();

    /**
     * Called when the object is registered
     */
    default void onRegister() {
        // Do nothing by default
    }

    /**
     * Called when the object is unregistered
     */
    default void onUnregister() {
        // Do nothing by default
    }
}
