package com.minersstudios.whomine.api.listener;

import com.minersstudios.whomine.api.registrable.Registrable;

/**
 * Represents a listener that can be registered with
 * {@link ListenerManager listener managers}.
 * <p>
 * The key of the listener is used to its identification in the listener manager
 * and must be unique.
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
 * @param <K> The key type of the listener
 *
 * @see ListenerManager
 */
public interface Listener<K> extends Registrable<K> {}
