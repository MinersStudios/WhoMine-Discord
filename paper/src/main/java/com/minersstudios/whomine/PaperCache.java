package com.minersstudios.whomine;

import com.google.gson.JsonElement;
import com.minersstudios.whomine.api.module.components.Cache;
import com.minersstudios.whomine.api.module.MainModule;
import com.minersstudios.whomine.chat.ChatBuffer;
import com.minersstudios.whomine.collection.DiggingMap;
import com.minersstudios.whomine.collection.StepMap;
import com.minersstudios.whomine.custom.anomaly.Anomaly;
import com.minersstudios.whomine.custom.anomaly.AnomalyAction;
import com.minersstudios.whomine.custom.block.CustomBlockData;
import com.minersstudios.whomine.custom.item.renameable.RenameableItem;
import com.minersstudios.whomine.discord.BotHandler;
import com.minersstudios.whomine.discord.DiscordMap;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.collection.IDMap;
import com.minersstudios.whomine.player.collection.MuteMap;
import com.minersstudios.whomine.player.collection.PlayerInfoMap;
import com.minersstudios.whomine.api.status.StatusHandler;
import com.minersstudios.whomine.world.WorldDark;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PaperCache implements Cache<WhoMine> {

    private final WhoMine module;
    private boolean isLoaded;

    public List<Recipe> customDecorRecipes;
    public List<Recipe> customItemRecipes;
    public List<Recipe> customBlockRecipes;
    private StepMap stepMap;
    private DiggingMap diggingMap;
    private Map<Player, EquipmentSlot> dosimeterPlayers;
    private List<RenameableItem> renameableMenuItems;
    private List<Map.Entry<CustomBlockData, JsonElement>> blockDataRecipes;
    private PlayerInfoMap playerInfoMap;
    private MuteMap muteMap;
    private DiscordMap discordMap;
    private IDMap idMap;
    private Map<Player, ArmorStand> seats;
    private Map<NamespacedKey, Anomaly> anomalies;
    private Map<Player, Map<AnomalyAction, Long>> playerAnomalyActionMap;
    private ChatBuffer chatBuffer;
    private List<BukkitTask> bukkitTasks;
    private Long2ObjectMap<BotHandler> botHandlers;
    PlayerInfo consolePlayerInfo;
    WorldDark worldDark;

    PaperCache(final @NotNull WhoMine module) {
        this.module = module;
    }

    @Override
    public @NotNull WhoMine getModule() {
        return this.module;
    }

    public @UnknownNullability StepMap getStepMap() {
        return this.stepMap;
    }

    public @UnknownNullability DiggingMap getDiggingMap() {
        return this.diggingMap;
    }

    public @UnknownNullability Map<Player, EquipmentSlot> getDosimeterPlayers() {
        return this.dosimeterPlayers;
    }

    public @UnknownNullability List<RenameableItem> getRenameableMenuItems() {
        return this.renameableMenuItems;
    }

    @ApiStatus.Internal
    public @UnknownNullability List<Map.Entry<CustomBlockData, JsonElement>> getBlockDataRecipes() {
        return this.blockDataRecipes;
    }

    public @UnknownNullability PlayerInfoMap getPlayerInfoMap() {
        return this.playerInfoMap;
    }

    public @UnknownNullability MuteMap getMuteMap() {
        return this.muteMap;
    }

    public @UnknownNullability DiscordMap getDiscordMap() {
        return this.discordMap;
    }

    public @UnknownNullability IDMap getIdMap() {
        return this.idMap;
    }

    public @UnknownNullability Map<Player, ArmorStand> getSeats() {
        return this.seats;
    }

    public @UnknownNullability Map<NamespacedKey, Anomaly> getAnomalies() {
        return this.anomalies;
    }

    public @UnknownNullability Map<Player, Map<AnomalyAction, Long>> getPlayerAnomalyActionMap() {
        return this.playerAnomalyActionMap;
    }

    public @UnknownNullability ChatBuffer getChatBuffer() {
        return this.chatBuffer;
    }

    public @UnknownNullability List<BukkitTask> getBukkitTasks() {
        return this.bukkitTasks;
    }

    public @UnknownNullability Long2ObjectMap<BotHandler> getBotHandlers() {
        return this.botHandlers;
    }

    public @UnknownNullability PlayerInfo getConsolePlayerInfo() {
        return this.consolePlayerInfo;
    }

    public @UnknownNullability WorldDark getWorldDark() {
        return this.worldDark;
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    @Override
    public @NotNull String toString() {
        return "Cache{isLoaded=" + this.isLoaded + "}";
    }

    public void load() throws IllegalStateException {
        if (this.isLoaded()) {
            throw new IllegalStateException("Cache is already loaded");
        }

        this.isLoaded = true;
        final StatusHandler statusHandler = this.module.getStatusHandler();

        statusHandler.assignStatus(MainModule.LOADING_CACHE);

        this.customDecorRecipes = new ObjectArrayList<>();
        this.customItemRecipes = new ObjectArrayList<>();
        this.customBlockRecipes = new ObjectArrayList<>();
        this.stepMap = new StepMap();
        this.diggingMap = new DiggingMap();
        this.dosimeterPlayers = new ConcurrentHashMap<>();
        this.renameableMenuItems = new ObjectArrayList<>();
        this.blockDataRecipes = new ObjectArrayList<>();
        this.playerInfoMap = new PlayerInfoMap(this.module);
        this.muteMap = new MuteMap(this.module);
        this.discordMap = new DiscordMap(this.module);
        this.idMap = new IDMap(this.module);
        this.seats = new ConcurrentHashMap<>();
        this.anomalies = new ConcurrentHashMap<>();
        this.playerAnomalyActionMap = new ConcurrentHashMap<>();
        this.chatBuffer = new ChatBuffer(this.module);
        this.bukkitTasks = new ObjectArrayList<>();
        this.botHandlers = new Long2ObjectOpenHashMap<>();

        statusHandler.assignStatus(
                this.isLoaded()
                ? MainModule.LOADED_CACHE
                : MainModule.FAILED_LOAD_CACHE
        );
    }

    public void unload() {
        if (!this.isLoaded()) {
            return;
        }

        this.isLoaded = false;

        for (final var task : this.bukkitTasks) {
            task.cancel();
        }

        this.customDecorRecipes = null;
        this.customItemRecipes = null;
        this.customBlockRecipes = null;
        this.stepMap = null;
        this.diggingMap = null;
        this.dosimeterPlayers = null;
        this.renameableMenuItems = null;
        this.blockDataRecipes = null;
        this.playerInfoMap = null;
        this.muteMap = null;
        this.discordMap = null;
        this.idMap = null;
        this.seats = null;
        this.anomalies = null;
        this.playerAnomalyActionMap = null;
        this.chatBuffer = null;
        this.bukkitTasks = null;
        this.botHandlers = null;
    }
}
