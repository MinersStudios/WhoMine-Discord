package com.minersstudios.whomine.command.impl.minecraft.admin;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.utility.MSLogger;
import com.minersstudios.whomine.command.api.PluginCommandExecutor;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.utility.Font;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import static com.minersstudios.whomine.locale.Translations.*;
import static net.kyori.adventure.text.Component.text;

public final class GetMapLocationCommand extends PluginCommandExecutor {

    public GetMapLocationCommand(final @NotNull WhoMine plugin) {
        super(
                plugin,
                CommandData.builder()
                .name("getmaplocation")
                .aliases("getmaploc")
                .usage(" " + Font.Chars.RED_EXCLAMATION_MARK + " §cИспользуй: /<command>")
                .description("Добывает координаты карты, находящейся в руке")
                .permission("msessentials.maplocation")
                .permissionDefault(PermissionDefault.OP)
                .playerOnly(true)
                .commandNode(LiteralArgumentBuilder.literal("getmaplocation").build())
                .build()
        );
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        final Player player = (Player) sender;

        if (!(player.getInventory().getItemInMainHand().getItemMeta() instanceof final MapMeta mapMeta)) {
            MSLogger.warning(
                    player,
                    COMMAND_GET_MAP_LOCATION_NO_MAP_IN_RIGHT_HAND.asTranslatable()
            );

            return true;
        }

        final MapView mapView = mapMeta.getMapView();

        if (
                mapView == null
                || mapView.getWorld() == null
        ) {
            MSLogger.severe(
                    sender,
                    ERROR_SOMETHING_WENT_WRONG.asTranslatable()
            );

            return true;
        }

        final int x = mapView.getCenterX();
        final int z = mapView.getCenterZ();
        final int y = mapView.getWorld().getHighestBlockYAt(x, z) + 1;

        MSLogger.warning(
                player,
                COMMAND_GET_MAP_LOCATION_FORMAT.asTranslatable()
                .arguments(
                        text(mapView.getWorld().getName(), NamedTextColor.WHITE),
                        text(x, NamedTextColor.WHITE),
                        text(y, NamedTextColor.WHITE),
                        text(z, NamedTextColor.WHITE),
                        COMMAND_GET_MAP_LOCATION_COMMAND_BUTTON_TEXT.asTranslatable()
                        .decorate(TextDecoration.BOLD)
                        .clickEvent(ClickEvent.runCommand("/tp " + x + " " + y + " " + z))
                )
        );
        return true;
    }
}
