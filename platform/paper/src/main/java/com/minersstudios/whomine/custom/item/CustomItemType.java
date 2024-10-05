package com.minersstudios.whomine.custom.item;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.annotation.Resource;
import com.minersstudios.whomine.api.module.MainModule;
import com.minersstudios.whomine.custom.item.damageable.Damageable;
import com.minersstudios.whomine.custom.item.registry.*;
import com.minersstudios.whomine.menu.CraftsMenu;
import com.minersstudios.whomine.custom.item.registry.hazmat.HazmatBoots;
import com.minersstudios.whomine.custom.item.registry.hazmat.HazmatChestplate;
import com.minersstudios.whomine.custom.item.registry.hazmat.HazmatHelmet;
import com.minersstudios.whomine.custom.item.registry.hazmat.HazmatLeggings;
import com.minersstudios.whomine.custom.item.registry.cards.CardsBicycle;
import com.minersstudios.whomine.custom.item.registry.cosmetics.LeatherHat;
import com.minersstudios.whomine.api.status.StatusHandler;
import com.minersstudios.whomine.api.status.StatusWatcher;
import com.minersstudios.whomine.api.utility.ChatUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * The CustomItemType enum represents various types of custom items in the
 * MSItem plugin. Each enum value is associated with a specific class that
 * implements the CustomItem interface. This class provides methods to manage
 * and retrieve custom item instances, keys, and types.
 */
public enum CustomItemType {
    //<editor-fold desc="Types" defaultstate="collapsed">
    LEATHER_HAT(LeatherHat.class),
    PLUMBUM_INGOT(PlumbumIngot.class),
    RAW_PLUMBUM(RawPlumbum.class),
    ANTI_RADIATION_TEXTILE(AntiRadiationTextile.class),
    DOSIMETER(Dosimeter.class),
    HAZMAT_HELMET(HazmatHelmet.class),
    HAZMAT_CHESTPLATE(HazmatChestplate.class),
    HAZMAT_LEGGINGS(HazmatLeggings.class),
    HAZMAT_BOOTS(HazmatBoots.class),
    COCAINE(Cocaine.class),
    WRENCH(Wrench.class),
    CARDS_BICYCLE_BLUE_1(CardsBicycle.Blue.First.class),
    CARDS_BICYCLE_BLUE_2(CardsBicycle.Blue.Second.class),
    CARDS_BICYCLE_RED_1(CardsBicycle.Red.First.class),
    CARDS_BICYCLE_RED_2(CardsBicycle.Red.Second.class),
    BAN_SWORD(BanSword.class);
    //</editor-fold>

    private final Class<? extends CustomItem> clazz;

    public static final String TYPE_TAG_NAME = "type";
    public static final NamespacedKey TYPE_NAMESPACED_KEY = new NamespacedKey(Resource.WMITEM, TYPE_TAG_NAME);

    private static final Map<String, CustomItemType> KEY_TO_TYPE_MAP = new Object2ObjectOpenHashMap<>();
    private static final Map<Class<? extends CustomItem>, CustomItemType> CLASS_TO_TYPE_MAP = new Object2ObjectOpenHashMap<>();
    static final Map<Class<? extends CustomItem>, CustomItem> CLASS_TO_ITEM_MAP = new Object2ObjectOpenHashMap<>();

    /**
     * Constructor for CustomItemType enum values
     *
     * @param clazz The associated class that implements the CustomItem
     *              interface
     */
    CustomItemType(final @NotNull Class<? extends CustomItem> clazz) {
        this.clazz = clazz;
    }

