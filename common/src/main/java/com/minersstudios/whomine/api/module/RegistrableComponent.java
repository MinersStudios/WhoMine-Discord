package com.minersstudios.whomine.api.module;

import com.minersstudios.whomine.api.registrable.Registrable;

public interface RegistrableComponent<M extends Module, K>
        extends ModuleComponent<M>, Registrable<K> {}
