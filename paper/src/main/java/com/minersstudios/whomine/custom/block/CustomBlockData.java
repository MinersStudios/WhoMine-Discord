package com.minersstudios.whomine.custom.block;

import com.google.gson.JsonElement;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.block.file.CustomBlockFile;
import com.minersstudios.whomine.custom.block.file.adapter.RecipeAdapter;
import com.minersstudios.whomine.custom.block.params.*;
import com.minersstudios.whomine.custom.block.params.settings.Placing;
import com.minersstudios.whomine.custom.block.params.settings.Tool;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.menu.CraftsMenu;
import com.minersstudios.whomine.world.sound.SoundGroup;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

/**
 * Class representing the data of a custom block. This class holds information
 * about the block's key, block settings, drop settings, sound group, and recipe
 * entries. It also holds a default instance of the CustomBlockData class which
 * is used as a fallback in some scenarios.
 *
 * @see #defaultData()
 */
@Immutable
public final class CustomBlockData {
    private final String key;
    private final BlockSettings blockSettings;
    private final DropSettings dropSettings;
    private final SoundGroup soundGroup;
    private final List<RecipeEntry> recipeEntries;

    private static final CustomBlockData DEFAULT = new CustomBlockData(
            //<editor-fold desc="Default note block params" defaultstate="collapsed">
            "default",
            new BlockSettings(
                    11.0f,
                    Tool.create(
                            ToolType.AXE,
                            false
                    ),
                    Placing.create(
                            PlacingType.defaultType(NoteBlockData.defaultData())
                    )
            ),
            new DropSettings(
                    new ItemStack(Material.NOTE_BLOCK),
                    0
            ),
            SoundGroup.WOOD
            //</editor-fold>
    );

    /**
     * Constructs the CustomBlockData with the specified parameters
     *
     * @param key           The key of the custom block data
     * @param blockSettings The block settings of the custom block data
     * @param dropSettings  The drop settings of the custom block data
     * @param soundGroup    The sound group of the custom block data
     * @param recipeEntries The recipe entries of the custom block data
     */
    public CustomBlockData(
            final @NotNull String key,
            final @NotNull BlockSettings blockSettings,
            final @NotNull DropSettings dropSettings,
            final @NotNull SoundGroup soundGroup,
            final @NotNull List<RecipeEntry> recipeEntries
    ) {
        this.key = key.toLowerCase(Locale.ENGLISH);
        this.blockSettings = blockSettings;
        this.dropSettings = dropSettings;
        this.soundGroup = soundGroup;
        this.recipeEntries = new ObjectArrayList<>(recipeEntries);
    }

    /**
     * Constructs the CustomBlockData with the specified parameters
     *
     * @param key           The key of the custom block data
     * @param blockSettings The block settings of the custom block data
     * @param dropSettings  The drop settings of the custom block data
     * @param soundGroup    The sound group of the custom block data
     * @param recipeEntries The recipe entries of the custom block data
     */
    public CustomBlockData(
            final @NotNull String key,
            final @NotNull BlockSettings blockSettings,
            final @NotNull DropSettings dropSettings,
            final @NotNull SoundGroup soundGroup,
            final RecipeEntry @NotNull ... recipeEntries
    ) {
        this(
                key,
                blockSettings,
                dropSettings,
                soundGroup,
                ObjectArrayList.of(recipeEntries)
        );
    }

    /**
     * Copies the specified custom block data
     *
     * @param data The custom block data to copy
     */
    @ApiStatus.Internal
    public CustomBlockData(final @NotNull CustomBlockData data) {
        this(
                data.key,
                data.blockSettings,
                data.dropSettings,
                data.soundGroup,
                data.recipeEntries == null ? ObjectLists.emptyList() : data.recipeEntries
        );
    }

    /**
     * Loads the custom block data from the specified file
     *
     * @param file The file to load the custom block data from
     * @return The custom block data loaded from the file, or null if an error
     *         occurred
     * @see CustomBlockFile#create(WhoMine, File)
     */
    public static @Nullable CustomBlockData fromFile(
            final @NotNull WhoMine plugin,
            final @NotNull File file
    ) {
        final CustomBlockFile blockFile = CustomBlockFile.create(plugin, file);

        if (blockFile == null) {
            plugin.getLogger().severe("Failed to load custom block file: " + file.getName());

            return null;
        }

        return blockFile.getData();
    }

