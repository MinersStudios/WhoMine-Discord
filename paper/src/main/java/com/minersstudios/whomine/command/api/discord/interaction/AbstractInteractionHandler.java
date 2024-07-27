package com.minersstudios.whomine.command.api.discord.interaction;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.player.PlayerInfo;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractInteractionHandler<T extends Interaction> {
    private final WhoMine plugin;
    private final T interaction;
    protected PlayerInfo playerInfo;

    public AbstractInteractionHandler(
            final @NotNull WhoMine plugin,
            final @NotNull T interaction
    ) {
        this.plugin = plugin;
        this.interaction = interaction;
    }

    public final @NotNull WhoMine getPlugin() {
        return this.plugin;
    }

    public final @NotNull T getInteraction() {
        return this.interaction;
    }

    public final @Nullable PlayerInfo getPlayerInfo() {
        if (this.playerInfo != null) {
            return this.playerInfo;
        }

        final User user = this.interaction.getUser();

        if (this.plugin.getDiscordManager().isVerified(user)) {
            this.playerInfo = PlayerInfo.fromDiscord(this.plugin, user.getIdLong());
        }

        return this.playerInfo;
    }

    abstract @NotNull String getCommandName();
}
