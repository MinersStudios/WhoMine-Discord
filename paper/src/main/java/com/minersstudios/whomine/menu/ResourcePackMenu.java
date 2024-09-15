package com.minersstudios.whomine.menu;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.inventory.InventoryButton;
import com.minersstudios.whomine.inventory.holder.AbstractInventoryHolder;
import com.minersstudios.whomine.inventory.holder.InventoryHolder;
import com.minersstudios.whomine.player.ResourcePack;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.PlayerSettings;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.minersstudios.whomine.locale.Translations.*;
import static com.minersstudios.whomine.api.utility.ChatUtils.COLORLESS_DEFAULT_STYLE;
import static com.minersstudios.whomine.api.utility.ChatUtils.DEFAULT_STYLE;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

@InventoryHolder
public final class ResourcePackMenu extends AbstractInventoryHolder {

    @Override
    protected @NotNull CustomInventory createCustomInventory() {
        final WhoMine plugin = this.getPlugin();
        final ItemStack infoItem = new ItemStack(Material.KNOWLEDGE_BOOK);
        final ItemMeta infoMeta = infoItem.getItemMeta();

        infoMeta.displayName(MENU_RESOURCE_PACK_BUTTON_INFO_TITLE.asComponent().style(DEFAULT_STYLE));
        infoMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        infoMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        infoMeta.lore(Arrays.asList(
                MENU_RESOURCE_PACK_BUTTON_INFO_LORE_0.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_INFO_LORE_1.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_INFO_LORE_2.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_INFO_LORE_3.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_INFO_LORE_4.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY)
        ));
        infoItem.setItemMeta(infoMeta);

        final ItemStack noneItem = new ItemStack(Material.COAL_BLOCK);
        final ItemMeta noneMeta = noneItem.getItemMeta();

        noneMeta.displayName(MENU_RESOURCE_PACK_BUTTON_NONE_TITLE.asComponent().style(DEFAULT_STYLE));
        noneMeta.lore(Arrays.asList(
                MENU_RESOURCE_PACK_BUTTON_NONE_LORE_0.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_NONE_LORE_1.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY)
        ));
        noneItem.setItemMeta(noneMeta);

        final ItemStack liteItem = new ItemStack(Material.IRON_BLOCK);
        final ItemMeta liteMeta = liteItem.getItemMeta();

        liteMeta.displayName(MENU_RESOURCE_PACK_BUTTON_LITE_TITLE.asComponent().style(DEFAULT_STYLE));
        liteMeta.lore(Arrays.asList(
                MENU_RESOURCE_PACK_BUTTON_LITE_LORE_0.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_LITE_LORE_1.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_LITE_LORE_2.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_LITE_LORE_3.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY)
        ));
        liteItem.setItemMeta(liteMeta);

        final ItemStack fullItem = new ItemStack(Material.NETHERITE_BLOCK);
        final ItemMeta fullMeta = fullItem.getItemMeta();

        fullMeta.displayName(MENU_RESOURCE_PACK_BUTTON_FULL_TITLE.asComponent().style(DEFAULT_STYLE));
        fullMeta.lore(Arrays.asList(
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_0.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_1.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_2.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_3.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_4.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_5.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_6.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_7.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_8.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_9.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_10.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_11.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY),
                MENU_RESOURCE_PACK_BUTTON_FULL_LORE_12.asComponent().style(COLORLESS_DEFAULT_STYLE).color(GRAY)
        ));
        fullItem.setItemMeta(fullMeta);

        final InventoryButton noneButton = new InventoryButton(noneItem, (event, inventory) -> {
            final Player player = (Player) event.getWhoClicked();
            final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
            final PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();
            final ResourcePack.Type packType = playerSettings.getResourcePackType();

            if (
                    packType != ResourcePack.Type.NULL
                    && packType != ResourcePack.Type.NONE
            ) {
                playerInfo.kick(
                        MENU_RESOURCE_PACK_BUTTON_NONE_KICK_TITLE.asComponent(),
                        MENU_RESOURCE_PACK_BUTTON_NONE_KICK_SUBTITLE.asComponent(),
                        PlayerKickEvent.Cause.RESOURCE_PACK_REJECTION
                );
            }

            playerSettings.setResourcePackType(ResourcePack.Type.NONE);
            playerSettings.save();
            InventoryButton.playClickSound(player);
            player.closeInventory();

            if (playerInfo.isInWorldDark()) {
                playerInfo.handleJoin();
            }
        });

        final InventoryButton fullButton = new InventoryButton(fullItem, (event, inventory) -> {
            final Player player = (Player) event.getWhoClicked();
            final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
            final PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();

            playerSettings.setResourcePackType(ResourcePack.Type.FULL);
            playerSettings.save();
            InventoryButton.playClickSound(player);
            player.closeInventory();
            playerInfo.handleResourcePack()
            .thenAccept(isLoaded -> {
                if (
                        isLoaded
                        && playerInfo.isInWorldDark()
                ) {
                    playerInfo.handleJoin();
                }
            });
        });

        final InventoryButton liteButton = new InventoryButton(liteItem, (event, inventory) -> {
            final Player player = (Player) event.getWhoClicked();
            final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
            final PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();

            playerSettings.setResourcePackType(ResourcePack.Type.LITE);
            playerSettings.save();
            InventoryButton.playClickSound(player);
            player.closeInventory();
            playerInfo.handleResourcePack()
            .thenAccept(isLoaded -> {
                if (
                        isLoaded
                        && playerInfo.isInWorldDark()
                ) {
                    playerInfo.handleJoin();
                }
            });
        });

        return CustomInventory
                .single(
                        MENU_RESOURCE_PACK_TITLE.asTranslatable().style(DEFAULT_STYLE),
                        1
                )
                .buttonAt(0, noneButton)
                .buttonAt(1, noneButton)
                .buttonAt(2, fullButton)
                .buttonAt(3, fullButton)
                .buttonAt(4, new InventoryButton().item(infoItem))
                .buttonAt(5, fullButton)
                .buttonAt(6, fullButton)
                .buttonAt(7, liteButton)
                .buttonAt(8, liteButton)
                .closeAction((event, inventory) -> {
                    final Player player = (Player) event.getPlayer();
                    final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
                    final ResourcePack.Type type = playerInfo.getPlayerFile().getPlayerSettings().getResourcePackType();

                    if (type == ResourcePack.Type.NULL) {
                        plugin.runTask(() -> player.openInventory(inventory));
                    }
                });
    }

    @Override
    public void open(final @NotNull Player player) {
        this.getCustomInventory().open(player);
    }
}
