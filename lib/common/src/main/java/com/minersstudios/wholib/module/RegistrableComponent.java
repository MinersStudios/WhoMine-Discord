package com.minersstudios.wholib.module;

import com.minersstudios.wholib.registrable.Registrable;

public interface RegistrableComponent<M extends Module, K>
        extends ModuleComponent<M>, Registrable<K> {}
