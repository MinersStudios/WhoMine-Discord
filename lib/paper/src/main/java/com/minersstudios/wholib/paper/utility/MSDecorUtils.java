package com.minersstudios.wholib.paper.utility;

import com.minersstudios.wholib.annotation.Path;
import com.minersstudios.wholib.annotation.Resource;
import com.minersstudios.wholib.utility.ChatUtils;
import com.minersstudios.wholib.paper.world.location.MSBoundingBox;
import com.minersstudios.wholib.paper.world.location.MSPosition;
import com.minersstudios.wholib.paper.custom.decor.CustomDecorData;
import com.minersstudios.wholib.paper.custom.decor.DecorHitBox;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Utility class for {@link CustomDecorData}
 */
public final class MSDecorUtils {
    public static final String NAMESPACED_KEY_REGEX = '(' + Resource.WMDECOR + "):(" + Path.REGEX + ")";
    public static final Pattern NAMESPACED_KEY_PATTERN = Pattern.compile(NAMESPACED_KEY_REGEX);

    @Contract(" -> fail")
    private MSDecorUtils() throws AssertionError {
        throw new AssertionError("Utility class");
    }

    /**
     * @param key Key of custom decor data
     * @return An optional of {@link ItemStack} object retrieved from a custom
     *         decor data key
     * @see CustomDecorData#fromKey(String)
     * @see CustomDecorData#getItem()
     */
    public static @NotNull Optional<ItemStack> getItemStack(final @Nullable String key) {
        return CustomDecorData
                .fromKey(key)
                .map(CustomDecorData::getItem);
    }

    /**
     * @param clazz Class of custom decor data
     * @return An optional of {@link ItemStack} object retrieved from custom
     *         decor data class
     * @see CustomDecorData#fromClass(Class)
     * @see CustomDecorData#getItem()
     */
    public static @NotNull Optional<ItemStack> getItemStack(final @NotNull Class<? extends CustomDecorData<?>> clazz) {
        return CustomDecorData
                .fromClass(clazz)
                .map(CustomDecorData::getItem);
    }

    /**
     * @param position Position to be checked
     * @return Nearby {@link Interaction} array
     * @throws IllegalArgumentException If a position world is null
     */
    public static Interaction @NotNull [] getNearbyInteractions(final @NotNull MSPosition position) throws IllegalArgumentException {
        final World world = position.world();

        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }

        final var entities = MSBoundingBox.ofSize(position, 1.0d, 1.0d, 1.0d)
                .getNMSEntities(
                        ((CraftWorld) world).getHandle(),
                        entity -> entity instanceof net.minecraft.world.entity.Interaction
                );
        final int size = entities.size();
        final Interaction[] interactions = new Interaction[size];

        for (int i = 0; i < size; ++i) {
            interactions[i] = (Interaction) entities.get(i).getBukkitEntity();
        }

        return interactions;
    }

    /**
     * @param position Position to be checked
     * @return Nearby {@link Interaction} or null if not found
     * @throws IllegalArgumentException If position world is null
     */
    public static @Nullable Interaction getNearbyInteraction(final @NotNull MSPosition position) throws IllegalArgumentException {
        final World world = position.world();

        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }

        final var entities = MSBoundingBox.ofSize(position, 1.0d, 1.0d, 1.0d)
                .getNMSEntities(
                        ((CraftWorld) world).getHandle(),
                        entity -> entity instanceof net.minecraft.world.entity.Interaction
                );
        return entities.isEmpty()
                ? null
                : (Interaction) entities.getFirst().getBukkitEntity();
    }

    /**
     * @param material Material to be checked
     * @return True if material is custom decor material
     *         (barrier, structure_void, light)
     */
    @Contract("null -> false")
    public static boolean isCustomDecorMaterial(final @Nullable Material material) {
        return material == Material.BARRIER
                || material == Material.LIGHT;
    }

    /**
     * @param itemStack Item stack to be checked
     * @return True if item stack is custom decor item stack
     */
    @Contract("null -> false")
    public static boolean isCustomDecor(final @Nullable ItemStack itemStack) {
        return CustomDecorData.fromItemStack(itemStack).isPresent();
    }

    /**
     * @param entity Entity to be checked
     * @return True if entity is custom decor entity
     */
    @Contract("null -> false")
    public static boolean isCustomDecor(final @Nullable net.minecraft.world.entity.Entity entity) {
        return entity != null
                && isCustomDecor(entity.getBukkitEntity());
    }

    /**
     * @param entity Entity to be checked
     * @return True if entity is custom decor entity
     */
    @Contract("null -> false")
    public static boolean isCustomDecor(final @Nullable Entity entity) {
        return entity instanceof final Interaction interaction
                && (
                        DecorHitBox.isChild(interaction)
                        || DecorHitBox.isParent(interaction)
                );
    }

    /**
     * @param block Block to be checked
     * @return True if block is custom decor block
     */
    @Contract("null -> false")
    public static boolean isCustomDecor(final @Nullable Block block) {
        return block != null
                && MSBoundingBox.of(block)
                .hasNMSEntity(
                        ((CraftWorld) block.getWorld()).getHandle(),
                        MSDecorUtils::isCustomDecor
                );
    }

    /**
     * @param string String to be checked
     * @return True if string matches {@link #NAMESPACED_KEY_REGEX}
     */
    @Contract("null -> false")
    public static boolean matchesNamespacedKey(final @Nullable String string) {
        return ChatUtils.isNotBlank(string)
                && NAMESPACED_KEY_PATTERN.matcher(string).matches();
    }
}