    /**
     * Loads all custom item types
     *
     * @param plugin The plugin instance
     * @throws IllegalStateException If the custom item types have already been
     *                               loaded
     */
    @ApiStatus.Internal
    public static void load(final @NotNull WhoMine plugin) throws IllegalStateException {
        if (!KEY_TO_TYPE_MAP.isEmpty()) {
            throw new IllegalStateException("Custom item types have already been loaded!");
        }

        final long startTime = System.currentTimeMillis();
        final StatusHandler statusHandler = plugin.getStatusHandler();
        final CustomItemType[] types = values();
        final var typesWithRecipes = new ObjectArrayList<CustomItemType>();

        statusHandler.addWatcher(
                StatusWatcher.builder()
                .successStatuses(
                        MainModule.LOADED_BLOCKS,
                        MainModule.LOADED_DECORATIONS,
                        MainModule.LOADED_ITEMS
                )
                .successRunnable(
                        () -> plugin.runTask(() -> {
                            for (final var item : typesWithRecipes) {
                                item.getCustomItem().registerRecipes(plugin);
                            }

                            CraftsMenu.putCrafts(
                                    CraftsMenu.Type.ITEMS,
                                    plugin.getCache().customItemRecipes
                            );
                        })
                )
                .build()
        );

        statusHandler.assignStatus(MainModule.LOADING_ITEMS);
        Stream.of(types).parallel()
        .forEach(type -> {
            final CustomItem customItem;

            try {
                customItem = type.getItemClass().getDeclaredConstructor().newInstance();
            } catch (final Throwable e) {
                plugin.getLogger().log(
                        Level.SEVERE,
                        "An error occurred while loading custom item " + type.name(),
                        e
                );

                return;
            }

            if (customItem instanceof final Damageable damageable) {
                damageable.buildDamageable().saveForItemStack(customItem.getItem());
            }

            synchronized (CustomItemType.class) {
                KEY_TO_TYPE_MAP.put(customItem.getKey().getKey().toLowerCase(Locale.ENGLISH), type);
                CLASS_TO_TYPE_MAP.put(type.clazz, type);
                CLASS_TO_ITEM_MAP.put(type.clazz, customItem);
                typesWithRecipes.add(type);
            }
        });
        typesWithRecipes.sort(Comparator.comparingInt(CustomItemType::ordinal));

        statusHandler.assignStatus(MainModule.LOADED_ITEMS);
        plugin.getComponentLogger().info(
                Component.text(
                        "Loaded " + types.length + " custom items in " + (System.currentTimeMillis() - startTime) + "ms",
                        NamedTextColor.GREEN
                )
        );
    }

    /**
     * @return The class associated with this custom item type
     */
    public @NotNull Class<? extends CustomItem> getItemClass() {
        return this.clazz;
    }

    /**
     * @return The CustomItem instance associated with this custom item type
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     */
    public @NotNull CustomItem getCustomItem() throws IllegalStateException {
        checkLoaded();
        return CLASS_TO_ITEM_MAP.get(this.clazz);
    }

    /**
     * @param clazz The target class to cast the custom item instance
     * @param <I>   The type of the target class
     * @return The custom item instance cast to the specified class
     * @throws IllegalArgumentException If the custom item instance cannot be
     *                                  cast to the specified class
     * @throws IllegalStateException    If custom item types have not been
     *                                  loaded yet
     */
    public <I extends CustomItem> @NotNull I getCustomItem(final @NotNull Class<I> clazz) throws IllegalArgumentException, IllegalStateException {
        checkLoaded();

        final CustomItem customItem = CLASS_TO_ITEM_MAP.get(this.clazz);

        try {
            return clazz.cast(customItem);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException("Custom item " + this.name() + " is not an instance of " + clazz.getName() + "!");
        }
    }