    /**
     * @return Default custom block data with the following parameters:
     *     <br> - key: "default"
     *     <br> - hardness: 11.0f
     *     <br> - custom model data: 0
     *     <br> - tool type: {@link ToolType#AXE}
     *     <br> - note block data: {@link NoteBlockData#defaultData()}
     *     <br> - sound group: {@link SoundGroup#WOOD}
     * @see #DEFAULT
     */
    public static @NotNull CustomBlockData defaultData() {
        return DEFAULT;
    }

    /**
     * @return The key of the custom block data
     */
    public @NotNull String getKey() {
        return this.key;
    }

    /**
     * @return The block settings of the custom block data
     * @see BlockSettings
     */
    public @NotNull BlockSettings getBlockSettings() {
        return this.blockSettings;
    }

    /**
     * @return The drop settings of the custom block data
     * @see DropSettings
     */
    public @NotNull DropSettings getDropSettings() {
        return this.dropSettings;
    }

    /**
     * @return The sound group of the custom block data
     * @see SoundGroup
     */
    public @NotNull SoundGroup getSoundGroup() {
        return this.soundGroup;
    }

    /**
     * @return The recipe entries of the custom block data
     */
    public @NotNull @Unmodifiable List<RecipeEntry> getRecipeEntries() {
        return Collections.unmodifiableList(this.recipeEntries);
    }

    /**
     * @return True if the custom block data is the default instance
     * @see #defaultData()
     */
    public boolean isDefault() {
        return this == DEFAULT;
    }

    /**
     * Converts the custom block data to a custom block file
     *
     * @param file The file that stores the custom block data
     * @return The custom block file created from the custom block data
     * @throws IllegalArgumentException If the file is not a json file
     * @see CustomBlockFile#create(WhoMine, File)
     */
    public @NotNull CustomBlockFile toFile(final @NotNull File file) throws IllegalArgumentException {
        return CustomBlockFile.create(file, this);
    }

    /**
     * Creates an ItemStack based on the custom block data parameters. The
     * ItemStack will have the custom block data key stored in its persistent
     * data container.
     *
     * @return The new ItemStack based on the custom block data parameters
     * @see CustomBlockRegistry#TYPE_NAMESPACED_KEY
     */
    public @NotNull ItemStack craftItemStack() {
        final ItemStack itemStack = this.dropSettings.getItem();
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(
                CustomBlockRegistry.TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING,
                this.key
        );
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Registers the recipes of the custom block data. If the showInCraftsMenu
     * parameter of a recipe entry is true, the recipe will be added to the
     * global cache's custom block recipes list. This list is used to display
     * the custom block recipes in the {@link CraftsMenu}
     *
     * @param plugin     The plugin that owns this custom block data
     * @param recipeJson The json element containing the recipes
     */
    public void registerRecipes(
            final @NotNull WhoMine plugin,
            final @NotNull JsonElement recipeJson
    ) {
        final Server server = plugin.getServer();

        try {
            this.recipeEntries.addAll(
                    RecipeAdapter.deserializeEntries(
                            new ItemStack(this.craftItemStack()),
                            recipeJson.getAsJsonArray()
                    )
            );
        } catch (final Throwable e) {
            plugin.getLogger().log(
                    Level.SEVERE,
                    "Failed to deserialize recipes for custom block data: " + this.key,
                    e
            );

            return;
        }

        for (final var recipeEntry : this.recipeEntries) {
            final Recipe recipe = recipeEntry.getRecipe();

            server.addRecipe(recipe);

            if (recipeEntry.isRegisteredInMenu()) {
                plugin.getCache().customBlockRecipes.add(recipe);
            }
        }
    }

    /**
     * Unregisters the recipes of the custom block data
     */
    public void unregisterRecipes(final @NotNull WhoMine plugin) {
        if (this.recipeEntries == null) {
            return;
        }

        for (final var entry : this.recipeEntries) {
            final Keyed recipe = (Keyed) entry.getRecipe();

            plugin.getServer().removeRecipe(recipe.getKey());

            if (entry.isRegisteredInMenu()) {
                plugin.getCache().customBlockRecipes.remove(recipe);
            }
        }
    }
}
