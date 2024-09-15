package com.minersstudios.whomine.discord;

import com.minersstudios.whomine.Config;
import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.chat.ChatType;
import com.minersstudios.whomine.api.status.StatusHandler;
import com.minersstudios.whomine.api.utility.ChatUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.minersstudios.whomine.locale.Translations.DISCORD_BOT_STATUS;
import static com.minersstudios.whomine.locale.Translations.DISCORD_SERVER_ENABLED;

/**
 * This class handles the Discord integration for the MSEssentials plugin. It
 * provides methods for interacting with the Discord API using JDA
 * (Java Discord API).
 *
 * @see <a href="https://jda.wiki">JDA Wiki</a>
 */
public final class DiscordManager {
    private final WhoMine plugin;
    private JDA jda;
    private Guild mainGuild;
    private TextChannel globalChannel;
    private TextChannel localChannel;
    private Role memberRole;

    /**
     * Constructor for the DiscordHandler class
     *
     * @param plugin The MSEssentials plugin instance
     */
    public DiscordManager(final @NotNull WhoMine plugin) {
        this.plugin = plugin;
    }

    /**
     * @return The plugin instance
     */
    public @NotNull WhoMine getPlugin() {
        return this.plugin;
    }

    /**
     * @return An Optional containing the JDA instance if it exists, or an empty
     *         Optional otherwise
     */
    public @NotNull Optional<JDA> getJda() {
        return Optional.ofNullable(this.jda);
    }

    /**
     * @return An Optional containing the main guild if it exists, or an empty
     *         Optional otherwise
     */
    public @NotNull Optional<Guild> getMainGuild() {
        return Optional.ofNullable(this.mainGuild);
    }

    /**
     * @return An Optional containing the global channel if it exists, or an
     *         empty Optional otherwise
     */
    public @NotNull Optional<TextChannel> getGlobalChannel() {
        return Optional.ofNullable(this.globalChannel);
    }

    /**
     * @return An Optional containing the local channel if it exists, or an
     *         empty Optional otherwise
     */
    public @NotNull Optional<TextChannel> getLocalChannel() {
        return Optional.ofNullable(this.localChannel);
    }

    /**
     * @return An Optional containing the member role if it exists, or an empty
     *         Optional otherwise.
     *         <br>
     *         The member role is the role given to verified members.
     */
    public @NotNull Optional<Role> getMemberRole() {
        return Optional.ofNullable(this.memberRole);
    }

    /**
     * @param userId The id of the user to get
     * @return An Optional containing the user, or an empty Optional if not
     *         available
     */
    public @NotNull Optional<User> getUser(final long userId) {
        return this.jda == null
                ? Optional.empty()
                : Optional.ofNullable(this.jda.getUserById(userId));
    }

    /**
     * @param userId The id of the user to retrieve
     * @return An Optional containing the user, or an empty Optional if not
     *         available
     */
    public @NotNull Optional<User> retrieveUser(final long userId) {
        return this.jda == null
                ? Optional.empty()
                : Optional.ofNullable(this.jda.retrieveUserById(userId).complete());
    }

    /**
     * @param user The user to get the member of
     * @return An Optional containing the member, or an empty Optional if not
     *         available
     */
    public @NotNull Optional<Member> getMember(final @Nullable User user) {
        return this.mainGuild == null
                || user == null
                ? Optional.empty()
                : Optional.ofNullable(this.mainGuild.getMember(user));
    }

    /**
     * @param userId The id of the user to get the member of
     * @return An Optional containing the member, or an empty Optional if not
     *         available
     */
    public @NotNull Optional<Member> getMember(final long userId) {
        return this.mainGuild == null
                ? Optional.empty()
                : Optional.ofNullable(this.mainGuild.getMemberById(userId));
    }

    /**
     * @param user The user to retrieve the member of
     * @return An Optional containing the member, or an empty Optional if not
     *         available
     */
    public @NotNull Optional<Member> retrieveMember(final @Nullable User user) {
        return this.mainGuild == null
                || user == null
                ? Optional.empty()
                : Optional.ofNullable(this.mainGuild.retrieveMember(user).complete());
    }

    /**
     * @param userId The id of the user to retrieve the member of
     * @return An Optional containing the member, or an empty Optional if not
     *         available
     */
    public @NotNull Optional<Member> retrieveMember(final long userId) {
        return this.mainGuild == null
                ? Optional.empty()
                : Optional.ofNullable(this.mainGuild.retrieveMemberById(userId).complete());
    }

    /**
     * @param channelId The id of the channel to get
     * @return An Optional containing the channel, or an empty Optional if not
     *         available
     */
    public @NotNull Optional<TextChannel> getTextChannel(final long channelId) {
        return this.isLoaded()
                ? Optional.ofNullable(this.jda.getTextChannelById(channelId))
                : Optional.empty();
    }

