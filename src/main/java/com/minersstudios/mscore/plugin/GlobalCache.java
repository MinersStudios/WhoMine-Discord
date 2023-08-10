package com.minersstudios.mscore.plugin;

import com.minersstudios.mscore.collections.DualMap;
import com.minersstudios.mscore.collections.HashDualMap;
import com.minersstudios.mscore.packet.collection.PacketListenersMap;
import com.minersstudios.msdecor.customdecor.CustomDecorData;
import org.bukkit.inventory.Recipe;

import java.util.*;

/**
 * Cache for all custom data.
 * Use {@link MSPlugin#getGlobalCache()} to get cache instance.
 */
public final class GlobalCache {
    public final PacketListenersMap packetListenerMap = new PacketListenersMap();
    public final Set<String> onlyPlayerCommandSet = new HashSet<>();
    public final DualMap<String, Integer, CustomDecorData> customDecorMap = new HashDualMap<>();
    public final List<Recipe> customDecorRecipes = new ArrayList<>();
    public final List<Recipe> customItemRecipes = new ArrayList<>();
    public final List<Recipe> customBlockRecipes = new ArrayList<>();
}
