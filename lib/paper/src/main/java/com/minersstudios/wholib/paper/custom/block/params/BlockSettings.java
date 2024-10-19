package com.minersstudios.wholib.paper.custom.block.params;

import com.minersstudios.wholib.paper.custom.block.params.settings.Placing;
import com.minersstudios.wholib.paper.custom.block.params.settings.Tool;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

/**
 * Class representing the settings for a custom block. This class holds
 * information about the block's hardness, the tool rules for breaking the
 * block, and the placing rules for placing the block.
 */
@Immutable
public final class BlockSettings {
    private final float hardness;
    private final Tool tool;
    private final Placing placing;

    /**
     * Constructs the BlockSettings with the specified hardness, tool, and
     * placing rules
     *
     * @param hardness The hardness value of the custom block
     * @param tool     The Tool rules for the custom block
     * @param placing  The Placing rules for the custom block
     */
    public BlockSettings(
            final float hardness,
            final @NotNull Tool tool,
            final @NotNull Placing placing
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
     * Calculates the dig speed of the custom block for a specific player. The
     * dig speed is influenced by the player's tool, enchantments, and potion
     * effects.
     *
     * @param player               The player whose dig speed is being
     *                             calculated
     * @return The calculated dig speed of the custom block for the player using
     *         magic numbers
     */
    public float calculateDigSpeed(
            final @NotNull Player player
    ) {
        float base = 1.0f;
        final ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        final Material material = itemInMainHand.getType();
        final ToolType toolType = this.tool.getToolType();

        if (toolType == ToolType.fromMaterial(material)) {
            base = ToolTier.fromMaterial(material).getDigSpeed();

            if (itemInMainHand.containsEnchantment(Enchantment.EFFICIENCY)) {
                base += itemInMainHand.getEnchantmentLevel(Enchantment.EFFICIENCY) * 0.3f;
            }
        } else if (toolType == ToolType.PICKAXE) {
            base /= 30.0f;
        } else {
            base /= 5.0f;
        }

        final PotionEffect fastDigging = player.getPotionEffect(PotionEffectType.HASTE);

        if (fastDigging != null) {
            base *= (fastDigging.getAmplifier() + 1) * 1.8f;
        }

        final PotionEffect slowDigging = player.getPotionEffect(PotionEffectType.MINING_FATIGUE);

        if (slowDigging != null) {
            base /= (slowDigging.getAmplifier() + 1) * 3.6f;
        }

        return base / this.hardness;
    }
}
