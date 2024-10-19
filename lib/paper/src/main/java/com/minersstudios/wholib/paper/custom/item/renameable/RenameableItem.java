package com.minersstudios.wholib.paper.custom.item.renameable;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.annotation.ResourcePath;
import com.minersstudios.wholib.paper.utility.MSCustomUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.kyori.adventure.text.Component.text;

/**
 * The RenameableItem class represents a renameable item with associated renames,
 * whitelisted players, and menu visibility. It facilitates the creation,
 * configuration, and management of renameable items.
 */
public class RenameableItem {
    private final String key;
    private final RenameCollection renameCollection;
    private final Set<UUID> whiteList;
    private final boolean isShowInRenameMenu;

    /**
     * Constructs a RenameableItem instance with the given unique key and rename
     * collection
     *
     * @param plugin           The plugin instance
     * @param key              The unique key associated with the item
     * @param renameCollection The collection of renames associated with item
     */
    public RenameableItem(
            final @NotNull WhoMine plugin,
            final @NotNull String key,
            final @NotNull RenameCollection renameCollection
    ) {
        this(plugin, key, renameCollection, true);
    }

    /**
     * Constructs a RenameableItem instance with the given unique key, rename
     * collection, and visibility in the rename menu
     *
     * @param plugin             The plugin instance
     * @param key                The unique key associated with the item
     * @param renameCollection   The collection of renames associated with item
     * @param isShowInRenameMenu Whether the item should appear in the rename
     *                           menu
     */
    public RenameableItem(
            final @NotNull WhoMine plugin,
            final @NotNull String key,
            final @NotNull RenameCollection renameCollection,
            final boolean isShowInRenameMenu
    ) {
        this(plugin, key, renameCollection, null, isShowInRenameMenu);
    }

    /**
     * Constructs a RenameableItem instance with the given unique key, rename
     * collection, and whitelist
     *
     * @param plugin           The plugin instance
     * @param key              The unique key associated with the item
     * @param renameCollection The collection of renames associated with the item
     * @param whiteList        The set of players that are allowed to rename the
     *                         item (an empty set means all players are allowed)
     */
    public RenameableItem(
            final @NotNull WhoMine plugin,
            final @NotNull String key,
            final @NotNull RenameCollection renameCollection,
            final @NotNull Collection<UUID> whiteList
    ) {
        this(plugin, key, renameCollection, whiteList, true);
    }

    /**
     * Constructs a RenameableItem instance with the given unique key, rename
     * collection, whitelist, and visibility in the rename menu
     *
     * @param key                The unique key associated with the item
     * @param isShowInRenameMenu Whether the item should appear in the rename
     *                           menu
     * @param renameCollection   The collection of renames associated with the
     *                           item
     * @param whiteList          The set of players that are allowed to rename
     *                           the item (an empty set means all players are
     *                           allowed)
     */
    public RenameableItem(
            final @NotNull WhoMine plugin,
            final @NotNull String key,
            final @NotNull RenameCollection renameCollection,
            final @Nullable Collection<UUID> whiteList,
            final boolean isShowInRenameMenu
    ) {
        this.key = key.toLowerCase(Locale.ENGLISH);
        this.renameCollection = renameCollection;
        this.isShowInRenameMenu = isShowInRenameMenu;
        this.whiteList =
                whiteList == null
                ? Collections.emptySet()
                : new ObjectOpenHashSet<>(whiteList);

        if (isShowInRenameMenu) {
            plugin.getCache().getRenameableMenuItems().add(this);
        }
    }

