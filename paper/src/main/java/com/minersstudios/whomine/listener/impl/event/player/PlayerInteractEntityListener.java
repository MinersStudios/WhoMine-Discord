package com.minersstudios.whomine.listener.impl.event.player;

import com.minersstudios.whomine.api.event.EventOrder;
import com.minersstudios.whomine.api.event.ListenFor;
import com.minersstudios.whomine.event.PaperEventContainer;
import com.minersstudios.whomine.event.PaperEventListener;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.utility.MessageUtils;
import com.minersstudios.whomine.api.utility.SharedConstants;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import com.minersstudios.whomine.api.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

@ListenFor(eventClass = PlayerInteractEntityEvent.class)
public final class PlayerInteractEntityListener extends PaperEventListener {
    private final SecureRandom random = new SecureRandom();

    @EventHandler(priority = EventOrder.CUSTOM, ignoreCancelled = true)
    public void onPlayerInteractEntity(final @NotNull PaperEventContainer<PlayerInteractEntityEvent> container) {
        final PlayerInteractEntityEvent event = container.getEvent();
        final Player whoClicked = event.getPlayer();

        if (event.getRightClicked() instanceof final Player clickedPlayer) {
            final PlayerInfo clickedInfo = PlayerInfo.fromOnlinePlayer(container.getModule(), clickedPlayer);
            final ItemStack helmet = clickedPlayer.getInventory().getHelmet();
            final float pitch = whoClicked.getEyeLocation().getPitch();

            if (
                    (pitch >= 80 && pitch <= 90)
                    && whoClicked.isSneaking()
                    && !whoClicked.getPassengers().isEmpty()
            ) {
                whoClicked.eject();
            }

            whoClicked.sendActionBar(
                    clickedInfo.getPlayerFile().getPlayerName()
                    .createFullName(
                            clickedInfo.getID(),
                            MessageUtils.Colors.JOIN_MESSAGE_COLOR_SECONDARY,
                            MessageUtils.Colors.JOIN_MESSAGE_COLOR_PRIMARY
                    )
            );

            if (
                    !whoClicked.isInsideVehicle()
                    && helmet != null
                    && !whoClicked.isSneaking()
                    && helmet.getType() == Material.SADDLE
            ) {
                final var passengers = clickedPlayer.getPassengers();

                if (passengers.isEmpty()) {
                    clickedPlayer.addPassenger(whoClicked);
                } else {
                    passengers.getLast().addPassenger(whoClicked);
                }
            }
        } else if (event.getRightClicked() instanceof final ItemFrame itemFrame) {
            final boolean hasTag = itemFrame.getScoreboardTags().contains(SharedConstants.INVISIBLE_ITEM_FRAME_TAG);
            final Material frameMaterial = itemFrame.getItem().getType();
            final Material handMaterial = whoClicked.getInventory().getItemInMainHand().getType();

            if (
                    frameMaterial.isAir()
                    && !handMaterial.isAir()
                    && hasTag
            ) {
                itemFrame.setVisible(false);
            } else if (
                    (!frameMaterial.isAir() || whoClicked.isSneaking())
                    && handMaterial == Material.SHEARS
                    && !hasTag
            ) {
                whoClicked.getWorld().playSound(
                        itemFrame.getLocation(),
                        Sound.ENTITY_SHEEP_SHEAR,
                        SoundCategory.PLAYERS,
                        1.0f,
                        this.random.nextFloat() * 0.1f + 0.5f
                );
                itemFrame.addScoreboardTag(SharedConstants.INVISIBLE_ITEM_FRAME_TAG);
                itemFrame.setVisible(frameMaterial.isAir());
                event.setCancelled(true);
            }
        }
    }
}
