package com.minersstudios.whomine.menu;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.inventory.CustomInventory;
import com.minersstudios.whomine.inventory.InventoryButton;
import com.minersstudios.whomine.inventory.holder.AbstractInventoryHolder;
import com.minersstudios.whomine.inventory.holder.InventoryHolder;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.player.PlayerFile;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.Pronouns;
import com.minersstudios.whomine.player.RegistrationProcess;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.locale.Translations.*;

@InventoryHolder
public final class PronounMenu extends AbstractInventoryHolder {

    @Override
    protected @NotNull CustomInventory createCustomInventory() {
        final WhoMine plugin = this.getPlugin();
        final ItemStack heItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        final ItemMeta heMeta = heItem.getItemMeta();
        final var loreHe = new ObjectArrayList<Component>();

        heMeta.displayName(MENU_PRONOUNS_BUTTON_HE_TITLE.asComponent().style(ChatUtils.DEFAULT_STYLE));
        loreHe.add(MENU_PRONOUNS_BUTTON_HE_LORE.asComponent().style(ChatUtils.COLORLESS_DEFAULT_STYLE).color(NamedTextColor.GRAY));
        heMeta.lore(loreHe);
        heItem.setItemMeta(heMeta);

        final ItemStack sheItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        final ItemMeta sheMeta = sheItem.getItemMeta();
        final var loreShe = new ObjectArrayList<Component>();

        sheMeta.displayName(MENU_PRONOUNS_BUTTON_SHE_TITLE.asComponent().style(ChatUtils.DEFAULT_STYLE));
        loreShe.add(MENU_PRONOUNS_BUTTON_SHE_LORE.asComponent().style(ChatUtils.COLORLESS_DEFAULT_STYLE).color(NamedTextColor.GRAY));
        sheMeta.lore(loreShe);
        sheItem.setItemMeta(sheMeta);

        final ItemStack theyItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta theyMeta = theyItem.getItemMeta();
        final var loreThey = new ObjectArrayList<Component>();

        theyMeta.displayName(MENU_PRONOUNS_BUTTON_THEY_TITLE.asComponent().style(ChatUtils.DEFAULT_STYLE));
        loreThey.add(MENU_PRONOUNS_BUTTON_THEY_LORE.asComponent().style(ChatUtils.COLORLESS_DEFAULT_STYLE).color(NamedTextColor.GRAY));
        theyMeta.lore(loreThey);
        theyItem.setItemMeta(theyMeta);

        final InventoryButton heButton = new InventoryButton(heItem, (event, inventory) -> {
            final Player player = (Player) event.getWhoClicked();
            final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
            final PlayerFile playerFile = playerInfo.getPlayerFile();

            playerFile.setPronouns(Pronouns.HE);
            playerFile.save();
            InventoryButton.playClickSound(player);
            player.closeInventory();
        });

        final InventoryButton sheButton = new InventoryButton(sheItem, (event, inventory) -> {
            final Player player = (Player) event.getWhoClicked();
            final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
            final PlayerFile playerFile = playerInfo.getPlayerFile();

            playerFile.setPronouns(Pronouns.SHE);
            playerFile.save();
            InventoryButton.playClickSound(player);
            player.closeInventory();
        });

        final InventoryButton theyButton = new InventoryButton(theyItem, (event, inventory) -> {
            final Player player = (Player) event.getWhoClicked();
            final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);
            final PlayerFile playerFile = playerInfo.getPlayerFile();

            playerFile.setPronouns(Pronouns.THEY);
            playerFile.save();
            InventoryButton.playClickSound(player);
            player.closeInventory();
        });

        return CustomInventory
                .single(
                        MENU_PRONOUNS_TITLE.asTranslatable().style(ChatUtils.DEFAULT_STYLE),
                        1
                )
                .buttonAt(0, heButton)
                .buttonAt(1, heButton)
                .buttonAt(2, heButton)
                .buttonAt(3, sheButton)
                .buttonAt(4, sheButton)
                .buttonAt(5, sheButton)
                .buttonAt(6, theyButton)
                .buttonAt(7, theyButton)
                .buttonAt(8, theyButton)
                .closeAction((event, inventory) -> {
                    final Player player = (Player) event.getPlayer();
                    final PlayerInfo playerInfo = PlayerInfo.fromOnlinePlayer(plugin, player);

                    if (playerInfo.getPlayerFile().getConfig().getString("pronouns") == null) {
                        plugin.runTask(() -> player.openInventory(inventory));
                    } else {
                        new RegistrationProcess(plugin).setPronouns(player, playerInfo);
                    }
                });
    }

    @Override
    public void open(final @NotNull Player player) {
        this.getCustomInventory().open(player);
    }
}
