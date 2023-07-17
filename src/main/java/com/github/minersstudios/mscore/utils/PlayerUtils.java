package com.github.minersstudios.mscore.utils;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.minersstudios.msessentials.player.PlayerInfo;
import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerShowEntityEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PlayerUtils {
    public static final @NotNull String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/";

    private static final Map<String, UUID> UUID_CACHE = new HashMap<>();

    @Contract(value = " -> fail")
    private PlayerUtils() {
        throw new AssertionError("Utility class");
    }

    /**
     * Sets player to a seated position underneath him
     *
     * @param player player
     */
    public static void setSitting(@NotNull Player player) {
        setSitting(player, player.getLocation(), null);
    }

    /**
     * Sets player to a seated position in specified location
     *
     * @param player   Player
     * @param location Location where the player will sit
     */
    public static void setSitting(
            @NotNull Player player,
            @NotNull Location location
    ) {
        setSitting(player, location, null);
    }

    /**
     * Sets player to a seated position in specified location with message
     *
     * @param player   Player
     * @param location Location where the player will sit
     * @param message  Message
     */
    public static void setSitting(
            @NotNull Player player,
            @NotNull Location location,
            @Nullable Component message
    ) {
        PlayerInfo.fromOnlinePlayer(player).setSitting(location, message);
    }

    /**
     * Unsets the sitting position of the player
     *
     * @param player  Player who is currently sitting
     */
    public static void unsetSitting(@NotNull Player player) {
        unsetSitting(player, null);
    }

    /**
     * Unsets the sitting position of the player with message
     *
     * @param player  Player who is currently sitting
     * @param message Message
     */
    public static void unsetSitting(
            @NotNull Player player,
            @Nullable Component message
    ) {
        PlayerInfo.fromOnlinePlayer(player).unsetSitting(message);
    }

    /**
     * @param offlinePlayer Offline player whose data will be loaded
     * @return Online player from offline player
     */
    public static @Nullable Player loadPlayer(@NotNull OfflinePlayer offlinePlayer) {
        if (!offlinePlayer.hasPlayedBefore()) return null;

        GameProfile profile = new GameProfile(
                offlinePlayer.getUniqueId(),
                offlinePlayer.getName() != null
                ? offlinePlayer.getName()
                : offlinePlayer.getUniqueId().toString()
        );
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel worldServer = server.getLevel(Level.OVERWORLD);

        if (worldServer == null) return null;

        Player online = new ServerPlayer(server, worldServer, profile).getBukkitEntity();

        online.loadData();
        return online;
    }

    /**
     * Sets player's skin with specified value and signature.
     * Also updates the skin for all players on the server.
     * If wanted to reset the skin, set both value and signature
     * to null.
     *
     * @param player    Player whose skin will be set
     * @param value     Value of the skin
     * @param signature Signature of the skin
     */
    public static void setSkin(
            @NotNull Player player,
            @Nullable String value,
            @Nullable String signature
    ) {
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        Entity vehicle = player.getVehicle();
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        GameProfile gameProfile = serverPlayer.getGameProfile();
        PropertyMap propertyMap = gameProfile.getProperties();

        if (vehicle != null) {
            vehicle.eject();
        }

        if (propertyMap.containsKey("textures")) {
            Property oldProperty = propertyMap.get("textures").iterator().next();
            propertyMap.remove("textures", oldProperty);
        }

        if (
                !StringUtils.isBlank(value)
                && !StringUtils.isBlank(signature)
        ) {
            propertyMap.put("textures", new Property("textures", value, signature));
        }

        if (!serverPlayer.sentListPacket) {
            serverPlayer.gameProfile = gameProfile;
            return;
        }

        Location location = player.getLocation();
        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        ServerLevel serverLevel = serverPlayer.serverLevel();
        ServerPlayerGameMode gameMode = serverPlayer.gameMode;
        var players = minecraftServer.getPlayerList().players;

        ClientboundRespawnPacket respawnPacket = new ClientboundRespawnPacket(
                serverLevel.dimensionTypeId(),
                serverLevel.dimension(),
                BiomeManager.obfuscateSeed(serverLevel.getSeed()),
                gameMode.getGameModeForPlayer(),
                gameMode.getPreviousGameModeForPlayer(),
                serverLevel.isDebug(),
                serverLevel.isFlat(),
                ClientboundRespawnPacket.KEEP_ALL_DATA,
                serverPlayer.getLastDeathLocation(),
                serverPlayer.getPortalCooldown()
        );
        ClientboundSetExperiencePacket experiencePacket = new ClientboundSetExperiencePacket(
                serverPlayer.experienceProgress,
                serverPlayer.totalExperience,
                serverPlayer.experienceLevel
        );

        players.stream()
        .filter(forWho -> forWho.getBukkitEntity().canSee(player))
        .forEach(forWho -> unregisterEntity(forWho, serverPlayer));

        serverPlayer.gameProfile = gameProfile;

        players.stream()
        .filter(forWho -> forWho.getBukkitEntity().canSee(player))
        .forEach(forWho -> trackAndShowEntity(forWho, serverPlayer));

        connection.send(respawnPacket);
        serverPlayer.onUpdateAbilities();
        connection.teleport(location);
        minecraftServer.getPlayerList().sendAllPlayerInfo(serverPlayer);
        connection.send(experiencePacket);

        for (var mobEffect : serverPlayer.getActiveEffects()) {
            connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), mobEffect));
        }

        if (player.isOp()) {
            player.setOp(false);
            player.setOp(true);
        }
    }

    /**
     * Gets UUID from player nickname
     *
     * @param nickname Player nickname
     * @return Player UUID
     */
    public static @Nullable UUID getUUID(@NotNull String nickname) {
        UUID uuid = UUID_CACHE.get(nickname);

        if (uuid != null) return uuid;

        if (Bukkit.getOnlineMode()) {
            try {
                URL url = new URL(UUID_URL + nickname);
                String jsonString = IOUtils.toString(url, Charset.defaultCharset());

                if (jsonString.isEmpty()) return null;

                JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(jsonString);
                String uuidString = jsonObject.get("id").toString();
                uuid = UUID.fromString(
                        uuidString.replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                                "$1-$2-$3-$4-$5"
                        )
                );
            } catch (Exception ignored) {
                return null;
            }
        } else {
            byte[] bytes = ("OfflinePlayer:" + nickname).getBytes(Charsets.UTF_8);
            uuid = UUID.nameUUIDFromBytes(bytes);
        }

        UUID_CACHE.put(nickname, uuid);
        return uuid;
    }

    /**
     * Gets offline player by nickname
     *
     * @param nickname Player nickname
     * @return Offline player
     */
    public static @Nullable OfflinePlayer getOfflinePlayerByNick(@NotNull String nickname) {
        UUID uuid = getUUID(nickname);
        return uuid != null
                ? getOfflinePlayer(uuid, nickname)
                : null;
    }

    /**
     * Gets offline player by uuid and nickname
     *
     * @param uuid Player unique id
     * @param name Player nickname
     * @return Offline player
     */
    public static @NotNull OfflinePlayer getOfflinePlayer(
            @NotNull UUID uuid,
            @NotNull String name
    ) {
        Server server = Bukkit.getServer();
        OfflinePlayer offlinePlayer = server.getOfflinePlayer(uuid);

        if (offlinePlayer.getName() == null) {
            CraftServer craftServer = (CraftServer) server;
            GameProfile gameProfile = new GameProfile(uuid, name);

            return craftServer.getOfflinePlayer(gameProfile);
        }

        return offlinePlayer;
    }

    /**
     * Gets player profile by uuid and nickname
     *
     * @param uuid     Player unique id
     * @param nickname Player nickname
     * @return Player profile
     * @throws IllegalArgumentException If uuid and nickname are both null or empty
     */
    @Contract("_, _ -> new")
    public static @NotNull PlayerProfile craftProfile(
            @NotNull UUID uuid,
            @NotNull String nickname
    ) throws IllegalArgumentException {
        return new CraftPlayerProfile(uuid, nickname);
    }

    private static void unregisterEntity(
            @NotNull ServerPlayer forWho,
            @NotNull ServerPlayer serverPlayer
    ) {
        ChunkMap tracker = forWho.serverLevel().getChunkSource().chunkMap;
        ChunkMap.TrackedEntity entry = tracker.entityMap.get(serverPlayer.getId());
        ClientboundPlayerInfoRemovePacket packet = new ClientboundPlayerInfoRemovePacket(List.of(serverPlayer.getUUID()));

        if (entry != null) {
            entry.removePlayer(forWho);
        }

        if (serverPlayer.sentListPacket) {
            forWho.connection.send(packet);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void trackAndShowEntity(
            @NotNull ServerPlayer forWho,
            @NotNull ServerPlayer serverPlayer
    ) {
        PluginManager pluginManager = serverPlayer.getBukkitEntity().getServer().getPluginManager();
        ChunkMap tracker = forWho.serverLevel().getChunkSource().chunkMap;
        ClientboundPlayerInfoUpdatePacket packet = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(serverPlayer));
        ChunkMap.TrackedEntity entry = tracker.entityMap.get(serverPlayer.getId());
        Event event = new PlayerShowEntityEvent(forWho.getBukkitEntity(), serverPlayer.getBukkitEntity());

        forWho.connection.send(packet);

        if (entry != null && !entry.seenBy.contains(forWho.connection)) {
            entry.updatePlayer(forWho);
        }

        pluginManager.callEvent(event);
    }
}
