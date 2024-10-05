package com.minersstudios.whomine.utility;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.player.PlayerInfo;
import com.minersstudios.whomine.player.collection.PlayerInfoMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for players
 */
public final class MSPlayerUtils {
    /**
     * Regex supports all <a href="https://jrgraphix.net/r/Unicode/0400-04FF">cyrillic</a> characters
     */
    public static final String NAME_REGEX = "[-Ѐ-ӿ]+";
    public static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    @Contract(" -> fail")
    private MSPlayerUtils() throws AssertionError {
        throw new AssertionError("Utility class");
    }

    /**
     * Adds player to the "hide_tags" {@link Team} and sets the {@link Scoreboard} for player
     *
     * @param player the player
     */
    public static void hideNameTag(
            final @NotNull WhoMine plugin,
            final @Nullable Player player
    ) {
        //if (player != null) {
        //    plugin.getScoreboardHideTagsTeam().addEntry(player.getName());
        //    player.setScoreboard(plugin.getScoreboardHideTags());
        //}
    }

    /**
     * @return A list of all online players' names and IDs
     */
    public static @NotNull List<String> getLocalPlayerNames(final @NotNull WhoMine plugin) {
        final var completions = new ObjectArrayList<String>();
        final PlayerInfoMap map = plugin.getCache().getPlayerInfoMap();

        for (final var player : plugin.getServer().getOnlinePlayers()) {
            final PlayerInfo playerInfo = map.get(player);

            if (playerInfo.isOnline()) {
                final int id = playerInfo.getID(false, false);

                if (id != -1) {
                    completions.add(String.valueOf(id));
                }

                completions.add(player.getName());
            }
        }

        return completions;
    }

    /**
     * @param string String to be checked
     * @return True if string matches {@link #NAME_REGEX}
     */
    @Contract("null -> false")
    public static boolean matchesNameRegex(final @Nullable String string) {
        return ChatUtils.isNotBlank(string)
                && NAME_PATTERN.matcher(string).matches();
    }
}
