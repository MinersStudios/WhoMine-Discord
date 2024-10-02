package com.minersstudios.whomine.api.module;

import com.minersstudios.whomine.api.status.StatusHandler;
import org.jetbrains.annotations.NotNull;

public interface Module {

    /**
     * Returns the status handler of the module
     *
     * @return The status handler of the module
     */
    @NotNull StatusHandler getStatusHandler();
}
