package com.minersstudios.wholib.paper.player.collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.minersstudios.wholib.paper.WhoMine;
import com.mojang.util.InstantTypeAdapter;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mute map with {@link UUID} and its {@link Entry}.
 * All mutes stored in the "config/minersstudios/MSEssentials/muted_players.json" file.
 *
 * @see Entry
 */
public final class MuteMap {
    private final File file;
    private final Map<UUID, Entry> map;
    private final Logger logger;

    private static final Gson GSON =
            new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * Mute map with {@link UUID} and its {@link Entry}. Loads mutes from the
     * file.
     */
    public MuteMap(final @NotNull WhoMine plugin) {
        this.file = new File(plugin.getDataFolder(), "muted_players.json");
        this.map = new ConcurrentHashMap<>();
        this.logger = plugin.getLogger();
        this.reloadMutes();
    }

    /**
     * Gets mute entry of the player from the map
     *
     * @param player Probably muted player
     * @return Creation and expiration date, reason and source of mute
     */
    public @Nullable MuteMap.Entry getMuteEntry(final @NotNull OfflinePlayer player) {
        return this.map.get(player.getUniqueId());
    }

    /**
     * @param player Probably muted player
     * @return True if the player is muted
     */
    @Contract("null -> false")
    public boolean isMuted(final @Nullable OfflinePlayer player) {
        return player != null && this.map.containsKey(player.getUniqueId());
    }

    /**
     * Adds mute for the player
     *
     * @param player     Player, who will be muted
     * @param expiration Date when the player will be unmuted
     * @param reason     Mute reason
     * @param source     Mute source could be a player's nickname or CONSOLE
     */
    public void put(
            final @NotNull OfflinePlayer player,
            final @NotNull Instant expiration,
            final @NotNull String reason,
            final @NotNull String source
    ) {
        final Instant created = Instant.now();
        final UUID uuid = player.getUniqueId();

        this.map.put(uuid, Entry.create(created, expiration, reason, source));
        this.saveFile();
    }

    /**
     * Removes mute from player
     *
     * @param player Muted player
     */
    public void remove(final @Nullable OfflinePlayer player) {
        if (player == null) {
            return;
        }

        this.map.remove(player.getUniqueId());
        this.saveFile();
    }

    /**
     * @return The number of muted players
     */
    public int size() {
        return this.map.size();
    }

    /**
     * @return True if this map contains no muted players
     */
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    /**
     * @param uuid Player's UUID
     * @return True if this map contains a mute for the specified uuid
     */
    public boolean containsUniqueId(final @Nullable UUID uuid) {
        return uuid != null
                && this.map.containsKey(uuid);
    }

    /**
     * @return An unmodifiable view of the UUIDs contained in this map
     */
    public @NotNull @UnmodifiableView Set<UUID> uuidSet() {
        return Collections.unmodifiableSet(this.map.keySet());
    }

    /**
     * @return An unmodifiable view of the mappings contained in this map
     */
    public @NotNull @UnmodifiableView Set<Map.Entry<UUID, Entry>> entrySet() {
        return Collections.unmodifiableSet(this.map.entrySet());
    }

    /**
     * Reloads "muted_players.json" file
     */
    public void reloadMutes() {
        this.map.clear();

        if (!this.file.exists()) {
            this.createFile();
        } else {
            try {
                final Type mapType = new TypeToken<Map<UUID, Entry>>() {}.getType();
                final String json = Files.readString(this.file.toPath(), StandardCharsets.UTF_8);
                final Map<UUID, Entry> jsonMap = GSON.fromJson(json, mapType);

                if (jsonMap == null) {
                    this.createBackupFile();
                    this.reloadMutes();
                    return;
                }

                jsonMap.forEach((uuid, params) -> {
                    if (params != null && params.isValidate()) {
                        this.map.put(uuid, params);
                    } else {
                        this.logger.severe("Failed to read the player params : " + uuid.toString() + " in \"muted_players.json\"");
                    }
                });
            } catch (final Exception ignored) {
                this.createBackupFile();
                this.reloadMutes();
            }
        }
    }

    /**
     * Creates a new "muted_players.json" file
     */
    private void createFile() {
        try {
            if (
                    this.file.getParentFile().mkdirs()
                    && this.file.createNewFile()
            ) {
                this.saveFile();
            }
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "Failed to create a new \"muted_players.json\" file", e);
        }
    }

    /**
     * Creates a backup file of the "muted_players.json" file
     */
    private void createBackupFile() {
        final File backupFile = new File(this.file.getParent(), this.file.getName() + ".OLD");

        try {
            Files.move(this.file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.saveFile();
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "Failed to create \"muted_players.json.OLD\" backup file", e);
        }

        this.logger.severe("Failed to read the \"muted_players.json\" file, creating a new file");
    }

    /**
     * Saves the mute map to the "muted_players.json" file
     */
    private void saveFile() {
        try (final var writer = new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8)) {
            GSON.toJson(this.map, writer);
        } catch (final IOException e) {
            this.logger.log(Level.SEVERE, "Failed to save muted players", e);
        }
    }

    /**
     * Mute entry, used in {@link MuteMap}
     * <br>
     * Parameters:
     * <ul>
     *     <li>created - date when the player was muted</li>
     *     <li>expiration - date when the player will be unmuted</li>
     *     <li>reason - mute reason</li>
     *     <li>source - mute source, could be a player's nickname or CONSOLE</li>
     * </ul>
     *
     * @see MuteMap
     */
    public static class Entry {
        private final Instant created;
        private final Instant expiration;
        private final String reason;
        private final String source;

        private Entry(
                final Instant created,
                final Instant expiration,
                final String reason,
                final String source
        ) {
            this.created = created;
            this.expiration = expiration;
            this.reason = reason;
            this.source = source;
        }

        /**
         * Creates a new {@link Entry} with the specified parameters
         *
         * @param created    Date when the player was muted
         * @param expiration Date when the player will be unmuted
         * @param reason     Mute reason
         * @param source     Mute source could be a player's nickname or CONSOLE
         * @return New {@link Entry}
         */
        @Contract("_, _, _, _ -> new")
        public static @NotNull MuteMap.Entry create(
                final @NotNull Instant created,
                final @NotNull Instant expiration,
                final @NotNull String reason,
                final @NotNull String source
        ) {
            return new Entry(created, expiration, reason, source);
        }

        /**
         * @return Date when the player was muted
         */
        public Instant getCreated() {
            return this.created;
        }

        /**
         * @return Date when the player will be unmuted
         */
        public Instant getExpiration() {
            return this.expiration;
        }

        /**
         * @return Mute reason
         */
        public String getReason() {
            return this.reason;
        }

        /**
         * @return Mute source could be a player's nickname or CONSOLE
         */
        public String getSource() {
            return this.source;
        }

        /**
         * @return True if created, expiration, reason, source are not null
         */
        public boolean isValidate() {
            return this.created != null
                    && this.expiration != null
                    && this.reason != null
                    && this.source != null;
        }
    }
}
