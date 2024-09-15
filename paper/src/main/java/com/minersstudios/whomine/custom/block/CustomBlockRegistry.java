package com.minersstudios.whomine.custom.block;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.params.PlacingType;
import com.minersstudios.whomine.api.utility.SharedConstants;
import com.minersstudios.whomine.custom.block.params.NoteBlockData;
import com.minersstudios.whomine.api.utility.ChatUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * The CustomBlockRegistry class is responsible for managing and storing custom
 * block data for {@link WhoMine} plugin.
 * <br>
 * It provides various methods to register, unregister, and retrieve custom
 * block data based on different criteria, such as the custom block's key, hash
 * code, or block data.
 * <br>
 * The CustomBlockRegistry uses one concurrent map to store all the registered
 * custom block data associated with the corresponding hash code of the
 * {@link NoteBlockData} of the custom block. And another concurrent map to
 * store the registered keys associated with the corresponding hash code of the
 * {@link NoteBlockData} of the custom block. The {@link #HASH_CODE_MAP} is a
 * main map that stores all the registered custom block data. The 
 * {@link #KEY_MAP} is used to store the registered keys and associated hash
 * codes.
 * <br>
 * All recipes by default are registered after all custom blocks, items, and
 * decorations are registered. This is to avoid problems related to dependencies 
 * between other plugins and custom items, decorations, and blocks.
 * <br>
 * Make sure to use the provided methods and their respective Optional return
 * types to handle cases where the desired custom block data might not be
 * present.
 * <br>
 * Example usage:
 * <pre>{@code
 * // Register a custom block data
 * CustomBlockRegistry.registerData(customBlockData);
 *
 * // Retrieve custom block data using key
 * Optional<CustomBlockData> customBlockData =
 *          CustomBlockRegistry.fromKey("my_custom_block");
 * if (customBlockData.isPresent()) {
 *     // Custom block data found, do something with it
 *     CustomBlockData data = customBlockData.get();
 * }
 *
 * // Check if an item stack is a custom block
 * ItemStack itemStack = ...;
 * if (CustomBlockRegistry.isCustomBlock(itemStack)) {
 *     // Handle the case when the item stack is a custom block
 * }
 *
 * // Check if a block is a custom block
 * Block block = ...;
 * if (CustomBlockRegistry.isCustomBlock(block)) {
 *    // Handle the case when the block is a custom block
 * }
 * }</pre>
 */
public final class CustomBlockRegistry {
    public static final NamespacedKey TYPE_NAMESPACED_KEY = new NamespacedKey(SharedConstants.MSBLOCK_NAMESPACE, "type");

    private static final Int2ObjectMap<CustomBlockData> HASH_CODE_MAP = new Int2ObjectOpenHashMap<>();
    private static final Map<String, IntSet> KEY_MAP = new Object2ObjectOpenHashMap<>();

    static {
        register(CustomBlockData.defaultData());
    }

    @Contract(" -> fail")
    private CustomBlockRegistry() throws AssertionError {
        throw new AssertionError("Utility class");
    }

    /**
     * @return An unmodifiable view of the hash codes of all registered custom 
     *         block data (NoteBlockData)
     * @see #HASH_CODE_MAP
     */
    public static @NotNull @UnmodifiableView Set<Integer> hashCodeSet() {
        return Collections.unmodifiableSet(HASH_CODE_MAP.keySet());
    }

    /**
     * @return An unmodifiable view of the keys of all registered custom 
     *         block data
     * @see #KEY_MAP
     */
    public static @NotNull @UnmodifiableView Set<String> keySet() {
        return Collections.unmodifiableSet(KEY_MAP.keySet());
    }

    /**
     * @return An unmodifiable view of all registered custom block data
     * @see #HASH_CODE_MAP
     */
    public static @NotNull @UnmodifiableView Collection<CustomBlockData> customBlockDataCollection() {
        return Collections.unmodifiableCollection(HASH_CODE_MAP.values());
    }

    /**
     * Gets the {@link CustomBlockData} from the given hash code of the
     * {@link NoteBlockData}. It will get the custom block data from the
     * {@link #HASH_CODE_MAP}.
     *
     * @param hashCode The hash code to get the {@link CustomBlockData} from
     * @return An {@link Optional} containing the {@link CustomBlockData}
     *         or an {@link Optional#empty()} if the given hash code is not
     *         associated with any custom block data
     * @see #HASH_CODE_MAP
     */
    public static @NotNull Optional<CustomBlockData> fromHashCode(final int hashCode) {
        return Optional.ofNullable(HASH_CODE_MAP.get(hashCode));
    }

    /**
     * Gets the {@link CustomBlockData} from the given custom block data key. It 
     * will get the hash code from the {@link #KEY_MAP}, then get the custom
     * block data from the {@link #HASH_CODE_MAP}.
     *
     * @param key The custom block data key to get the {@link CustomBlockData} 
     *            from, must not be blank
     * @return An {@link Optional} containing the {@link CustomBlockData}
     *         or an {@link Optional#empty()} if the given key is not associated 
     *         with any custom block data
     * @see #KEY_MAP
     * @see #fromHashCode(int)
     */
    public static @NotNull Optional<CustomBlockData> fromKey(final @Nullable String key) {
        return ChatUtils.isBlank(key)
                ? Optional.empty()
                : Optional.ofNullable(KEY_MAP.get(key.toLowerCase(Locale.ENGLISH)))
                .flatMap(
                        hashCodes -> hashCodes.isEmpty()
                                ? Optional.empty()
                                : fromHashCode(hashCodes.iterator().nextInt())
                );
    }

    /**
     * Gets the {@link CustomBlockData} from the given block data. It will check
     * if the given block data is an instance of {@link NoteBlock} and if it is,
     * it will get the custom block data from the {@link NoteBlock} by calling 
     * {@link #fromNoteBlock(NoteBlock)} method.
     *
     * @param blockData The block data to get the {@link CustomBlockData} from,
     *                  must be an instance of {@link NoteBlock}
     * @return An {@link Optional} containing the {@link CustomBlockData}
     *         or an {@link Optional#empty()} if the given block data is not an
     *         instance of {@link NoteBlock} or if the given block data is not
     *         associated with any custom block data
     * @see #fromNoteBlock(NoteBlock)
     */
    public static @NotNull Optional<CustomBlockData> fromBlockData(final @NotNull BlockData blockData) {
        return blockData instanceof final NoteBlock noteBlock
                ? fromNoteBlock(noteBlock)
                : Optional.empty();
    }

    /**
     * Gets the {@link CustomBlockData} from the given note block. It will get
     * the {@link NoteBlockData} from the note block, by calling
     * {@link NoteBlockData#from(NoteBlock)} method, and then it will
     * get the custom block data from the note block data, by calling
     * {@link #fromNoteBlockData(NoteBlockData)} method.
     *
     * @param noteBlock The note block to get the {@link CustomBlockData} from
     * @return An {@link Optional} containing the {@link CustomBlockData},
     *         or an {@link Optional#empty()} if the given note block is not
     *         associated with any custom block data
     * @see NoteBlockData#from(NoteBlock)
     * @see #fromNoteBlockData(NoteBlockData)
     */
    public static @NotNull Optional<CustomBlockData> fromNoteBlock(final @NotNull NoteBlock noteBlock) {
        return fromNoteBlockData(NoteBlockData.from(noteBlock));
    }

    /**
     * Gets the {@link CustomBlockData} from the given note block data. It will
     * check the hash code of the note block data, and if it is registered, it
     * will get the {@link CustomBlockData} from the hash code by calling
     * {@link #fromHashCode(int)} method.
     *
     * @param noteBlockData The note block data get the {@link CustomBlockData} 
     *                      from
     * @return An {@link Optional} containing the {@link CustomBlockData}
     *         or an {@link Optional#empty()} if the given note block data
     *         is not associated with any custom block data
     * @see NoteBlockData#hashCode()
     * @see #fromHashCode(int)
     */
    public static @NotNull Optional<CustomBlockData> fromNoteBlockData(final @NotNull NoteBlockData noteBlockData) {
        return fromHashCode(noteBlockData.hashCode());
    }

    /**
     * Gets the {@link CustomBlockData} from the given item stack. It will check
     * the item stack's persistent data container for the 
     * {@link #TYPE_NAMESPACED_KEY} key, and if it has it, it will get the 
     * custom block data from the key by calling {@link #fromKey(String)} method.
     *
     * @param itemStack The item stack to get the {@link CustomBlockData} from
     * @return An {@link Optional} containing the {@link CustomBlockData}, or an
     *         {@link Optional#empty()} if the key from the item stack's
     *         persistent data container is not associated with any custom block 
     *         data
     * @see #TYPE_NAMESPACED_KEY
     * @see #fromKey(String)
     */
    public static @NotNull Optional<CustomBlockData> fromItemStack(final @Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }
        
        final ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta == null
                ? Optional.empty()
                : fromKey(
                        itemMeta.getPersistentDataContainer().get(TYPE_NAMESPACED_KEY, PersistentDataType.STRING)
                );
    }

    /**
     * @param hashCode The hash code of the note block data to check
     * @return True if the {@link #HASH_CODE_MAP} contains the hash code
     */
    public static boolean containsHashCode(final int hashCode) {
        return HASH_CODE_MAP.containsKey(hashCode);
    }

    /**
     * @param key The key to check
     * @return True if the {@link #KEY_MAP} contains the key
     *         and key is not blank or null (case-insensitive)
     */
    @Contract("null -> false")
    public static boolean containsKey(final @Nullable String key) {
        return ChatUtils.isNotBlank(key)
                && KEY_MAP.containsKey(key.toLowerCase(Locale.ENGLISH));
    }

    /**
     * @param customBlockData The custom block data to check
     * @return True if the {@link #HASH_CODE_MAP} contains the hash code of the 
     *         note block data associated with the custom block data
     */
    @Contract("null -> false")
    public static boolean containsCustomBlockData(final @Nullable CustomBlockData customBlockData) {
        if (customBlockData == null) {
            return false;
        }

        final PlacingType placingType = customBlockData.getBlockSettings().getPlacing().getType();

        if (placingType instanceof final PlacingType.Default normal) {
            return containsHashCode(normal.getNoteBlockData().hashCode());
        } else if (placingType instanceof final PlacingType.Directional directional) {
            for (final var noteBlockData : directional.getMap().values()) {
                if (containsHashCode(noteBlockData.hashCode())) {
                    return true;
                }
            }
        } else if (placingType instanceof final PlacingType.Orientable orientable) {
            for (final var noteBlockData : orientable.getMap().values()) {
                if (containsHashCode(noteBlockData.hashCode())) {
                    return true;
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown placing type: " + placingType.getClass().getName());
        }

        return false;
    }

    /**
     * Checks if the item stack is a custom block by verifying if it has a valid 
     * custom block data key associated with it
     *
     * @param itemStack The item stack to check
     * @return True if the item stack is a custom block
     * @see #fromItemStack(ItemStack)
     */
    @Contract("null -> false")
    public static boolean isCustomBlock(final @Nullable ItemStack itemStack) {
        return itemStack != null
                && fromItemStack(itemStack).isPresent();
    }

    /**
     * Checks if the block is a custom block by verifying if its block data is 
     * an instance of {@link NoteBlock} and if it has a valid custom block data 
     * associated with it
     *
     * @param block The block to check
     * @return True if the block is a custom block
     * @see #isCustomBlock(BlockData)
     */
    @Contract("null -> false")
    public static boolean isCustomBlock(final @Nullable Block block) {
        return block != null
                && isCustomBlock(block.getBlockData());
    }

    /**
     * Checks if the block data is a custom block by verifying if it is an 
     * instance of {@link NoteBlock} and if it has a valid custom block data 
     * associated with it
     *
     * @param blockData The block data to check
     * @return True if the block data is a custom block
     * @see #fromNoteBlock(NoteBlock)
     */
    @Contract("null -> false")
    public static boolean isCustomBlock(final @Nullable BlockData blockData) {
        return blockData instanceof NoteBlock noteBlock
                && fromNoteBlock(noteBlock).isPresent();
    }

    /**
     * @return True if the data map is empty
     * @see #HASH_CODE_MAP
     */
    public static boolean isEmpty() {
        return HASH_CODE_MAP.isEmpty();
    }

    /**
     * @return The size of the data map
     * @see #HASH_CODE_MAP
     */
    public static int size() {
        return HASH_CODE_MAP.size();
    }

    /**
     * Registers the custom block data to the data map. The key and hash code
     * are all used to register the custom block data. If the custom block data
     * have the note block data, the note block data's hash code is used to 
     * register the custom block data in the data maps, otherwise the block
     * {@link PlacingType.Directional} or {@link PlacingType.Orientable} is used
     * to generate the hash code of the note block data. Make sure that one of
     * the note block data, block face map, or block axis map is not null.
     *
     * @param customBlockData The custom block data to register
     * @throws IllegalArgumentException If the custom block data is already 
     *                                  registered, or if the custom block data
     *                                  has an unknown placing type
     * @see CustomBlockData
     * @see #KEY_MAP
     * @see #HASH_CODE_MAP
     */
    public static synchronized void register(final @NotNull CustomBlockData customBlockData) throws IllegalArgumentException {
        final String key = customBlockData.getKey();
        final PlacingType placingType = customBlockData.getBlockSettings().getPlacing().getType();

        if (placingType instanceof final PlacingType.Default normal) {
            register(
                    customBlockData,
                    normal.getNoteBlockData().hashCode(),
                    key
            );
        } else if (placingType instanceof final PlacingType.Directional directional) {
            directional.getMap().forEach(
                    (blockFace, data) -> register(
                            customBlockData,
                            data.hashCode(),
                            key
                    )
            );
        } else if (placingType instanceof final PlacingType.Orientable orientable) {
            orientable.getMap().forEach(
                    (blockAxis, data) -> register(
                            customBlockData,
                            data.hashCode(),
                            key
                    )
            );
        } else {
            throw new IllegalArgumentException("Unknown placing type: " + placingType.getClass().getName());
        }
    }

    /**
     * Unregister a custom block data from the data map
     *
     * @param customBlockData The custom block data to unregister
     * @throws IllegalArgumentException If the key, or hash code is not 
     *                                  registered
     */
    public static synchronized void unregister(final @NotNull CustomBlockData customBlockData) throws IllegalArgumentException {
        final String key = customBlockData.getKey().toLowerCase(Locale.ENGLISH);
        final int hashCode = customBlockData.hashCode();

        if (!containsKey(key)) {
            throw new IllegalArgumentException("The key " + key + " is not registered! See " + key + " custom block data!");
        }

        if (!containsHashCode(hashCode)) {
            throw new IllegalArgumentException("The hash code " + hashCode + " is not registered! See " + key + " custom block data!");
        }

        KEY_MAP.remove(key);
        HASH_CODE_MAP.remove(hashCode);
    }

    /**
     * Unregisters all custom block data and recipes by clearing all maps and
     * lists used to store them. After this method is called, the custom block 
     * registry will be empty.
     */
    public static synchronized void unregisterAll() {
        KEY_MAP.clear();
        HASH_CODE_MAP.clear();
    }

    /**
     * Registers the custom block data to the :
     * <ul>
     *     <li>{@link #HASH_CODE_MAP}</li>
     *     <li>{@link #KEY_MAP}</li>
     * </ul>
     *
     * @param customBlockData The custom block data to register
     * @param hashCode        The hash code of the note block data to register
     * @param key             The key of the custom block data to register
     * @throws IllegalArgumentException If the hash code, or key is already 
     *                                  registered
     * @see #HASH_CODE_MAP
     * @see #KEY_MAP
     */
    private static synchronized void register(
            final @NotNull CustomBlockData customBlockData,
            final int hashCode,
            final String key
    ) throws IllegalArgumentException {
        if (containsCustomBlockData(customBlockData)) {
            throw new IllegalArgumentException("The custom block data is already registered! See " + key + " custom block data!");
        }

        if (
                customBlockData.getBlockSettings().getPlacing().getType() instanceof PlacingType.Default
                && containsHashCode(hashCode)
        ) {
            throw new IllegalArgumentException("The hash code " + hashCode + " is already registered! See " + key + " custom block data!");
        }

        final var hashKeys = KEY_MAP.computeIfAbsent(key, k -> new IntOpenHashSet());

        hashKeys.add(hashCode);
        HASH_CODE_MAP.put(hashCode, customBlockData);
        KEY_MAP.put(key, hashKeys);
    }
}
