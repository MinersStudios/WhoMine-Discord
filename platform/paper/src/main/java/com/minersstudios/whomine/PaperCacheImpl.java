package com.minersstudios.whomine;

import com.google.gson.JsonElement;
import com.minersstudios.wholib.module.AbstractModuleComponent;
import com.minersstudios.wholib.module.MainModule;
import com.minersstudios.wholib.paper.PaperCache;
import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.chat.ChatBuffer;
import com.minersstudios.wholib.paper.collection.DiggingMap;
import com.minersstudios.wholib.paper.collection.StepMap;
import com.minersstudios.wholib.paper.custom.anomaly.Anomaly;
import com.minersstudios.wholib.paper.custom.anomaly.AnomalyAction;
import com.minersstudios.wholib.paper.custom.block.CustomBlockData;
import com.minersstudios.wholib.paper.custom.item.renameable.RenameableItem;
import com.minersstudios.wholib.paper.discord.BotHandler;
import com.minersstudios.wholib.paper.discord.DiscordMap;
import com.minersstudios.wholib.paper.player.PlayerInfo;
import com.minersstudios.wholib.paper.player.collection.IDMap;
import com.minersstudios.wholib.paper.player.collection.MuteMap;
import com.minersstudios.wholib.paper.player.collection.PlayerInfoMap;
import com.minersstudios.wholib.status.StatusHandler;
import com.minersstudios.wholib.paper.world.WorldDark;
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

public final class PaperCacheImpl extends AbstractModuleComponent<WhoMine> implements PaperCache {

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

    PaperCacheImpl(final @NotNull WhoMine module) {
        super(module);
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

    public void setConsolePlayerInfo(final @NotNull PlayerInfo playerInfo) {
        this.consolePlayerInfo = playerInfo;
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
        final StatusHandler statusHandler = this.getModule().getStatusHandler();

        statusHandler.assignStatus(MainModule.LOADING_CACHE);

        this.customDecorRecipes = new ObjectArrayList<>();
        this.customItemRecipes = new ObjectArrayList<>();
        this.customBlockRecipes = new ObjectArrayList<>();
        this.stepMap = new StepMap();
        this.diggingMap = new DiggingMap();
        this.dosimeterPlayers = new ConcurrentHashMap<>();
        this.renameableMenuItems = new ObjectArrayList<>();
        this.blockDataRecipes = new ObjectArrayList<>();
        this.playerInfoMap = new PlayerInfoMap(this.getModule());
        this.muteMap = new MuteMap(this.getModule());
        this.discordMap = new DiscordMap(this.getModule());
        this.idMap = new IDMap(this.getModule());
        this.seats = new ConcurrentHashMap<>();
        this.anomalies = new ConcurrentHashMap<>();
        this.playerAnomalyActionMap = new ConcurrentHashMap<>();
        this.chatBuffer = new ChatBuffer(this.getModule());
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

    @Override
    public @UnknownNullability List<Recipe> getCustomDecorRecipes() {
        return this.customDecorRecipes;
    }

    @Override
    public @UnknownNullability List<Recipe> getCustomItemRecipes() {
        return this.customItemRecipes;
    }

    @Override
    public @UnknownNullability List<Recipe> getCustomBlockRecipes() {
        return this.customBlockRecipes;
    }
}
