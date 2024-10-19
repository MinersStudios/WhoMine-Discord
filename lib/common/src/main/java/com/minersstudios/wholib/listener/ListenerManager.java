package com.minersstudios.wholib.listener;

import com.minersstudios.wholib.module.AbstractModuleComponent;
import com.minersstudios.wholib.module.Module;
import com.minersstudios.wholib.module.ModuleComponent;
import com.minersstudios.wholib.registrable.Registrar;
import org.jetbrains.annotations.NotNull;

/**
 * The listener manager that can store and manage {@link Listener listeners}
 *
 * @param <M> The module, that the listener manager belongs to
 *
 * @see Listener
 */
public abstract class ListenerManager<M extends Module>
        extends AbstractModuleComponent<M>
        implements ModuleComponent<M>, Registrar<Listener<?>> {

    /**
     * Constructs a new listener manager
     *
     * @param module The module, that the listener manager belongs to
     */
    public ListenerManager(final @NotNull M module) {
        super(module);
    }
}