    /**
     * @return True if the Discord bot is loaded, false otherwise
     */
    public boolean isLoaded() {
        return this.jda != null
                && this.jda.getStatus() == JDA.Status.CONNECTED;
    }

    /**
     * @param user The user to check
     * @return True if the user is a member of the main guild, false otherwise
     * @see #retrieveMember(User)
     */
    public boolean isMember(final @Nullable User user) {
        return this.retrieveMember(user).isPresent();
    }

    /**
     * @param userId The id of the user to check
     * @return True if the user is a member of the main guild, false otherwise
     * @see #retrieveMember(long)
     */
    public boolean isMember(final long userId) {
        return this.retrieveMember(userId).isPresent();
    }

    /**
     * @param user The user to check
     * @return True if the user is a member of the main guild and has the member
     *         role, false otherwise
     * @see #retrieveMember(User)
     * @see #getMemberRole()
     */
    public boolean isVerified(final @Nullable User user) {
        return user != null
                && this.memberRole != null
                && this.retrieveMember(user)
                .map(member -> member.getRoles().contains(this.memberRole))
                .orElse(false);
    }

    /**
     * @param userId The id of the user to check
     * @return True if the user is a member of the main guild and has the member
     *         role, false otherwise
     * @see #retrieveMember(long)
     * @see #getMemberRole()
     */
    public boolean isVerified(final long userId) {
        return this.memberRole != null
                && this.retrieveMember(userId)
                .map(member -> member.getRoles().contains(this.memberRole))
                .orElse(false);
    }

    /**
     * Sends a message to the specified chat type
     *
     * @param chatType The chat type to send the message to
     * @param message  The message to send
     * @see ChatType
     * @see #getGlobalChannel()
     * @see #getLocalChannel()
     */
    public void sendMessage(
            final @NotNull ChatType chatType,
            final @NotNull CharSequence message
    ) {
        if (!this.isLoaded()) {
            return;
        }

        switch (chatType) {
            case GLOBAL -> {
                if (this.globalChannel != null) {
                    this.globalChannel.sendMessage(message).queue();
                }
            }
            case LOCAL -> {
                if (this.localChannel != null) {
                    this.localChannel.sendMessage(message).queue();
                }
            }
        }
    }

    /**
     * Sends a message to the private channel of the specified user
     *
     * @param user    The user to send the message to
     * @param message The message to send
     */
    public void sendMessage(
            final @NotNull User user,
            final @NotNull CharSequence message
    ) {
        if (this.isLoaded()) {
            user.openPrivateChannel().queue(
                    privateChannel -> privateChannel.sendMessage(message).queue()
            );
        }
    }

    /**
     * Sends a message to the private channel of the specified user
     *
     * @param userId  The id of the user to send the message to
     * @param message The message to send
     * @see #retrieveUser(long)
     * @see #sendMessage(User, CharSequence)
     */
    public void sendMessage(
            final long userId,
            final @NotNull CharSequence message
    ) {
        this.retrieveUser(userId).ifPresent(
                user -> this.sendMessage(user, message)
        );
    }

    /**
     * Sends an embeds to the specified chat type
     *
     * @param chatType The chat type to send the message to
     * @param first    The first embed to send
     * @param rest     The rest of the embeds to send
     * @see ChatType
     * @see #getGlobalChannel()
     * @see #getLocalChannel()
     */
    public void sendEmbeds(
            final @NotNull ChatType chatType,
            final @NotNull MessageEmbed first,
            final MessageEmbed @NotNull ... rest
    ) {
        if (!this.isLoaded()) {
            return;
        }

        switch (chatType) {
            case GLOBAL -> {
                if (this.globalChannel != null) {
                    this.globalChannel.sendMessageEmbeds(first, rest).queue();
                }
            }
            case LOCAL -> {
                if (this.localChannel != null) {
                    this.localChannel.sendMessageEmbeds(first, rest).queue();
                }
            }
        }
    }

    /**
     * Sends an embeds to the private channel of the specified user
     *
     * @param user  The user to send the message to
     * @param first The first embed to send
     * @param rest  The rest of the embeds to send
     */
    public void sendEmbeds(
            final @NotNull User user,
            final @NotNull MessageEmbed first,
            final MessageEmbed @NotNull ... rest
    ) {
        if (this.isLoaded()) {
            user.openPrivateChannel().queue(
                    privateChannel -> privateChannel.sendMessageEmbeds(first, rest).queue()
            );
        }
    }

    /**
     * Sends an embeds to the private channel of the specified user
     *
     * @param userId The id of the user to send the message to
     * @param first  The first embed to send
     * @param rest   The rest of the embeds to send
     * @see #retrieveUser(long)
     * @see #sendEmbeds(User, MessageEmbed, MessageEmbed...)
     */
    public void sendEmbeds(
            final long userId,
            final @NotNull MessageEmbed first,
            final MessageEmbed @NotNull ... rest
    ) {
        this.retrieveUser(userId).ifPresent(
                user -> this.sendEmbeds(user, first, rest)
        );
    }

