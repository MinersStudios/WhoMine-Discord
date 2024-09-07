package com.minersstudios.whomine.custom.decor;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.annotation.Key;
import com.minersstudios.whomine.custom.decor.action.DecorBreakAction;
import com.minersstudios.whomine.custom.decor.action.DecorClickAction;
import com.minersstudios.whomine.custom.decor.action.DecorPlaceAction;
import com.minersstudios.whomine.inventory.recipe.entry.RecipeEntry;
import com.minersstudios.whomine.world.location.MSBoundingBox;
import com.minersstudios.whomine.world.location.MSPosition;
import com.minersstudios.whomine.world.location.MSVector;
import com.minersstudios.whomine.api.throwable.InvalidRegexException;
import com.minersstudios.whomine.custom.decor.event.CustomDecorBreakEvent;
import com.minersstudios.whomine.custom.decor.event.CustomDecorClickEvent;
import com.minersstudios.whomine.custom.decor.event.CustomDecorPlaceEvent;
import com.minersstudios.whomine.world.sound.SoundGroup;
import com.minersstudios.whomine.utility.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Light;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The CustomDecorDataImpl class serves as a base implementation of the
 * {@link CustomDecorData} interface. It provides methods to get and manipulate
 * the properties of the decor, etc. It is recommended to extend this class when
 * creating custom decor data classes.
 *
 * @see CustomDecorData
 * @see Builder
 */
@Immutable
public abstract class CustomDecorDataImpl<D extends CustomDecorData<D>> implements CustomDecorData<D> {
    private final NamespacedKey namespacedKey;
    private final DecorHitBox hitBox;
    private final EnumSet<Facing> facingSet;
    private final SoundGroup soundGroup;
    private final ItemStack itemStack;
    private final List<RecipeEntry> recipeEntries;
    private final Function<D, RecipeEntry>[] recipeFunctions;
    private final EnumSet<DecorParameter> parameterSet;
    private final double sitHeight;
    private final int[] lightLevels;
    private final CustomDecorData.Type<D>[] types;
    private final EnumMap<Facing, CustomDecorData.Type<D>> faceTypeMap;
    private final Int2ObjectMap<CustomDecorData.Type<D>> lightLevelTypeMap;
    private final DecorClickAction clickAction;
    private final DecorPlaceAction placeAction;
    private final DecorBreakAction breakAction;
    private final boolean isDropType;

    private static final int MAX_DECORATIONS_IN_BLOCK = 6;

    /**
     * Protected constructor to initialize custom decor data with the
     *
     * @throws IllegalStateException If the pre-build validation fails
     * @see #builder()
     */
    protected CustomDecorDataImpl() throws IllegalStateException {
        final Builder builder = this.builder();

        builder.preBuild();

        this.namespacedKey = builder.namespacedKey;
        this.hitBox = builder.hitBox;
        this.facingSet = builder.facingSet;
        this.soundGroup = builder.soundGroup;
        this.itemStack = builder.itemStack;
        this.parameterSet = builder.parameterSet;
        this.sitHeight = builder.sitHeight;
        this.types = builder.types;
        this.faceTypeMap = builder.faceTypeMap;
        this.lightLevels = builder.lightLevels;
        this.lightLevelTypeMap = builder.lightLevelTypeMap;
        this.clickAction = builder.clickAction;
        this.placeAction = builder.placeAction;
        this.breakAction = builder.breakAction;
        this.isDropType = builder.dropsType;

        if (builder.recipeFunctions != null) {
            this.recipeEntries = new ObjectArrayList<>(builder.recipeFunctions.length);
            this.recipeFunctions = builder.recipeFunctions;
        } else {
            this.recipeEntries = Collections.emptyList();
            this.recipeFunctions = null;
        }
    }

    /**
     * Protected constructor to initialize custom decor data with the given
     * builder and plugin instance
     *
     * @param plugin The plugin instance
     * @throws IllegalStateException If the pre-build validation fails
     * @see #builder(WhoMine)
     */
    protected CustomDecorDataImpl(final @NotNull WhoMine plugin) throws IllegalStateException {
        final Builder builder = this.builder(plugin);

        builder.preBuild();

        this.namespacedKey = builder.namespacedKey;
        this.hitBox = builder.hitBox;
        this.facingSet = builder.facingSet;
        this.soundGroup = builder.soundGroup;
        this.itemStack = builder.itemStack;
        this.parameterSet = builder.parameterSet;
        this.sitHeight = builder.sitHeight;
        this.types = builder.types;
        this.faceTypeMap = builder.faceTypeMap;
        this.lightLevels = builder.lightLevels;
        this.lightLevelTypeMap = builder.lightLevelTypeMap;
        this.clickAction = builder.clickAction;
        this.placeAction = builder.placeAction;
        this.breakAction = builder.breakAction;
        this.isDropType = builder.dropsType;

        if (builder.recipeFunctions != null) {
            this.recipeEntries = new ObjectArrayList<>(builder.recipeFunctions.length);
            this.recipeFunctions = builder.recipeFunctions;
        } else {
            this.recipeEntries = Collections.emptyList();
            this.recipeFunctions = null;
        }
    }

    /**
     * Override this method to provide a builder for the custom decor data
     *
     * @return The builder of the custom decor data
     */
    protected @NotNull Builder builder() {
        throw new UnsupportedOperationException("Method without plugin must be overridden!");
    }

    /**
     * Override this method to provide a builder for the custom decor data
     *
     * @return The builder of the custom decor data
     */
    protected @NotNull Builder builder(final @NotNull WhoMine plugin) {
        throw new UnsupportedOperationException("Method with plugin must be overridden!");
    }

    @Override
    public final @NotNull NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public final @NotNull DecorHitBox getHitBox() {
        return this.hitBox;
    }

    @Override
    public final @NotNull @Unmodifiable Set<Facing> getFacingSet() {
        return Collections.unmodifiableSet(this.facingSet);
    }

    @Override
    public final @NotNull ItemStack getItem() {
        return this.itemStack.clone();
    }

    @Override
    public final @NotNull SoundGroup getSoundGroup() {
        return this.soundGroup;
    }

    @Override
    public final Function<D, RecipeEntry> @Nullable [] recipeFunctions() {
        return this.recipeFunctions == null
                ? null
                : this.recipeFunctions.clone();
    }

    @Override
    public final @NotNull @UnmodifiableView List<RecipeEntry> recipeEntries() {
        return Collections.unmodifiableList(this.recipeEntries);
    }

    @Override
    public @NotNull @Unmodifiable EnumSet<DecorParameter> parameterSet() {
        return this.parameterSet.clone();
    }

