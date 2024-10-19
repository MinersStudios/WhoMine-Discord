package com.minersstudios.wholib.paper;

import com.google.gson.JsonElement;
import com.minersstudios.wholib.module.components.Cache;
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
import com.minersstudios.wholib.paper.world.WorldDark;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Map;

public interface PaperCache extends Cache<WhoMine> {

    void load() throws IllegalStateException;

    void unload();

    @UnknownNullability List<Recipe> getCustomDecorRecipes();

    @UnknownNullability List<Recipe> getCustomItemRecipes();

    @UnknownNullability List<Recipe> getCustomBlockRecipes();

    @UnknownNullability StepMap getStepMap();

    @UnknownNullability DiggingMap getDiggingMap();

    @UnknownNullability Map<Player, EquipmentSlot> getDosimeterPlayers();

    @UnknownNullability List<RenameableItem> getRenameableMenuItems();

    @ApiStatus.Internal
    @UnknownNullability List<Map.Entry<CustomBlockData, JsonElement>> getBlockDataRecipes();

    @UnknownNullability PlayerInfoMap getPlayerInfoMap();

    @UnknownNullability MuteMap getMuteMap();

    @UnknownNullability DiscordMap getDiscordMap();

    @UnknownNullability IDMap getIdMap();

    @UnknownNullability Map<Player, ArmorStand> getSeats();

    @UnknownNullability Map<NamespacedKey, Anomaly> getAnomalies();

    @UnknownNullability Map<Player, Map<AnomalyAction, Long>> getPlayerAnomalyActionMap();

    @UnknownNullability ChatBuffer getChatBuffer();

    @UnknownNullability List<BukkitTask> getBukkitTasks();

    @UnknownNullability Long2ObjectMap<BotHandler> getBotHandlers();

    @UnknownNullability PlayerInfo getConsolePlayerInfo();

    void setConsolePlayerInfo(PlayerInfo playerInfo);

    @UnknownNullability WorldDark getWorldDark();

    boolean isLoaded();
}
