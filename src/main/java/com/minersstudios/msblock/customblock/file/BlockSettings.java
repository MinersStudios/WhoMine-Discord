package com.minersstudios.msblock.customblock.file;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Set;

/**
 * Class representing the settings for a custom block. This class
 * holds information about the block's hardness, the tool rules
 * for breaking the block, and the placing rules for placing the
 * block.
 */
public class BlockSettings {
    private float hardness;
    private final Tool tool;
    private final Placing placing;

    /**
     * Constructs the BlockSettings with the specified hardness,
     * tool, and placing rules
     *
     * @param hardness The hardness value of the custom block
     * @param tool     The Tool rules for the custom block
     * @param placing  The Placing rules for the custom block
     */
    public BlockSettings(
            float hardness,
            @NotNull Tool tool,
            @NotNull Placing placing
    ) {
        this.hardness = hardness;
        this.tool = tool;
        this.placing = placing;
    }

    /**
     * @return The hardness value of the custom block
     */
    public float getHardness() {
        return this.hardness;
    }

    /**
     * Sets the hardness value of the custom block
     *
     * @param hardness A new hardness value for the custom block
     */
    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    /**
     * @return The Tool rules for the custom block
     */
    public @NotNull Tool getTool() {
        return this.tool;
    }

    /**
     * @return The Placing rules for the custom block
     */
    public @NotNull Placing getPlacing() {
        return this.placing;
    }

    /**
     * Calculates the dig speed of the custom block for a specific
     * player. The dig speed is influenced by the player's tool,
     * enchantments, and potion effects.
     *
     * @param player The player whose dig speed is being calculated
     * @return The calculated dig speed of the custom block for the
     *         player
     */
    public float calculateDigSpeed(@NotNull Player player) {
        float base = 1.0f;
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        Material material = itemInMainHand.getType();
        PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.FAST_DIGGING);

        if (this.tool.type == ToolType.fromMaterial(material)) {
            base = ToolTier.fromMaterial(material).getDigSpeed();

            if (itemInMainHand.containsEnchantment(Enchantment.DIG_SPEED)) {
                base += itemInMainHand.getEnchantmentLevel(Enchantment.DIG_SPEED) * 0.3f;
            }
        } else if (this.tool.type == ToolType.PICKAXE) {
            base /= 30.0f;
        } else {
            base /= 5.0f;
        }

        if (potionEffect != null) {
            base *= (potionEffect.getAmplifier() + 1) * 0.32f;
        }