    /**
     * Creates a renameable item instance by loading its configuration from a
     * file
     *
     * @param file The configuration file to load
     * @return A RenameableItem instance loaded from the file, or null if
     *         loading fails
     */
    public static @Nullable RenameableItem fromFile(
            final @NotNull WhoMine plugin,
            final @NotNull File file
    ) {
        final Logger logger = plugin.getLogger();
        final YamlConfiguration renameableItemConfig;

        try {
            renameableItemConfig = YamlConfiguration.loadConfiguration(file);
        } catch (final Throwable e) {
            logger.log(Level.SEVERE, "Failed to load " + file.getName() + "!", e);
            return null;
        }

        final String fileName = file.getName();
        final String key = renameableItemConfig.getString("key");

        if (key == null) {
            logger.severe("Key is not defined in " + fileName + "!");
            return null;
        }

        final var itemString = renameableItemConfig.getStringList("items");
        final var renameString = renameableItemConfig.getStringList("renames");
        final var loreString = renameableItemConfig.getStringList("lore");
        final int customModelData = renameableItemConfig.getInt("custom-model-data", -1);

        final RenameCollection renameCollection = new RenameCollection(key);
        final var lore = new ObjectArrayList<Component>();
        final var whiteList = new ObjectOpenHashSet<UUID>();

        if (itemString.isEmpty()) {
            final String item = renameableItemConfig.getString("items");

            if (item == null) {
                logger.severe("Items are not defined in " + fileName + "!");
                return null;
            }

            itemString.add(item);
        }

        if (renameString.isEmpty()) {
            final String rename = renameableItemConfig.getString("renames");

            if (rename == null) {
                logger.severe("Renames are not defined in " + fileName + "!");

                return null;
            }

            renameCollection.addRename(rename);
        } else {
            for (final var rename : renameString) {
                renameCollection.addRename(rename);
            }
        }

        if (!loreString.isEmpty()) {
            for (final var text : loreString) {
                lore.add(text(text));
            }
        }

        if (customModelData < 0) {
            logger.severe("Custom model data is not valid! (in " + fileName + ")");
            return null;
        }

        for (final @ResourcePath var namespacedKey : itemString) {
            final ItemStack itemStack;

            if (namespacedKey.contains(":")) {
                itemStack = MSCustomUtils.getItemStack(namespacedKey)
                        .map(ItemStack::new)
                        .orElse(null);
            } else {
                final Material material = Material.getMaterial(namespacedKey);
                itemStack = material == null
                        ? null
                        : new ItemStack(material);
            }

            if (
                    itemStack == null
                    || itemStack.isEmpty()
            ) {
                logger.severe("Item " + namespacedKey + " is not valid! (in " + fileName + ")");
                return null;
            }

            if (renameCollection.isEmptyItems()) {
                final ItemStack mainItem = itemStack.clone();
                final ItemMeta meta = mainItem.getItemMeta();

                if (!lore.isEmpty()) {
                    meta.lore(lore);
                }

                meta.setCustomModelData(customModelData);
                mainItem.setItemMeta(meta);
                renameCollection.setMainItem(mainItem);
            }

            renameCollection.addItem(itemStack);
        }

        for (final var uuid : renameableItemConfig.getStringList("white-list")) {
            try {
                whiteList.add(UUID.fromString(uuid));
            } catch (final IllegalArgumentException ignored) {
                logger.severe("Invalid UUID " + uuid + " in white-list! (in " + fileName + ")");
            }
        }

        return new RenameableItem(
                plugin,
                key,
                renameCollection,
                whiteList,
                renameableItemConfig.getBoolean("show-in-rename-menu")
        );
    }

    /**
     * @return The unique key associated with the item
     */
    public @NotNull String getKey() {
        return this.key;
    }

    /**
     * @return The collection of renames associated with the item
     */
    public @NotNull RenameCollection getRenames() {
        return this.renameCollection;
    }

    /**
     * @return The set of uuids of players that are allowed to rename the item
     *         (an empty set means all players are allowed)
     */
    public @NotNull @Unmodifiable Set<UUID> getWhiteList() {
        return Collections.unmodifiableSet(this.whiteList);
    }

    /**
     * @return Whether the item should appear in the rename menu
     */
    public boolean isShowInRenameMenu() {
        return this.isShowInRenameMenu;
    }

    /**
     * @param player The player to check
     * @return Whether the player is allowed to rename the item
     *         (true if the whitelist is empty)
     */
    public boolean isWhiteListed(final @Nullable OfflinePlayer player) {
        return this.whiteList.isEmpty()
                || (
                        player != null
                        && this.whiteList.contains(player.getUniqueId())
                );
    }

    /**
     * Creates a copy of the given item with the rename metadata applied to it
     *
     * @param item   The item to get the rename for
     * @param rename The rename to get the item for
     * @return The item with the rename metadata applied to it,
     *         or null if the item is null, the rename is null
     *         or blank, the rename collection is not initialized,
     *         or the item is air
     * @see RenameCollection#craftRenamed(ItemStack, String)
     */
    @Contract("null, null -> null")
    public @Nullable ItemStack craftRenamed(
            final @Nullable ItemStack item,
            final @Nullable String rename
    ) {
        return this.renameCollection.craftRenamed(item, rename);
    }
}
