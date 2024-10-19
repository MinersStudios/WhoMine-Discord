package com.minersstudios.wholib.paper.inventory.holder;

import java.lang.annotation.*;

/**
 * All event listeners annotated using {@link InventoryHolder} will be registered
 * automatically. Also, it must be extended by {@link AbstractInventoryHolder}.
 *
 * @see AbstractInventoryHolder
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InventoryHolder {}
