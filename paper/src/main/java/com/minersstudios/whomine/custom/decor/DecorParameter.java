package com.minersstudios.whomine.custom.decor;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.custom.decor.action.DecorClickAction;
import com.minersstudios.whomine.world.location.MSBoundingBox;
import com.minersstudios.whomine.utility.ItemUtils;
import com.minersstudios.whomine.utility.PlayerUtils;
import com.minersstudios.whomine.custom.decor.event.CustomDecorClickEvent;
import com.minersstudios.whomine.custom.item.CustomItemType;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * DecorParameter enum represents different parameters for custom decor
 * functionalities
 */
public enum DecorParameter {
    //<editor-fold desc="Parameters" defaultstate="collapsed">
    PAINTABLE,
    SITTABLE(
            (plugin, event) -> {
                if (
                        event.getClickType().isRightClick()
                        && event.getCustomDecor().getData().isSittable()
                ) {
                    doSit(plugin, event);
                }
            }
    ),
    WRENCHABLE(
            (plugin, event) -> {
                if (event.getClickType().isLeftClick()) {
                    return;
                }

                final ItemStack itemInUse = event.getPlayer().getInventory().getItem(event.getHand());

                if (CustomItemType.fromItemStack(itemInUse) == CustomItemType.WRENCH) {
                    doWrench(event, itemInUse);
                }
            }
    ),
    LIGHTABLE(
            (plugin, event) -> {
                if (event.getClickType().isLeftClick()) {
                    return;
                }

                final Interaction interaction = event.getClickedInteraction();

                doLight(
                        event,
                        event.getCustomDecor().getData().getNextLightLevel(
                                interaction.getWorld().getBlockAt(interaction.getLocation()).getBlockData() instanceof final Light light
                                        ? light.getLevel()
                                        : 0
                        )
                );
            }
    ),
    TYPED,
    LIGHT_TYPED(
            (plugin, event) -> {
                if (event.getClickType().isLeftClick()) {
                    return;
                }

                final CustomDecor customDecor = event.getCustomDecor();
                final var data = customDecor.getData();
                final Interaction interaction = event.getClickedInteraction();
                final int nextLevel = data.getNextLightLevel(
                        interaction.getWorld().getBlockAt(interaction.getLocation()).getBlockData() instanceof Light light
                                ? light.getLevel()
                                : 0
                );
                final var nextType = data.getLightTypeOf(nextLevel);

                doLight(event, nextLevel);

                if (nextType == null) {
                    return;
                }

                final ItemStack typeItem = nextType.getItem();
                final ItemMeta itemMeta = typeItem.getItemMeta();

                final ItemDisplay itemDisplay = customDecor.getDisplay();
                final ItemStack displayItem = itemDisplay.getItemStack();

                itemMeta.displayName(displayItem.getItemMeta().displayName());
                typeItem.setItemMeta(itemMeta);

                itemDisplay.setItemStack(typeItem);
            }
    ),
    FACE_TYPED;
    //</editor-fold>

    private final DecorClickAction clickAction;

    //<editor-fold desc="Actions" defaultstate="collapsed">
    private static final DecorClickAction WRENCHABLE_SITTABLE_CLICK_ACTION =
            (plugin, event) -> {
                if (event.getClickType().isLeftClick()) {
                    return;
                }

                final ItemStack itemInUse = event.getPlayer().getInventory().getItem(event.getHand());

                if (CustomItemType.fromItemStack(itemInUse) == CustomItemType.WRENCH) {
                    doWrench(event, itemInUse);
                } else {
                    doSit(plugin, event);
                }
            };

    private static final DecorClickAction WRENCHABLE_LIGHTABLE_CLICK_ACTION =
            (plugin, event) -> {
                if (event.getClickType().isLeftClick()) {
                    return;
                }

                final ItemStack itemInUse = event.getPlayer().getInventory().getItem(event.getHand());

                if (CustomItemType.fromItemStack(itemInUse) == CustomItemType.WRENCH) {
                    doWrench(event, itemInUse);
                } else {
                    final Interaction interaction = event.getClickedInteraction();
                    final BlockData lightBlockData =
                            interaction.getWorld().getBlockAt(interaction.getLocation()).getBlockData();

                    doLight(
                            event,
                            event.getCustomDecor().getData().getNextLightLevel(
                                    lightBlockData instanceof final Light light
                                    ? light.getLevel()
                                    : 0
                            )
                    );
                }
            };
    //</editor-fold>