        return base / this.hardness;
    }

    /**
     * Class representing the tool rules for a custom block. This
     * class holds information about the tool type and whether the
     * tool is forced. If the tool is forced, the player must use
     * the specified tool type to break the block to get the drops.
     */
    public static class Tool {
        private ToolType type;
        private boolean force;

        private Tool(
                @NotNull ToolType type,
                boolean force
        ) {
            this.type = type;
            this.force = force;
        }

        /**
         * Factory method for creating a new Tool object with the
         * default parameters
         *
         * @return A new Tool object with the {@link ToolType#HAND}
         *         tool type and not forced tool
         */
        @Contract(value = " -> new")
        public static @NotNull Tool create() {
            return new Tool(ToolType.HAND, false);
        }

        /**
         * Factory method for creating a new Tool object with the
         * specified tool type and whether the tool is forced
         *
         * @param type  The tool type for the custom block
         * @param force Whether the tool is forced
         * @return A new Tool object with the specified tool type
         */
        @Contract(value = "_, _ -> new")
        public static @NotNull Tool create(
                @NotNull ToolType type,
                boolean force
        ) {
            return new Tool(type, force);
        }

        /**
         * @return The tool type for the custom block
         */
        public @NotNull ToolType type() {
            return this.type;
        }

        /**
         * Sets the tool type for the custom block
         *
         * @param type The new tool type for the custom block
         * @return The Tool object with the new tool type
         */
        public @NotNull Tool type(@NotNull ToolType type) {
            this.type = type;
            return this;
        }

        /**
         * @return Whether the tool is forced for the custom block
         */
        public boolean force() {
            return this.force;
        }

        /**
         * Sets whether the tool is forced for the custom block
         *
         * @param force Whether the tool is forced
         * @return The Tool object with the new forced value
         */
        public @NotNull Tool force(boolean force) {
            this.force = force;
            return this;
        }

        /**
         * @return A string representation of the Tool rules
         *         for the custom block
         */
        @Override
        public @NotNull String toString() {
            return "Tool{" +
                    "type=" + this.type +
                    ", force=" + this.force +
                    '}';
        }
    }

    /**
     * Class representing the placing rules for a custom block. This
     * class holds information about the placing type and the set of
     * materials that can be placed on the custom block (like a Grass).
     */
    public static class Placing {
        private PlacingType type;
        private Set<Material> placeableMaterials;

        private Placing(
                @NotNull PlacingType type,
                @NotNull Set<Material> placeableMaterials
        ) {
            this.type = type;
            this.placeableMaterials = placeableMaterials;
        }

        /**
         * Factory method for creating a new Placing object with the
         * specified placing type and placeable materials
         *
         * @param type               The placing type for the custom block
         * @param placeableMaterials The set of materials that can be
         *                           placed on the custom block
         * @return A new Placing object with the specified placing type
         *         and placeable materials
         */
        @Contract(value = "_, _ -> new")
        public static @NotNull Placing create(
                @NotNull PlacingType type,
                @NotNull Set<Material> placeableMaterials
        ) {
            return new Placing(type, Collections.unmodifiableSet(placeableMaterials));
        }

        /**
         * Factory method for creating a new Placing object with the
         * specified placing type and placeable materials
         *
         * @param type               The placing type for the custom block
         * @param placeableMaterials The array of materials that can be
         *                           placed on the custom block
         * @return A new Placing object with the specified placing type
         *         and placeable materials
         */
        @Contract("_, _ -> new")
        public static @NotNull Placing create(
                @NotNull PlacingType type,
                Material @NotNull ... placeableMaterials
        ) {
            return new Placing(type, Set.of(placeableMaterials));
        }

        /**
         * @return The placing type for the custom block
         */
        public @NotNull PlacingType type() {
            return this.type;
        }

        /**
         * Sets the placing type for the custom block
         *
         * @param type The new placing type for the custom block
         * @return The Placing object with the new placing type
         */
        public @NotNull Placing type(@NotNull PlacingType type) {
            this.type = type;
            return this;
        }

        /**
         * @return The unmodifiable set of materials that can be
         *         placed on the custom block, or an empty set if
         *         no materials are specified
         */
        public @NotNull @Unmodifiable Set<Material> placeableMaterials() {
            return this.placeableMaterials == null ? Collections.emptySet() : this.placeableMaterials;
        }

        /**
         * Sets the set of materials that can be placed on the
         * custom block
         *
         * @param placeableMaterials The new set of materials that can
         *                           be placed on the custom block
         *                           (like a Grass)
         * @return The Placing object with the new set of placeable
         */
        public @NotNull Placing placeableMaterials(@NotNull Set<Material> placeableMaterials) {
            this.placeableMaterials = Collections.unmodifiableSet(placeableMaterials);
            return this;
        }

        /**
         * Sets the array of materials that can be placed on the
         * custom block
         *
         * @param placeableMaterials The new array of materials that can
         *                           be placed on the custom block
         * @return The Placing object with the new array of placeable
         */
        public @NotNull Placing placeableMaterials(Material @NotNull ... placeableMaterials) {
            this.placeableMaterials = Set.of(placeableMaterials);
            return this;
        }

        /**
         * @return A string representation of the Placing rules
         *         for the custom block
         */
        @Override
        public @NotNull String toString() {
            return "Placing{" +
                    "type=" + this.type +
                    ", placeableMaterials=" + this.placeableMaterials +
                    '}';
        }
    }
}