    @Override
    public CustomDecorData.Type<D> @NotNull [] types() throws UnsupportedOperationException {
        if (
                !this.isWrenchable()
                || !this.isTyped()
        ) {
            throw new UnsupportedOperationException("This custom decor is not wrenchable or typed!");
        }

        return this.types.clone();
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getTypeOf(final @Nullable Interaction interaction) throws UnsupportedOperationException {
        return interaction == null
                ? null
                : this.getTypeOf(
                        CustomDecor.fromInteraction(interaction).map(
                                customDecor -> customDecor.getDisplay().getItemStack()
                        ).orElse(null)
                );
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getTypeOf(final @Nullable ItemStack itemStack) throws UnsupportedOperationException {
        if (
                !this.isWrenchable()
                && !this.isTyped()
        ) {
            throw new UnsupportedOperationException("This custom decor is not wrenchable or typed!");
        }

        if (itemStack == null) {
            return null;
        }

        final String key = itemStack.getItemMeta().getPersistentDataContainer().get(
                CustomDecorType.TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING
        );

        if (key == null) {
            return null;
        }

        if (!CustomDecorType.matchesTypedKey(key)) {
            return this.types[0];
        }
        
        for (final var type : this.types) {
            if (key.equals(type.getKey().getKey())) {
                return type;
            }
        }
        
        return null;
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getNextType(final @Nullable Interaction interaction) throws UnsupportedOperationException {
        return this.getNextType(this.getTypeOf(interaction));
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getNextType(final @Nullable ItemStack itemStack) throws UnsupportedOperationException {
        return this.getNextType(this.getTypeOf(itemStack));
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getNextType(final @Nullable CustomDecorData.Type<? extends CustomDecorData<?>> type) throws UnsupportedOperationException {
        if (
                !this.isWrenchable()
                && !this.isTyped()
        ) {
            throw new UnsupportedOperationException("This custom decor is not wrenchable or typed!");
        }

        if (type == null) {
            return null;
        }

        final int typesSize = this.types.length;

        for (int i = 0; i < typesSize; ++i) {
            if (this.types[i].equals(type)) {
                return this.types[Math.floorMod(i + 1, typesSize)];
            }
        }

        return null;
    }

    @Override
    public @NotNull @Unmodifiable Map<Facing, CustomDecorData.Type<D>> typeFaceMap() throws UnsupportedOperationException {
        if (!this.isFaceTyped()) {
            throw new UnsupportedOperationException("This custom decor is not face typed!");
        }

        return Collections.unmodifiableMap(this.faceTypeMap);
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getFaceTypeOf(final @Nullable Interaction interaction) throws UnsupportedOperationException {
        if (!this.isFaceTyped()) {
            throw new UnsupportedOperationException("This custom decor is not face typed!");
        }

        return interaction == null
                ? null
                : this.getFaceTypeOf(
                        CustomDecor.fromInteraction(interaction).map(
                                customDecor -> customDecor.getDisplay().getItemStack()
                        ).orElse(null)
                );
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getFaceTypeOf(final @Nullable ItemStack itemStack) throws UnsupportedOperationException {
        if (!this.isFaceTyped()) {
            throw new UnsupportedOperationException("This custom decor is not face typed!");
        }

        if (itemStack == null) {
            return null;
        }

        final String key = itemStack.getItemMeta().getPersistentDataContainer().get(
                CustomDecorType.TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING
        );

        if (key == null) {
            return null;
        }

        if (!CustomDecorType.matchesTypedKey(key)) {
            return this.faceTypeMap.getOrDefault(Facing.FLOOR, null);
        }

        for (final var type : this.faceTypeMap.values()) {
            if (key.equals(type.getKey().getKey())) {
                return type;
            }
        }

        return null;
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getFaceTypeOf(final @Nullable BlockFace blockFace) throws UnsupportedOperationException {
        if (!this.isFaceTyped()) {
            throw new UnsupportedOperationException("This custom decor is not face typed!");
        }

        final Facing facing = Facing.fromBlockFace(blockFace);

        return facing == null
                || !this.facingSet.contains(facing)
                ? null
                : this.faceTypeMap.get(facing);
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getFaceTypeOf(final @Nullable Facing facing) throws UnsupportedOperationException {
        if (!this.isFaceTyped()) {
            throw new UnsupportedOperationException("This custom decor is not face typed!");
        }

        return facing == null
                ? null
                : this.faceTypeMap.getOrDefault(facing, this.faceTypeMap.values().iterator().next());
    }

    @Override
    public @NotNull @Unmodifiable Int2ObjectMap<CustomDecorData.Type<D>> typeLightLevelMap() throws UnsupportedOperationException {
        if (!this.isLightTyped()) {
            throw new UnsupportedOperationException("This custom decor is not light typed!");
        }

        return this.lightLevelTypeMap;
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getLightTypeOf(final @Nullable Interaction interaction) throws UnsupportedOperationException {
        if (!this.isLightTyped()) {
            throw new UnsupportedOperationException("This custom decor is not light typed!");
        }

        return interaction == null
                ? null
                : this.getLightTypeOf(
                        CustomDecor.fromInteraction(interaction)
                        .map(decor -> decor.getDisplay().getItemStack())
                        .orElse(null)
                );
    }

    @Contract("null -> null")
    @Override
    public @Nullable CustomDecorData.Type<D> getLightTypeOf(final @Nullable ItemStack itemStack) throws UnsupportedOperationException {
        if (!this.isLightTyped()) {
            throw new UnsupportedOperationException("This custom decor is not light typed!");
        }

        if (itemStack == null) {
            return null;
        }

        final String key = itemStack.getItemMeta().getPersistentDataContainer().get(
                CustomDecorType.TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING
        );

        if (key == null) {
            return null;
        }

        if (!CustomDecorType.matchesTypedKey(key)) {
            return this.lightLevelTypeMap.getOrDefault(this.lightLevels[0], null);
        }

        for (final var type : this.lightLevelTypeMap.values()) {
            if (key.equals(type.getKey().getKey())) {
                return type;
            }
        }

        return null;
    }

    @Override
    public @Nullable CustomDecorData.Type<D> getLightTypeOf(final int lightLevel) throws UnsupportedOperationException {
        if (!this.isLightTyped()) {
            throw new UnsupportedOperationException("This custom decor is not light typed!");
        }

        return lightLevel < 0 || lightLevel > 15
                ? null
                : this.lightLevelTypeMap.getOrDefault(lightLevel, null);
    }

    @Override
    public int @NotNull [] lightLevels() throws UnsupportedOperationException {
        if (
                !this.isLightable()
                && !this.isLightTyped()
        ) {
            throw new UnsupportedOperationException("This custom decor is not lightable or light typed!");
        }

        return this.lightLevels.clone();
    }

    @Override
    public int getLightLevelOf(final @Nullable Interaction interaction) throws UnsupportedOperationException {
        return interaction != null
                && interaction.getWorld().getBlockAt(interaction.getLocation()).getBlockData() instanceof Light light
                ? light.getLevel()
                : -1;
    }

    @Override
    public int getLightLevelOf(final @Nullable ItemStack itemStack) throws UnsupportedOperationException {
        if (!this.isLightTyped()) {
            throw new UnsupportedOperationException("This custom decor is not light typed!");
        }

        if (itemStack == null) {
            return this.lightLevels[0];
        }

        final String key = itemStack.getItemMeta().getPersistentDataContainer().get(
                CustomDecorType.TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING
        );

        if (!CustomDecorType.matchesTypedKey(key)) {
            return this.lightLevels[0];
        }

        for (final var entry : this.lightLevelTypeMap.int2ObjectEntrySet()) {
            if (key.equals(entry.getValue().getKey().getKey())) {
                return entry.getIntKey();
            }
        }

        return this.lightLevels[0];
    }

    @Override
    public int getNextLightLevel(final @Nullable Interaction interaction) throws UnsupportedOperationException {
        return this.getNextLightLevel(this.getLightLevelOf(interaction));
    }

    @Override
    public int getNextLightLevel(final @Nullable ItemStack itemStack) throws UnsupportedOperationException {
        return this.getNextLightLevel(this.getLightLevelOf(itemStack));
    }

    @Override
    public int getNextLightLevel(final int lightLevel) throws UnsupportedOperationException {
        if (
                !this.isLightable()
                && !this.isLightTyped()
        ) {
            throw new UnsupportedOperationException("This custom decor is not lightable or light typed!");
        }

        final int levelsSize = this.lightLevels.length;

        for (int i = 0; i < levelsSize; ++i) {
            if (this.lightLevels[i] == lightLevel) {
                return this.lightLevels[Math.floorMod(i + 1, levelsSize)];
            }
        }

        return this.lightLevels[0];
    }

    @Override
    public double getSitHeight() throws UnsupportedOperationException {
        if (!this.isSittable()) {
            throw new UnsupportedOperationException("This custom decor is not sittable!");
        }

        return this.sitHeight;
    }

    @Override
    public @NotNull DecorClickAction getClickAction() {
        return this.clickAction;
    }

    @Override
    public @NotNull DecorPlaceAction getPlaceAction() {
        return this.placeAction;
    }

    @Override
    public @NotNull DecorBreakAction getBreakAction() {
        return this.breakAction;
    }

    @Override
    public boolean hasParameters(
            final @NotNull DecorParameter first,
            final DecorParameter @NotNull ... rest
    ) {
        return this.parameterSet.contains(first)
                && (
                        rest.length == 0
                        || this.parameterSet.containsAll(Arrays.asList(rest))
                );
    }

    @Override
    public final boolean isSimilar(final @Nullable ItemStack itemStack) {
        if (
                itemStack == null
                || itemStack.getType() != this.itemStack.getType()
                || !itemStack.hasItemMeta()
        ) {
            return false;
        }

        final ItemMeta thisMeta = this.itemStack.getItemMeta();
        final ItemMeta thatMeta = itemStack.getItemMeta();

        return thisMeta.hasCustomModelData()
                && thatMeta.hasCustomModelData()
                && thatMeta.getCustomModelData() == thatMeta.getCustomModelData();
    }

    @Override
    public final boolean isSimilar(final @Nullable CustomDecorData<? extends CustomDecorData<?>> customDecorData) {
        return customDecorData != null
                && (
                        customDecorData == this
                        || this.isSimilar(customDecorData.getItem())
                );
    }

    @Override
    public boolean isPaintable() {
        return this.parameterSet.contains(DecorParameter.PAINTABLE);
    }

    @Override
    public boolean isSittable() {
        return this.parameterSet.contains(DecorParameter.SITTABLE);
    }

    @Override
    public boolean isWrenchable() {
        return this.parameterSet.contains(DecorParameter.WRENCHABLE);
    }

    @Override
    public boolean isLightable() {
        return this.parameterSet.contains(DecorParameter.LIGHTABLE);
    }

    @Override
    public boolean isTyped() {
        return this.parameterSet.contains(DecorParameter.TYPED);
    }

    @Override
    public boolean isLightTyped() {
        return this.parameterSet.contains(DecorParameter.LIGHT_TYPED);
    }

    @Override
    public boolean isFaceTyped() {
        return this.parameterSet.contains(DecorParameter.FACE_TYPED);
    }

    @Override
    public boolean isAnyTyped() {
        return this.isWrenchable()
                || this.isTyped()
                || this.isLightTyped()
                || this.isFaceTyped();
    }

    @Override
    public boolean isDropType() {
        return this.isDropType;
    }

    @Override
    public void doClickAction(
            final @NotNull WhoMine plugin,
            final @NotNull CustomDecorClickEvent event
    ) {
        if (this.clickAction.isSet()) {
            this.clickAction.execute(plugin, event);
        }
    }

    @Override
    public void doPlaceAction(
            final @NotNull WhoMine plugin,
            final @NotNull CustomDecorPlaceEvent event
    ) {
        if (this.placeAction.isSet()) {
            this.placeAction.execute(plugin, event);
        }
    }

    @Override
    public void doBreakAction(
            final @NotNull WhoMine plugin,
            final @NotNull CustomDecorBreakEvent event
    ) {
        if (this.breakAction.isSet()) {
            this.breakAction.execute(plugin, event);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerRecipes(final @NotNull WhoMine plugin) {
        if (this.recipeFunctions == null) {
            return;
        }

        for (final var function : this.recipeFunctions) {
            final var entry = function.apply((D) this);
            final boolean registerInMenu = entry.isRegisteredInMenu();
            final Recipe recipe;

            if (entry.isBuildable()) {
                final var recipeBuilder = entry.getBuilder();

                if (recipeBuilder.namespacedKey() == null) {
                    recipeBuilder.namespacedKey(this.namespacedKey);
                }

                if (recipeBuilder.result() == null) {
                    recipeBuilder.result(this.itemStack);
                }

                recipe = recipeBuilder.build();
            } else {
                recipe = entry.getRecipe();
            }

            this.recipeEntries.add(
                    RecipeEntry.fromRecipe(
                            recipe,
                            registerInMenu
                    )
            );
            plugin.getServer().addRecipe(recipe);

            if (registerInMenu) {
                plugin.getCache().customDecorRecipes.add(recipe);
            }
        }
    }

    @Override
    public final void unregisterRecipes(final @NotNull WhoMine plugin) {
        if (this.recipeEntries.isEmpty()) {
            return;
        }

        for (final var entry : this.recipeEntries) {
            final Keyed recipe = (Keyed) entry.getRecipe();
            final boolean isRegisteredInMenu = entry.isRegisteredInMenu();

            plugin.getServer().removeRecipe(recipe.getKey());

            if (isRegisteredInMenu) {
                plugin.getCache().customDecorRecipes.remove(recipe);
            }
        }
    }

    @Override
    public void place(
            final @NotNull WhoMine plugin,
            final @NotNull MSPosition position,
            final @NotNull Player player,
            final @NotNull BlockFace blockFace,
            final @Nullable EquipmentSlot hand,
            final @Nullable Component customName
    ) throws IllegalArgumentException {
        final CraftWorld world = (CraftWorld) position.world();

        if (world == null) {
            throw new IllegalArgumentException("The world of the position cannot be null!");
        }

        final Material blockType = position.getBlock().getType();
        final float rotation = player.getYaw();
        BlockFace finalFace = null;

        if (
                !this.hitBox.getType().isNone()
                && !BlockUtils.isReplaceable(blockType)
                && !blockType.isAir()
        ) {
            for (final var facing : this.facingSet) {
                if (facing.hasFace(blockFace)) {
                    finalFace = blockFace;
                    break;
                }
            }
        } else {
            final BlockFace rotationFace = LocationUtils.degreesToBlockFace90(rotation);

            for (final var facing : this.facingSet) {
                if (
                        facing.hasFace(
                                position,
                                rotation
                        )
                ) {
                    finalFace = switch (facing) {
                        case WALL -> rotationFace;
                        case FLOOR -> BlockFace.UP;
                        case CEILING -> BlockFace.DOWN;
                    };
                    break;
                }
            }
        }

        if (finalFace == null) {
            return;
        }

        final ServerLevel serverLevel = world.getHandle();
        final MSBoundingBox msbb = this.hitBox.getBoundingBox(position, finalFace, rotation);
        final BlockPos[] blocksToReplace = msbb.getBlockPositions();
        final var blockStates = new ObjectArrayList<org.bukkit.block.BlockState>();

        for (final var blockPos : blocksToReplace) {
            final BlockState blockState = serverLevel.getBlockState(blockPos);

            if (BlockUtils.isReplaceable(blockState.getBlock())) {
                final BlockEntity tileEntity = serverLevel.getBlockEntity(blockPos);
                final CraftBlockState craftState = CraftBlockStates.getBlockState(null, blockPos, blockState, tileEntity);

                blockStates.add(craftState);
            }
        }

        if (
                this.hasEntitiesInside(
                        serverLevel,
                        msbb.max(msbb.max().offset(1.0d))
                )
        ) {
            return;
        }

        final ItemStack itemInHand = hand != null
                ? player.getInventory().getItem(hand)
                : this.itemStack;

        if (customName != null) {
            final ItemMeta itemMeta = itemInHand.getItemMeta();

            itemMeta.displayName(customName);
            itemInHand.setItemMeta(itemMeta);
        }

        final CustomDecorPlaceEvent event = new CustomDecorPlaceEvent(
                this.placeInWorld(
                        player.getName(),
                        this.summonItem(position.yaw(rotation), finalFace, itemInHand),
                        msbb,
                        blocksToReplace,
                        finalFace,
                        rotation
                ),
                player,
                hand == null
                        ? EquipmentSlot.HAND
                        : hand,
                blockStates
        );

        player.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            event.getCustomDecor().destroy(plugin, player, false);

            for (final var replacedBlock : event.getReplacedBlocks()) {
                replacedBlock.setType(replacedBlock.getType());
                replacedBlock.setBlockData(replacedBlock.getBlockData());
            }

            return;
        }

        if (hand != null) {
            final int currentAmount = itemInHand.getAmount();

            itemInHand.setAmount(
                    player.getGameMode() == GameMode.SURVIVAL
                            ? currentAmount - 1
                            : currentAmount
            );
            player.swingHand(hand);
        }

        this.getSoundGroup().playPlaceSound(position.center());
        this.doPlaceAction(plugin, event);
    }

    static @NotNull ItemStack copyMetaForTypeItem(
            final @NotNull ItemStack typeItem,
            final @NotNull ItemStack item
    ) {
        final ItemMeta typeMeta = typeItem.getItemMeta();
        final ItemMeta itemMeta = item.getItemMeta();

        if (
                itemMeta instanceof final LeatherArmorMeta colorable
                && typeMeta instanceof final LeatherArmorMeta typeColorable
        ) {
            typeColorable.setColor(colorable.getColor());
        }

        typeMeta.displayName(itemMeta.displayName());
        typeMeta.lore(itemMeta.lore());
        typeItem.setItemMeta(typeMeta);

        return typeItem;
    }

    private @NotNull CustomDecor placeInWorld(
            final @NotNull String placerName,
            final @NotNull ItemDisplay itemDisplay,
            final @NotNull MSBoundingBox boundingBox,
            final BlockPos @NotNull [] replacePositions,
            final @NotNull BlockFace blockFace,
            final float rotation
    ) {
        final Interaction[] interactions = this.fillInteractions(
                itemDisplay,
                boundingBox,
                this.hitBox.getVectorInBlock(blockFace, rotation),
                blockFace
        );
        final DecorHitBox.Type type = this.hitBox.getType();

        if (!type.isNone()) {
            final var blocks = fillBlocks(
                    placerName,
                    ((CraftWorld) itemDisplay.getWorld()).getHandle(),
                    replacePositions,
                    type.getNMSMaterial().defaultBlockState(),
                    null
            );

            if (
                    this.isLightable()
                    || this.isLightTyped()
            ) {
                final int lightLevel =
                        this.isLightTyped()
                        ? this.getLightLevelOf(itemDisplay.getItemStack())
                        : this.lightLevels[0];

                for (final var currentBlock : blocks) {
                    if (currentBlock.getBlockData() instanceof final Light light) {
                        light.setLevel(lightLevel);
                        currentBlock.setBlockData(light);
                    }
                }
            }
        }

        return new CustomDecor(this, itemDisplay, interactions, boundingBox);
    }

    private @NotNull ItemDisplay summonItem(
            final @NotNull MSPosition position,
            final @NotNull BlockFace blockFace,
            final @NotNull ItemStack item
    ) {
        final World world = position.world();

        if (world == null) {
            throw new IllegalArgumentException("The world of the position cannot be null!");
        }

        final ItemStack itemStack = item.clone();
        final CustomDecorData.Type<D> type;

        itemStack.setAmount(1);

        if (this.isLightTyped()) {
            type = this.getLightTypeOf(itemStack);
        } else if (this.isFaceTyped()) {
            type = this.getFaceTypeOf(blockFace);
        } else if (this.isWrenchable()) {
            type = this.getTypeOf(itemStack);
        } else {
            type = null;
        }

        return world.spawn(
                position.center()
                .offset(
                        this.hitBox.getModelOffsetX(),
                        this.hitBox.getModelOffsetY(),
                        this.hitBox.getModelOffsetZ()
                )
                .yaw(
                        this.hitBox.getX() > 1.0d || this.hitBox.getZ() > 1.0d
                        || this.hitBox.getX() < -1.0d || this.hitBox.getZ() < -1.0d
                                ? LocationUtils.to90(position.yaw()) + 180.0f
                                : LocationUtils.to45(position.yaw()) + 180.0f
                )
                .toLocation(),
                ItemDisplay.class,
                itemDisplay -> {
                    itemDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.NONE);

                    if (type == null) {
                        itemDisplay.setItemStack(itemStack);
                    } else {
                        itemDisplay.setItemStack(
                                copyMetaForTypeItem(type.getItem(), itemStack)
                        );
                    }

                    itemDisplay.setDisplayHeight(1.0f);
                    itemDisplay.setDisplayWidth(1.0f);
                });
    }

    private Interaction @NotNull [] fillInteractions(
            final @NotNull ItemDisplay itemDisplay,
            final @NotNull MSBoundingBox boundingBox,
            final @NotNull MSVector offset,
            final @NotNull BlockFace blockFace
    ) {
        final World world = itemDisplay.getWorld();
        final BlockPos[] spawnPositions = this.getSpawnPositions(boundingBox, blockFace);
        final int positionsSize = spawnPositions.length;
        final Interaction[] interactions = new Interaction[positionsSize];

        final double offsetX = offset.x();
        final double offsetY = offset.y();
        final double offsetZ = offset.z();

        final float width = this.hitBox.getInteractionWidth();
        final float height = this.hitBox.getInteractionHeight(blockFace);

        final Consumer<Interaction> interactionConsumer =
                interaction -> {
                    interaction.setInteractionHeight(height);
                    interaction.setInteractionWidth(width);
                    interaction.setResponsive(false);
                };

        for (int i = 0; i < positionsSize; ++i) {
            final BlockPos blockPos = spawnPositions[i];
            interactions[i] = world.spawn(
                    new Location(
                            null,
                            blockPos.getX() + offsetX,
                            blockPos.getY() + offsetY,
                            blockPos.getZ() + offsetZ
                    ),
                    Interaction.class,
                    interactionConsumer
            );
        }

        this.processInteractions(itemDisplay, interactions, boundingBox);

        return interactions;
    }

    private BlockPos @NotNull [] getSpawnPositions(
            final @NotNull MSBoundingBox boundingBox,
            final @NotNull BlockFace blockFace
    ) {
        final boolean isCeiling =
                this.facingSet.contains(Facing.CEILING)
                && blockFace == BlockFace.DOWN;

        return boundingBox.getBlockPositions(
                0,
                isCeiling
                        ? (int) (this.hitBox.getY() - 1)
                        : 0,
                0,
                0,
                isCeiling
                        ? 0
                        : (int) (-this.hitBox.getY() + 1),
                0
        );
    }

    private boolean hasEntitiesInside(
            final @NotNull ServerLevel serverLevel,
            final @NotNull MSBoundingBox searchBox
    ) {
        switch (this.hitBox.getType()) {
            case NONE -> {
                final AtomicInteger decorCounter = new AtomicInteger();

                if (
                        searchBox.hasNMSEntity(
                                serverLevel,
                                entity ->
                                        entity instanceof net.minecraft.world.entity.Interaction
                                        && decorCounter.incrementAndGet() >= MAX_DECORATIONS_IN_BLOCK
                        )
                ) {
                    return true;
                }
            }
            case SOLID ->{
                return searchBox.hasNMSEntity(
                        serverLevel,
                        entity -> !BlockUtils.isIgnorableEntity(entity.getType())
                );
            }
            case LIGHT -> {
                return searchBox.hasNMSEntity(
                        serverLevel,
                        entity -> entity instanceof net.minecraft.world.entity.Interaction
                );
            }
        }

        return false;
    }

    private void processInteractions(
            final @NotNull ItemDisplay display,
            final Interaction @NotNull [] interactions,
            final @NotNull MSBoundingBox msbb
    ) {
        final Interaction firstInteraction = interactions[0];
        final String firstUUID = firstInteraction.getUniqueId().toString();
        final StringBuilder uuidsBuilder = new StringBuilder();

        for (int i = 1; i < interactions.length; ++i) {
            final Interaction interaction = interactions[i];

            uuidsBuilder
            .append(interaction.getUniqueId())
            .append(',');
            interaction.getPersistentDataContainer().set(
                    DecorHitBox.HITBOX_CHILD_NAMESPACED_KEY,
                    PersistentDataType.STRING,
                    firstUUID
            );
        }

        final PersistentDataContainer firstContainer = firstInteraction.getPersistentDataContainer();

        firstContainer.set(
                CustomDecorType.TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING,
                this.namespacedKey.getKey()
        );

        firstContainer.set(
                DecorHitBox.HITBOX_DISPLAY_NAMESPACED_KEY,
                PersistentDataType.STRING,
                display.getUniqueId().toString()
        );

        if (!uuidsBuilder.isEmpty()) {
            firstContainer.set(
                    DecorHitBox.HITBOX_INTERACTIONS_NAMESPACED_KEY,
                    PersistentDataType.STRING,
                    uuidsBuilder
                    .deleteCharAt(uuidsBuilder.length() - 1)
                    .toString()
            );
        }

        firstContainer.set(
                DecorHitBox.HITBOX_BOUNDING_BOX_NAMESPACED_KEY,
                PersistentDataType.STRING,
                String.join(
                        ",",
                        String.valueOf(msbb.minX()),
                        String.valueOf(msbb.minY()),
                        String.valueOf(msbb.minZ()),
                        String.valueOf(msbb.maxX()),
                        String.valueOf(msbb.maxY()),
                        String.valueOf(msbb.maxZ())
                )
        );
    }

    static @NotNull List<Block> fillBlocks(
            final @NotNull String changerName,
            final @NotNull ServerLevel serverLevel,
            final BlockPos @NotNull [] replacePositions,
            final @NotNull BlockState fillBlockState,
            final @Nullable Predicate<BlockPos> predicate
    ) {
        final var blockList = new ObjectArrayList<Block>();
        final var list = new ObjectArrayList<BlockPos>();
        final Material fillMaterial = fillBlockState.getBukkitMaterial();
        final BlockData fillBlockData = fillBlockState.createCraftBlockData();

        for (final var blockPos : replacePositions) {
            if (
                    predicate != null
                    && !predicate.test(blockPos)
            ) {
                continue;
            }

            final Location location = LocationUtils.nmsToBukkit(blockPos, serverLevel);
            final BlockData replacedData = location.getBlock().getBlockData();
            BlockState blockState = net.minecraft.world.level.block.Block.updateFromNeighbourShapes(fillBlockState, serverLevel, blockPos);

            if (
                    fillBlockData instanceof Light light
                    && replacedData instanceof Levelled levelled
                    && levelled.getLevel() == levelled.getMinimumLevel()
            ) {
                light.setWaterlogged(true);

                blockState = ((CraftBlockData) light).getState();
            } else if (blockState.isAir()) {
                blockState = fillBlockState;
            }

            CoreProtectUtils.logRemoval(
                    changerName,
                    location,
                    replacedData.getMaterial(),
                    replacedData
            );
            serverLevel.setBlock(blockPos, blockState, 2);
            list.add(blockPos.immutable());

            final Block newBlock = location.getBlock();

            blockList.add(newBlock);
            CoreProtectUtils.logPlacement(
                    changerName,
                    location,
                    fillMaterial,
                    newBlock.getBlockData()
            );
        }

        for (final var blockPos : list) {
            serverLevel.blockUpdated(
                    blockPos,
                    serverLevel.getBlockState(blockPos).getBlock()
            );
        }

        return blockList;
    }

    private static @NotNull ItemStack setTypeKey(
            final @NotNull ItemStack itemStack,
            final @NotNull String typeKey
    ) {
        final ItemMeta meta = itemStack.getItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(
                CustomDecorType.TYPE_NAMESPACED_KEY,
                PersistentDataType.STRING,
                typeKey
        );
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    /**
     * Builder for {@link CustomDecorDataImpl}
     */
    public final class Builder {
        private NamespacedKey namespacedKey;
        private DecorHitBox hitBox;
        private EnumSet<Facing> facingSet;
        private ItemStack itemStack;
        private SoundGroup soundGroup;
        private Function<D, RecipeEntry>[] recipeFunctions;
        private EnumSet<DecorParameter> parameterSet;
        private double sitHeight;
        private CustomDecorData.Type<D>[] types;
        private EnumMap<Facing, CustomDecorData.Type<D>> faceTypeMap;
        private int[] lightLevels;
        private Int2ObjectMap<CustomDecorData.Type<D>> lightLevelTypeMap;
        private DecorClickAction clickAction;
        private DecorPlaceAction placeAction;
        private DecorBreakAction breakAction;
        private boolean dropsType;

        public Builder() {
            this.sitHeight = Double.NaN;
            this.clickAction = DecorClickAction.NONE;
            this.placeAction = DecorPlaceAction.NONE;
            this.breakAction = DecorBreakAction.NONE;
        }

        public @NotNull CustomDecorDataImpl<D> build() throws IllegalStateException {
            return new CustomDecorDataImpl<>() {

                @Override
                protected @NotNull Builder builder() {
                    return Builder.this;
                }
            };
        }

        public @NotNull CustomDecorDataImpl<D> build(final @NotNull WhoMine plugin) throws IllegalStateException {
            return new CustomDecorDataImpl<>(plugin) {

                @Override
                protected @NotNull Builder builder(final @NotNull WhoMine plugin) {
                    return Builder.this;
                }
            };
        }

        public @NotNull Builder preBuild() throws IllegalStateException {
            if (this.namespacedKey == null) {
                throw new IllegalStateException("Key is not set!");
            }

            if (this.hitBox == null) {
                throw new IllegalStateException("Hit box is not set!");
            }

            if (this.facingSet.isEmpty()) {
                throw new IllegalStateException("Facings is not set!");
            }

            if (this.itemStack == null) {
                throw new IllegalStateException("Item stack is not set!");
            }

            if (this.soundGroup == null) {
                throw new IllegalStateException("Sound group is not set!");
            }

            if (this.parameterSet != null) {
                if (
                        this.isPaintable()
                        || this.isWrenchable()
                ) {
                    final ItemMeta meta = this.itemStack.getItemMeta();
                    final var currentLore = meta.lore();
                    final var newLore = new ObjectArrayList<Component>();

                    if (this.isPaintable()) {
                        newLore.add(Font.Components.PAINTABLE);
                    }

                    if (this.isWrenchable()) {
                        newLore.add(Font.Components.WRENCHABLE);
                    }

                    if (currentLore != null) {
                        newLore.add(Component.empty());
                        newLore.addAll(currentLore);
                    }

                    meta.lore(newLore);
                    this.itemStack.setItemMeta(meta);
                }

                if (
                        this.isSittable()
                        && this.sitHeight != this.sitHeight
                ) {
                    throw new IllegalStateException("Sit height is not set, but sittable parameter is set!");
                }

                if (
                        this.isWrenchable()
                        && this.types == null
                ) {
                    throw new IllegalStateException("Types are not set, but wrenchable parameter is set!");
                }

                if (
                        this.isTyped()
                        && this.types == null
                ) {
                    throw new IllegalStateException("Types are not set, but typed parameter is set!");
                }

                if (
                        this.isLightable()
                        && this.lightLevels == null
                ) {
                    throw new IllegalStateException("Light levels are not set, but lightable parameter is set!");
                }

                if (
                        (
                                this.isLightable()
                                || this.isLightTyped()
                        )
                        && !this.hitBox.getType().isLight()

                ) {
                    throw new IllegalStateException("Lightable or light typed parameter is set, but hit box type is not light!");
                }

                if (this.isLightTyped()) {
                    if (this.lightLevels == null) {
                        throw new IllegalStateException("Light levels are not set, but light typed parameter is set!");
                    }

                    if (
                            this.lightLevelTypeMap == null
                            || this.lightLevelTypeMap.isEmpty()
                    ) {
                        throw new IllegalStateException("Light level type map is not set, but light typed parameter is set!");
                    }
                }

                if (
                        this.isFaceTyped()
                        && (
                                this.faceTypeMap == null
                                || this.faceTypeMap.isEmpty()
                        )
                ) {
                    throw new IllegalStateException("Face type map is not set, but face typed parameter is set!");
                }
            } else {
                this.parameterSet = EnumSet.noneOf(DecorParameter.class);
            }

            if (!this.clickAction.isSet()) {
                if (
                        this.isWrenchable()
                        && this.isSittable()
                ) {
                    this.clickAction = DecorParameter.wrenchableSittableAction();
                } else if (
                        this.isWrenchable()
                        && this.isLightable()
                ) {
                    this.clickAction = DecorParameter.wrenchableLightableAction();
                } else {
                    for (final var parameter : this.parameterSet) {
                        if (parameter.getClickAction().isSet()) {
                            this.clickAction = parameter.getClickAction();
                            break;
                        }
                    }
                }
            }

            return this;
        }

        public @UnknownNullability NamespacedKey key() {
            return this.namespacedKey;
        }

        public @NotNull Builder key(final @Key @NotNull String key) throws InvalidRegexException {
            Key.Validator.validate(key);

            this.namespacedKey = new NamespacedKey(SharedConstants.MSDECOR_NAMESPACE, key);

            return this;
        }

        public @UnknownNullability DecorHitBox hitBox() {
            return this.hitBox;
        }

        public @NotNull Builder hitBox(final @NotNull DecorHitBox hitBox) {
            this.hitBox = hitBox;

            return this;
        }

        public @NotNull @Unmodifiable Set<Facing> facings() {
            return Collections.unmodifiableSet(this.facingSet);
        }

        public @NotNull Builder facings(
                final @NotNull Facing first,
                final Facing @NotNull ... rest
        ) {
            this.facingSet = EnumSet.of(first, rest);

            return this;
        }

        public @UnknownNullability SoundGroup soundGroup() {
            return this.soundGroup;
        }

        public @NotNull Builder soundGroup(final @NotNull SoundGroup soundGroup) {
            this.soundGroup = soundGroup;

            return this;
        }

        public @UnknownNullability ItemStack itemStack() {
            return this.itemStack;
        }

        public @NotNull Builder itemStack(final @NotNull ItemStack itemStack) throws IllegalStateException {
            if (this.namespacedKey == null) {
                throw new IllegalStateException("Key is not set! Set key before setting item stack!");
            }

            this.itemStack = setTypeKey(itemStack, this.namespacedKey.getKey());

            return this;
        }

        public Function<D, RecipeEntry> @UnknownNullability [] recipes() {
            return this.recipeFunctions.clone();
        }

        @SuppressWarnings("unchecked")
        @SafeVarargs
        public final @NotNull Builder recipes(
                final @NotNull Function<D, RecipeEntry> first,
                final Function<D, RecipeEntry> @NotNull ... rest
        ) {
            final int length = rest.length + 1;
            this.recipeFunctions = (Function<D, RecipeEntry>[]) Array.newInstance(Function.class, length);

            System.arraycopy(rest, 0, this.recipeFunctions, 1, rest.length);
            this.recipeFunctions[0] = first;

            return this;
        }

        public @UnknownNullability EnumSet<DecorParameter> parameterSet() {
            return this.parameterSet;
        }

        public @NotNull Builder parameters(
                final @NotNull DecorParameter first,
                final DecorParameter @NotNull ... rest
        ) throws IllegalArgumentException {
            final var parameters = EnumSet.of(first, rest);

            if (
                    parameters.contains(DecorParameter.TYPED)
                    && parameters.contains(DecorParameter.WRENCHABLE)
            ) {
                throw new IllegalArgumentException("Typed and wrenchable parameters cannot be set together!");
            }

            if (
                    parameters.contains(DecorParameter.TYPED)
                    && parameters.contains(DecorParameter.LIGHT_TYPED)
            ) {
                throw new IllegalArgumentException("Typed and sittable parameters cannot be set together!");
            }

            if (
                    parameters.contains(DecorParameter.TYPED)
                    && parameters.contains(DecorParameter.FACE_TYPED)
            ) {
                throw new IllegalArgumentException("Typed and face typed parameters cannot be set together!");
            }

            if (
                    parameters.contains(DecorParameter.WRENCHABLE)
                    && parameters.contains(DecorParameter.LIGHT_TYPED)
            ) {
                throw new IllegalArgumentException("Wrenchable and light typed parameters cannot be set together!");
            }

            if (
                    parameters.contains(DecorParameter.WRENCHABLE)
                    && parameters.contains(DecorParameter.FACE_TYPED)
            ) {
                throw new IllegalArgumentException("Wrenchable and face typed parameters cannot be set together!");
            }

            if (
                    parameters.contains(DecorParameter.LIGHT_TYPED)
                    && parameters.contains(DecorParameter.FACE_TYPED)
            ) {
                throw new IllegalArgumentException("Light typed and face typed parameters cannot be set together!");
            }

            if (
                    parameters.contains(DecorParameter.LIGHT_TYPED)
                    && parameters.contains(DecorParameter.LIGHTABLE)
            ) {
                throw new IllegalArgumentException("Light typed and lightable parameters cannot be set together!");
            }

            this.parameterSet = parameters;

            return this;
        }

        public double sitHeight() {
            return this.sitHeight;
        }

        public @NotNull Builder sitHeight(final @Range(from = -9, to = 9) double sitHeight) throws IllegalStateException, IllegalArgumentException {
            this.validateParam(
                    DecorParameter.SITTABLE,
                    "Set sittable parameter before setting sit height!"
            );

            if (
                    sitHeight < -SharedConstants.SIT_RANGE
                    || sitHeight > SharedConstants.SIT_RANGE
            ) {
                throw new IllegalArgumentException(
                        "Sit height '" + sitHeight + "' is not in range [-" + SharedConstants.SIT_RANGE + ", " + SharedConstants.SIT_RANGE + "]!"
                );
            }

            this.sitHeight = sitHeight;

            return this;
        }

        public CustomDecorData.Type<D> @UnknownNullability [] types() {
            return this.types;
        }

        @SuppressWarnings("unchecked")
        @SafeVarargs
        public final @NotNull Builder types(
                final @NotNull Function<Builder, CustomDecorData.Type<D>> first,
                final Function<Builder, CustomDecorData.Type<D>> @NotNull ... rest
        ) {
            this.validateAnyOfParams(
                    "Set wrenchable or typed parameter before setting types!",
                    DecorParameter.WRENCHABLE, DecorParameter.TYPED
            );

            final var firstType = first.apply(this);
            final CustomDecorData.Type<D>[] restTypes = (CustomDecorData.Type<D>[]) new CustomDecorData.Type<?>[rest.length];

            for (int i = 0; i < rest.length; ++i) {
                restTypes[i] = rest[i].apply(this);
            }

            return this.types(firstType,  restTypes);
        }

        @SuppressWarnings("unchecked")
        @SafeVarargs
        public final @NotNull Builder types(
                final @NotNull CustomDecorData.Type<D> first,
                final CustomDecorData.Type<D> @NotNull ... rest
        ) throws IllegalStateException {
            this.validateAnyOfParams(
                    "Set wrenchable or typed parameter before setting types!",
                    DecorParameter.WRENCHABLE, DecorParameter.TYPED
            );

            if (this.itemStack == null) {
                throw new IllegalStateException("Item stack is not set! Set item stack before setting wrench types!");
            }

            final int restLength = rest.length;
            this.types = (CustomDecorData.Type<D>[]) new CustomDecorData.Type<?>[restLength + 2];

            if (restLength != 0) {
                System.arraycopy(rest, 0, this.types, 2, restLength);
            }

            this.types[0] =
                    new Type(
                            this,
                            "default",
                            this.itemStack
                    );
            this.types[1] = first;

            return this;
        }

        public @UnknownNullability EnumMap<Facing, CustomDecorData.Type<D>> faceTypeMap() {
            return this.faceTypeMap;
        }

        @SuppressWarnings("unchecked")
        @SafeVarargs
        public final @NotNull Builder faceTypes(
                final @NotNull Function<Builder, Map.Entry<Facing, CustomDecorData.Type<D>>> first,
                final Function<Builder, Map.Entry<Facing, CustomDecorData.Type<D>>> @NotNull ... rest
        ) {
            this.validateParam(
                    DecorParameter.FACE_TYPED,
                    "Set face typed parameter before setting face type map!"
            );

            final var firstType = first.apply(this);
            final var restTypes = (Map.Entry<Facing, CustomDecorData.Type<D>>[]) new Map.Entry<?, ?>[rest.length];

            for (int i = 0; i < rest.length; ++i) {
                restTypes[i] = rest[i].apply(this);
            }

            return this.faceTypes(firstType, restTypes);
        }

        @SafeVarargs
        public final @NotNull Builder faceTypes(
                final @NotNull Map.Entry<Facing, CustomDecorData.Type<D>> first,
                final Map.Entry<Facing, CustomDecorData.Type<D>> @NotNull ... rest
        ) throws IllegalStateException {
            this.validateParam(
                    DecorParameter.FACE_TYPED,
                    "Set face typed parameter before setting face type map!"
            );

            this.faceTypeMap = new EnumMap<>(Facing.class);

            this.putFaceType(first.getKey(), first.getValue());

            for (final var entry : rest) {
                this.putFaceType(entry.getKey(), entry.getValue());
            }

            for (final var facing : this.facingSet) {
                if (!this.faceTypeMap.containsKey(facing)) {
                    throw new IllegalStateException("Face type map does not contain type for facing '" + facing + "'!");
                }
            }

            return this;
        }

        public int @UnknownNullability [] lightLevels() {
            return this.lightLevels;
        }

        public @NotNull Builder lightLevels(
                final int first,
                final int @NotNull ... rest
        ) throws IllegalStateException, IllegalArgumentException {
            this.validateAnyOfParams(
                    "Set lightable or light typed parameter before setting light levels!",
                    DecorParameter.LIGHTABLE, DecorParameter.LIGHT_TYPED
            );

            final int length = rest.length + 1;
            this.lightLevels = new int[length];

            System.arraycopy(rest, 0, this.lightLevels, 1, rest.length);
            this.lightLevels[0] = first;

            for (int i = 0; i < length; ++i) {
                final int level = this.lightLevels[i];

                if (
                        level < BlockStateProperties.MIN_LEVEL
                        || level > BlockStateProperties.MAX_LEVEL_15
                ) {
                    throw new IllegalArgumentException(
                            "Light level '" + level + "' is not in range [" + BlockStateProperties.MIN_LEVEL + ", " + BlockStateProperties.MAX_LEVEL_15 + "]!"
                    );
                }

                for (int j = i + 1; j < length; ++j) {
                    if (level == this.lightLevels[j]) {
                        throw new IllegalArgumentException("Light level '" + level + "' is duplicated! Light levels must be unique!");
                    }
                }
            }

            return this;
        }

        public @UnknownNullability Int2ObjectMap<CustomDecorData.Type<D>> lightLevelTypeMap() {
            return this.lightLevelTypeMap;
        }

        @SuppressWarnings("unchecked")
        @SafeVarargs
        public final @NotNull Builder lightLevelTypes(
                final @NotNull Function<Builder, Map.Entry<Integer, CustomDecorData.Type<D>>> first,
                final Function<Builder, Map.Entry<Integer, CustomDecorData.Type<D>>> @NotNull ... rest
        ) {
            this.validateParam(
                    DecorParameter.LIGHT_TYPED,
                    "Set light typed parameter before setting light level type map!"
            );

            final var firstType = first.apply(this);
            final var restTypes = (Map.Entry<Integer, CustomDecorData.Type<D>>[]) new Map.Entry<?, ?>[rest.length];

            for (int i = 0; i < rest.length; ++i) {
                restTypes[i] = rest[i].apply(this);
            }

            return this.lightLevelTypes(firstType, restTypes);
        }

        @SafeVarargs
        public final @NotNull Builder lightLevelTypes(
                final @NotNull Map.Entry<Integer, CustomDecorData.Type<D>> first,
                final Map.Entry<Integer, CustomDecorData.Type<D>> @NotNull ... rest
        ) throws IllegalStateException {
            this.validateParam(
                    DecorParameter.LIGHT_TYPED,
                    "Set light typed parameter before setting light level type map!"
            );

            this.lightLevelTypeMap = new Int2ObjectOpenHashMap<>();

            this.putLightLevelType(first.getKey(), first.getValue());

            for (final var entry : rest) {
                this.putLightLevelType(entry.getKey(), entry.getValue());
            }

            if (this.lightLevelTypeMap.size() != this.lightLevels.length) {
                throw new IllegalStateException("Light level type map size is not equal to light levels size!");
            }

            return this;
        }

        public @UnknownNullability DecorClickAction clickAction() {
            return this.clickAction;
        }

        public @NotNull Builder clickAction(final @NotNull DecorClickAction rightClickAction) {
            this.clickAction = rightClickAction;
            return this;
        }

        public @UnknownNullability DecorPlaceAction placeAction() {
            return this.placeAction;
        }

        public @NotNull Builder placeAction(final @NotNull DecorPlaceAction placeAction) {
            this.placeAction = placeAction;
            return this;
        }

        public @UnknownNullability DecorBreakAction breakAction() {
            return this.breakAction;
        }

        public @NotNull Builder breakAction(final @NotNull DecorBreakAction breakAction) {
            this.breakAction = breakAction;

            return this;
        }

        public boolean dropsType() {
            return this.dropsType;
        }

        public @NotNull Builder dropsType(final boolean dropsType) throws IllegalStateException {
            this.validateAnyOfParams(
                    "Drop type can be set only if one of these parameters is set!",
                    DecorParameter.WRENCHABLE,
                    DecorParameter.LIGHT_TYPED,
                    DecorParameter.FACE_TYPED
            );

            this.dropsType = dropsType;
            return this;
        }

        public boolean isPaintable() {
            return this.parameterSet != null
                    && this.parameterSet.contains(DecorParameter.PAINTABLE);
        }

        public boolean isSittable() {
            return this.parameterSet != null
                    && this.parameterSet.contains(DecorParameter.SITTABLE);
        }

        public boolean isWrenchable() {
            return this.parameterSet != null
                    && this.parameterSet.contains(DecorParameter.WRENCHABLE);
        }

        public boolean isLightable() {
            return this.parameterSet != null
                    && this.parameterSet.contains(DecorParameter.LIGHTABLE);
        }

        public boolean isTyped() {
            return this.parameterSet != null
                    && this.parameterSet.contains(DecorParameter.TYPED);
        }

        public boolean isLightTyped() {
            return this.parameterSet != null
                    && this.parameterSet.contains(DecorParameter.LIGHT_TYPED);
        }

        public boolean isFaceTyped() {
            return this.parameterSet != null
                    && this.parameterSet.contains(DecorParameter.FACE_TYPED);
        }

        public boolean isAnyTyped() {
            return this.parameterSet != null
                    && (
                            this.isWrenchable()
                            || this.isTyped()
                            || this.isLightTyped()
                            || this.isFaceTyped()
                    );
        }

        private void putFaceType(
                final @NotNull Facing facing,
                final @NotNull CustomDecorData.Type<D> type
        ) {
            if (this.faceTypeMap == null) {
                throw new IllegalStateException("Face type map is not set! Set face type map before putting face type!");
            }

            if (this.faceTypeMap.containsKey(facing)) {
                throw new IllegalArgumentException("Facing '" + facing + "' is duplicated! Facings must be unique!");
            }

            this.faceTypeMap.put(facing, type);
        }

        private void putLightLevelType(
                final int level,
                final @NotNull CustomDecorData.Type<D> type
        ) {
            if (this.lightLevelTypeMap == null) {
                throw new IllegalStateException("Light level type map is not set! Set light level type map before putting light level type!");
            }

            if (this.lightLevels == null) {
                throw new IllegalStateException("Light levels are not set! Set light levels before putting light level type!");
            }

            if (this.lightLevelTypeMap.containsKey(level)) {
                throw new IllegalArgumentException("Light level '" + level + "' is duplicated! Light levels must be unique!");
            }


            for (final var lightLevel : this.lightLevels) {
                if (level == lightLevel) {
                    this.lightLevelTypeMap.put(level, type);
                    return;
                }
            }

            throw new IllegalArgumentException("Light level '" + level + "' not found in light levels!");
        }

        private void validateParam(
                final @NotNull DecorParameter param,
                final @NotNull String message
        ) throws IllegalStateException {
            if (this.parameterSet == null) {
                throw new IllegalStateException("Parameters are not set! First set parameters!");
            }

            if (!this.parameterSet.contains(param)) {
                throw new IllegalStateException("Parameter '" + param + "' is not set! " + message);
            }
        }

        private void validateAnyOfParams(
                final @NotNull String message,
                final @NotNull DecorParameter first,
                final DecorParameter @NotNull ... rest
        ) throws IllegalStateException {
            if (this.parameterSet == null) {
                throw new IllegalStateException("Parameters are not set! First set parameters!");
            }

            if (this.parameterSet.contains(first)) {
                return;
            }

            for (final var param : rest) {
                if (this.parameterSet.contains(param)) {
                    return;
                }
            }

            final StringBuilder builder = new StringBuilder();

            builder.append(first);

            for (final var param : rest) {
                builder.append(", ").append(param);
            }

            throw new IllegalStateException("Any of parameters : " + builder + " is not set! " + message);
        }
    }

    @Immutable
    protected final class Type implements CustomDecorData.Type<D> {
        private final NamespacedKey namespacedKey;
        private final ItemStack itemStack;

        public Type(
                final @NotNull Builder builder,
                final @Key @NotNull String key,
                final @NotNull ItemStack itemStack
        ) throws InvalidRegexException {
            Key.Validator.validate(key);

            final String typedKey = builder.namespacedKey.getKey() + ".type." + key;
            this.namespacedKey = new NamespacedKey(SharedConstants.MSDECOR_NAMESPACE, typedKey);
            this.itemStack = setTypeKey(itemStack, typedKey);
        }

        @Override
        public @NotNull NamespacedKey getKey() {
            return this.namespacedKey;
        }

        @Override
        public @NotNull ItemStack getItem() {
            return this.itemStack.clone();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 17;

            result = prime * result + this.namespacedKey.hashCode();
            result = prime * result + this.itemStack.hashCode();

            return result;
        }

        @Override
        @Contract("null -> false")
        public boolean equals(final @Nullable Object obj) {
            return obj == this
                    || (
                            obj != null
                            && obj.getClass() == this.getClass()
                            && ((CustomDecorData.Type<?>) obj).getKey().equals(this.getKey())
                    );
        }

        @Override
        public @NotNull String toString() {
            return "Type{" +
                    "namespacedKey=" + this.namespacedKey +
                    ", itemStack=" + this.itemStack +
                    '}';
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull D buildData() {
            final @Subst("key") String key = this.namespacedKey.getKey();

            return (D) CustomDecorDataImpl.this.builder()
                    .key(key)
                    .itemStack(this.itemStack)
                    .preBuild()
                    .build();
        }
    }
}