    /**
     * Sends an embed action message with the specified color, player head and
     * action message to the specified chat type
     *
     * @param chatType      The chat type to send the message to
     * @param playerName    The name of the player who did the action
     * @param actionMessage The action message to send
     * @param colorRaw      The color of the embed
     */
    public void sendActionMessage(
            final @NotNull ChatType chatType,
            final @NotNull String playerName,
            final @NotNull String actionMessage,
            final int colorRaw
    ) {
        if (!this.isLoaded()) {
            return;
        }

        this.sendEmbeds(
                chatType,
                new MessageEmbed(
                        null,
                        null,
                        null,
                        EmbedType.RICH,
                        null,
                        colorRaw,
                        null,
                        null,
                        new MessageEmbed.AuthorInfo(
                                actionMessage,
                                null,
                                "https://cravatar.eu/helmavatar/" + playerName + "/128.png",
                                null
                        ),
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    /**
     * Loads the Discord bot
     *
     * @throws IllegalStateException If the Discord bot is already loaded
     */
    public void load() throws IllegalStateException {
        if (this.isLoaded()) {
            throw new IllegalStateException("Discord bot is already loaded!");
        }

        final Logger logger = this.plugin.getLogger();
        final Config config = this.plugin.getConfiguration();
        final StatusHandler statusHandler = this.plugin.getStatusHandler();
        final String botToken = config.getYaml().getString("discord.bot-token");

        this.plugin.runTaskAsync(() -> {
            try {
                statusHandler.assignStatus(WhoMine.LOADING_DISCORD);

                this.jda = this.buildJda(botToken);
            } catch (final Throwable e) {
                logger.log(
                        Level.SEVERE,
                        "An error occurred while loading Discord bot!",
                        e
                );
            }

            if (this.jda == null) {
                logger.warning("Discord bot not found!");
                statusHandler.assignStatus(WhoMine.FAILED_LOAD_DISCORD);

                return;
            }

            this.mainGuild = this.jda.getGuildById(config.getDiscordServerId());

            if (this.mainGuild == null) {
                logger.warning("Discord server not found!");
                statusHandler.assignStatus(WhoMine.FAILED_LOAD_DISCORD);
                this.unload();

                return;
            }

            final long memberRoleId = config.getMemberRoleId();
            final long globalChannelId = config.getDiscordGlobalChannelId();
            final long localChannelId = config.getDiscordLocalChannelId();

            if (memberRoleId != 0) {
                this.memberRole = this.mainGuild.getRoleById(memberRoleId);

                if (this.memberRole == null) {
                    logger.warning("Discord member role not found!");
                }
            }

            if (globalChannelId != 0) {
                this.globalChannel = this.jda.getTextChannelById(globalChannelId);

                if (this.globalChannel == null) {
                    logger.warning("Discord global channel not found!");
                }
            }

            if (localChannelId != 0) {
                this.localChannel = this.jda.getTextChannelById(localChannelId);

                if (this.localChannel == null) {
                    logger.warning("Discord local channel not found!");
                }
            }

            final Presence presence = this.jda.getPresence();

            if (config.isDeveloperMode()) {
                presence.setStatus(OnlineStatus.DO_NOT_DISTURB);
            }

            presence.setActivity(
                    Activity.playing(
                            DISCORD_BOT_STATUS.asString(this.plugin.getServer().getMinecraftVersion())
                    )
            );

            statusHandler.assignStatus(WhoMine.LOADED_DISCORD);

            this.sendMessage(ChatType.GLOBAL, DISCORD_SERVER_ENABLED.asString());
            this.sendMessage(ChatType.LOCAL, DISCORD_SERVER_ENABLED.asString());
        });
    }

    /**
     * Unloads the Discord bot
     */
    public void unload() {
        if (this.isLoaded()) {
            this.jda.shutdown();
        }
    }

    private @Nullable JDA buildJda(final @Nullable String botToken) throws InterruptedException, IllegalStateException {
        return ChatUtils.isBlank(botToken)
                ? null
                : JDABuilder
                .createDefault(botToken)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS
                )
                .addEventListeners(new ListenerAdapter() {

                    @Override
                    public void onShutdown(final @NotNull ShutdownEvent event) {
                        DiscordManager.this.jda = null;
                        DiscordManager.this.mainGuild = null;
                        DiscordManager.this.globalChannel = null;
                        DiscordManager.this.localChannel = null;
                        DiscordManager.this.memberRole = null;
                    }
                })
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build()
                .awaitReady();
    }
}