    /**
     * Constructor for DecorParameter with no DecorClickAction
     */
    DecorParameter() {
        this(DecorClickAction.NONE);
    }

    /**
     * Constructor for DecorParameter with a specified DecorClickAction
     *
     * @param clickAction The DecorClickAction associated with this parameter
     */
    DecorParameter(final @NotNull DecorClickAction clickAction) {
        this.clickAction = clickAction;
    }

    /**
     * @return The DecorClickAction of this parameter
     */
    public @NotNull DecorClickAction getClickAction() {
        return this.clickAction;
    }

    public static @NotNull DecorClickAction sittableAction() {
        return SITTABLE.getClickAction();
    }

    /**
     * @return The DecorClickAction of the wrenchable parameter
     */
    public static @NotNull DecorClickAction wrenchableAction() {
        return WRENCHABLE.getClickAction();
    }

    /**
     * @return The DecorClickAction of the lightable parameter
     */
    public static @NotNull DecorClickAction lightableAction() {
        return LIGHTABLE.getClickAction();
    }

    /**
     * @return The DecorClickAction of the typed parameter
     */
    public static @NotNull DecorClickAction lightTypedAction() {
        return LIGHT_TYPED.getClickAction();
    }

    /**
     * @return The DecorClickAction of the face typed parameter
     */
    public static @NotNull DecorClickAction wrenchableSittableAction() {
        return WRENCHABLE_SITTABLE_CLICK_ACTION;
    }

    /**
     * @return The DecorClickAction of the face typed parameter
     */
    public static @NotNull DecorClickAction wrenchableLightableAction() {
        return WRENCHABLE_LIGHTABLE_CLICK_ACTION;
    }

    /**
     * Performs the action of sitting on the decor
     *
     * @param plugin The WhoMine plugin
     * @param event  The CustomDecorClickEvent called when the player
     *               right-clicked the decor
     */
    public static void doSit(
            final @NotNull WhoMine plugin,
            final @NotNull CustomDecorClickEvent event
    ) {
        final Player player = event.getPlayer();

        if (player.getVehicle() != null) {
            return;
        }

        final Location sitLocation =
                event.getClickedInteraction().getLocation()
                .add(0.0d, event.getCustomDecor().getData().getSitHeight(), 0.0d);

        for (final var nearbyPlayer : sitLocation.getNearbyEntitiesByType(Player.class, 0.5d)) {
            if (nearbyPlayer.getVehicle() != null) {
                return;
            }
        }

        PlayerUtils.setSitting(plugin, player, sitLocation);
        player.getWorld().playSound(
                sitLocation,
                Sound.ENTITY_HORSE_SADDLE,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );

        player.swingHand(event.getHand());
    }

    /**
     * Performs the action of wrenching the decor
     *
     * @param event      The CustomDecorClickEvent called when the player
     *                   right-clicked the decor
     * @param itemInUse  The item in the player's hand
     */
    public static void doWrench(
            final @NotNull CustomDecorClickEvent event,
            final @NotNull ItemStack itemInUse
    ) {
        final CustomDecor customDecor = event.getCustomDecor();
        final ItemDisplay itemDisplay = customDecor.getDisplay();
        final ItemStack displayItem = itemDisplay.getItemStack();
        final var nextType = customDecor.getData().getNextType(displayItem);

        if (nextType == null) {
            return;
        }

        final Player player = event.getPlayer();

        itemDisplay.setItemStack(
                CustomDecorDataImpl.copyMetaForTypeItem(
                        nextType.getItem(),
                        displayItem
                )
        );

        if (player.getGameMode() == GameMode.SURVIVAL) {
            ItemUtils.damageItem(player, itemInUse);
        }

        player.getWorld().playSound(
                customDecor.getBoundingBox().getCenter().toLocation(),
                Sound.ITEM_SPYGLASS_USE,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );
        player.swingHand(event.getHand());
    }

    /**
     * Performs the action of lighting the decor
     *
     * @param event     The CustomDecorClickEvent called when the player
     *                  right-clicked the decor
     * @param nextLevel The next light level of the decor to be set
     */
    public static void doLight(
            final @NotNull CustomDecorClickEvent event,
            final int nextLevel
    ) {
        final World world = event.getClickedInteraction().getWorld();
        final MSBoundingBox msbb = event.getCustomDecor().getBoundingBox();

        for (final var block : msbb.getBlockStates(world)) {
            if (block.getBlockData() instanceof final Light light) {
                light.setLevel(nextLevel);
                block.setBlockData(light);
                block.update();
            }
        }
    }
}
