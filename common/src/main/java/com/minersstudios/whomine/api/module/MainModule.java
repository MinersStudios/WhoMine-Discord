package com.minersstudios.whomine.api.module;

import com.minersstudios.whomine.api.module.components.Cache;
import com.minersstudios.whomine.api.module.components.Configuration;
import com.minersstudios.whomine.api.status.FailureStatus;
import com.minersstudios.whomine.api.status.SuccessStatus;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.api.status.Status.*;

/**
 * Represents a WhoMine module
 */
public interface MainModule<M extends MainModule<M>> extends Module {
    //<editor-fold desc="Module statuses" defaultstate="collapsed">
    /** High-priority module loading status */
    SuccessStatus LOADING = successHigh("LOADING");
    /** Low-priority module loaded status */
    SuccessStatus LOADED = successLow("LOADED");

    /** High-priority module enabling status */
    SuccessStatus ENABLING = successHigh("ENABLING");
    /** High-priority module enabled status */
    SuccessStatus ENABLED = successHigh("ENABLED");

    /** High-priority module disabling status */
    SuccessStatus DISABLING = successHigh("DISABLING");
    /** High-priority module disabled status */
    SuccessStatus DISABLED = successHigh("DISABLED");

    /** Failed low-priority status for loading cache */
    FailureStatus FAILED_LOAD_CACHE = failureLow("FAILED_LOAD_CACHE");
    /** Low-priority module loading cache status */
    SuccessStatus LOADING_CACHE = successLow("LOADING_CACHE");
    /** Low-priority module loaded cache status */
    SuccessStatus LOADED_CACHE = successLow("LOADED_CACHE", FAILED_LOAD_CACHE);

    /** Failed low-priority status for loading config */
    FailureStatus FAILED_LOAD_CONFIG = failureLow("FAILED_LOAD_CONFIG");
    /** Low-priority module loading config status */
    SuccessStatus LOADING_CONFIG = successLow("LOADING_CONFIG");
    /** Low-priority module loaded config status */
    SuccessStatus LOADED_CONFIG = successLow("LOADED_CONFIG", FAILED_LOAD_CONFIG);

    /** Low-priority module loading languages.yml status */
    SuccessStatus LOADING_LANGUAGES = successLow("LOADING_LANGUAGES");
    /** Low-priority module loaded languages.yml status */
    SuccessStatus LOADED_LANGUAGES = successLow("LOADED_LANGUAGES");

    /** Low-priority module decorations loading status */
    SuccessStatus LOADING_DECORATIONS = successLow("LOADING_DECORATIONS");
    /** Low-priority module decorations loaded status */
    SuccessStatus LOADED_DECORATIONS = successLow("LOADED_DECORATIONS");

    /** Failed low-priority status for loading blocks */
    FailureStatus FAILED_LOAD_BLOCKS = failureLow("FAILED_LOAD_BLOCKS");
    /** Low-priority module loading blocks status */
    SuccessStatus LOADING_BLOCKS = successLow("LOADING_BLOCKS");
    /** Low-priority module loaded blocks status */
    SuccessStatus LOADED_BLOCKS = successLow("LOADED_BLOCKS", FAILED_LOAD_BLOCKS);

    /** Failed low-priority status for loading items */
    SuccessStatus LOADING_ITEMS = successLow("LOADING_ITEMS");
    /** Low-priority module loaded items status */
    SuccessStatus LOADED_ITEMS = successLow("LOADED_ITEMS");

    /** Failed low-priority status for loading decor */
    FailureStatus FAILED_LOAD_RENAMEABLES = failureLow("FAILED_LOAD_RENAMEABLES");
    /** Low-priority module loading decor status */
    SuccessStatus LOADING_RENAMEABLES = successLow("LOADING_RENAMEABLES");
    /** Low-priority module loaded decor status */
    SuccessStatus LOADED_RENAMEABLES = successLow("LOADED_RENAMEABLES", FAILED_LOAD_RENAMEABLES);

    /** Failed low-priority status for loading resource packs */
    FailureStatus FAILED_LOAD_RESOURCE_PACKS = failureLow("FAILED_LOAD_RESOURCE_PACKS");
    /** Low-priority module loading resource packs status */
    SuccessStatus LOADING_RESOURCE_PACKS = successLow("LOADING_RESOURCE_PACKS");
    /** Low-priority module loaded resource packs status */
    SuccessStatus LOADED_RESOURCE_PACKS = successLow("LOADED_RESOURCE_PACKS", FAILED_LOAD_RESOURCE_PACKS);

    /** Failed low-priority status for loading anomalies */
    FailureStatus FAILED_LOAD_ANOMALIES = failureLow("FAILED_LOAD_ANOMALIES");
    /** Low-priority module loading anomalies status */
    SuccessStatus LOADING_ANOMALIES = successLow("LOADING_ANOMALIES");
    /** Low-priority module loaded anomalies status */
    SuccessStatus LOADED_ANOMALIES = successLow("LOADED_ANOMALIES", FAILED_LOAD_ANOMALIES);

    /** Failed low-priority status for loading discord */
    FailureStatus FAILED_LOAD_DISCORD = failureLow("FAILED_LOAD_DISCORD");
    /** Low-priority module loading discord status */
    SuccessStatus LOADING_DISCORD = successLow("LOADING_DISCORD");
    /** Low-priority module loaded discord status */
    SuccessStatus LOADED_DISCORD = successLow("LOADED_DISCORD", FAILED_LOAD_DISCORD);
    //</editor-fold>

    /**
     * Returns the cache of the module
     *
     * @return The cache of the module
     */
    @NotNull Cache<M> getCache();

    /**
     * Returns the configuration of the module
     *
     * @return The configuration of the module
     */
    @NotNull Configuration<M> getConfiguration();

    /**
     * Returns whether the module is fully loaded
     *
     * @return True if the module is fully loaded
     */
    boolean isFullyLoaded();
}