    /**
     * Gets the {@link CustomItemType} from the given key
     *
     * @param key The key to get the custom item type from, must not be null
     *            (case-insensitive)
     * @return The {@link CustomItemType} associated with the given key or null
     *         if the given key is not associated with any custom item type,
     *         or if the given key is null or blank
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     * @see #KEY_TO_TYPE_MAP
     */
    @Contract("null -> null")
    public static @Nullable CustomItemType fromKey(final @Nullable String key) throws IllegalStateException {
        checkLoaded();
        return ChatUtils.isBlank(key)
                ? null
                : KEY_TO_TYPE_MAP.get(key.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Gets the {@link CustomItemType} from the given class
     *
     * @param clazz The class to get the custom item type from
     * @return The {@link CustomItemType} associated with the given class
     *         or null if the given class is not associated with any custom item
     *         type, or if the given class is null
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     * @see #CLASS_TO_TYPE_MAP
     */
    @Contract("null -> null")
    public static @Nullable CustomItemType fromClass(final @Nullable Class<? extends CustomItem> clazz) throws IllegalStateException {
        checkLoaded();
        return clazz == null
                ? null
                : CLASS_TO_TYPE_MAP.get(clazz);
    }

    /**
     * Gets the {@link CustomItemType} from the given item stack.
     * <br>
     * It will get the namespaced key from the item stack's persistent data
     * container and then get the custom item type from {@link #KEY_TO_TYPE_MAP}
     *
     * @param itemStack The item stack to get the custom item type from
     * @return The {@link CustomItemType} associated with the given item stack
     *         or null if the given item stack is not associated with any custom
     *         item type, or if the given item stack is null, or an air item
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     * @see #fromKey(String)
     */
    @Contract("null -> null")
    public static @Nullable CustomItemType fromItemStack(final @Nullable ItemStack itemStack) throws IllegalStateException {
        checkLoaded();

        if (itemStack == null) {
            return null;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();

        return itemMeta == null
                ? null
                : fromKey(
                        itemMeta.getPersistentDataContainer().get(
                                TYPE_NAMESPACED_KEY,
                                PersistentDataType.STRING
                        )
                );
    }

    /**
     * @return An unmodifiable view of the custom item key set
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     * @see #KEY_TO_TYPE_MAP
     */
    public static @NotNull @UnmodifiableView Set<String> keySet() throws IllegalStateException {
        checkLoaded();
        return Collections.unmodifiableSet(KEY_TO_TYPE_MAP.keySet());
    }

    /**
     * @return An unmodifiable view of a set of custom item classes that
     *         implement the CustomItem interface
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     * @see #CLASS_TO_TYPE_MAP
     */
    public static @NotNull @UnmodifiableView Set<Class<? extends CustomItem>> classSet() throws IllegalStateException {
        checkLoaded();
        return Collections.unmodifiableSet(CLASS_TO_TYPE_MAP.keySet());
    }

    /**
     * @return An unmodifiable view of the custom item instances collection
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     * @see #CLASS_TO_ITEM_MAP
     */
    public static @NotNull @UnmodifiableView Collection<CustomItem> customItems() throws IllegalStateException {
        checkLoaded();
        return Collections.unmodifiableCollection(CLASS_TO_ITEM_MAP.values());
    }

    /**
     * @param key The key to check
     * @return True if the {@link #KEY_TO_TYPE_MAP} contains the given key
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     */
    @Contract("null -> false")
    public static boolean containsKey(final @Nullable String key) throws IllegalStateException {
        checkLoaded();
        return ChatUtils.isNotBlank(key)
                && KEY_TO_TYPE_MAP.containsKey(key.toLowerCase(Locale.ENGLISH));
    }

    /**
     * @param clazz The class to check
     * @return True if the {@link #CLASS_TO_TYPE_MAP} contains the given class
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     */
    @Contract("null -> false")
    public static boolean containsClass(final @Nullable Class<? extends CustomItem> clazz) throws IllegalStateException {
        checkLoaded();
        return clazz != null
                && CLASS_TO_TYPE_MAP.containsKey(clazz);
    }

    /**
     * Checks if custom item types have been loaded yet
     *
     * @throws IllegalStateException If custom item types have not been loaded
     *                               yet
     */
    private static void checkLoaded() throws IllegalStateException {
        if (KEY_TO_TYPE_MAP.isEmpty()) {
            throw new IllegalStateException("Custom item types have not been loaded yet!");
        }
    }
}
