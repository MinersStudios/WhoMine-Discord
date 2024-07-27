package com.minersstudios.whomine.command.api;

import com.minersstudios.whomine.WhoMine;
import com.minersstudios.whomine.command.api.discord.interaction.CommandHandler;
import com.minersstudios.whomine.command.api.discord.interaction.TabCompleterHandler;
import com.minersstudios.whomine.plugin.AbstractPluginComponent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an abstract class for handling discord slash commands
 */
public abstract class SlashCommandExecutor extends AbstractPluginComponent {
    private final Map<User, Map.Entry<Long, TabCompleterHandler>> userRemainingTabCompletes;
    private final SlashCommandData data;

    public static final int MAX_TAB_SIZE = 25;
    public static final List<Command.Choice> EMPTY_TAB = Collections.emptyList();

    /**
     * Slash command executor constructor
     *
     * @param plugin The plugin instance
     * @param data   The slash command data
     */
    protected SlashCommandExecutor(
            final @NotNull WhoMine plugin,
            final @NotNull SlashCommandData data
    ) {
        super(plugin);

        this.userRemainingTabCompletes = new ConcurrentHashMap<>();
        this.data = data;
    }

    /**
     * Returns the data of this slash command
     *
     * @return The data of this slash command
     */
    public final @NotNull SlashCommandData getData() {
        return this.data;
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{' +
                "plugin=" + this.getPlugin() +
                ", commandInfo=" + this.data +
                '}';
    }

    @Override
    public final void register() throws IllegalStateException {
        this.getPlugin().getCommandManager().registerDiscord(this);
        this.onRegister();
    }

    /**
     * Called on slash command interaction
     *
     * @param handler The command handler
     */
    protected abstract void onCommand(final @NotNull CommandHandler handler);

    /**
     * Called on tab complete
     * <br>
     * <b>NOTE:</b> The choices cannot be more than 25
     *
     * @param handler The tab completer handler
     * @return The tab complete choices
     */
    protected @NotNull List<Command.Choice> onTabComplete(final @NotNull TabCompleterHandler handler) {
        return EMPTY_TAB;
    }

    /**
     * Executes the slash command
     *
     * @param event The slash command interaction event
     */
    @ApiStatus.Internal
    public final void execute(final @NotNull SlashCommandInteractionEvent event) {
        this.onCommand(
                new CommandHandler(
                        this.getPlugin(),
                        event.getInteraction()
                )
        );
    }

    /**
     * Handles the tab complete event
     *
     * @param event The tab complete event
     * @throws IllegalStateException If the choices are more than 25
     */
    @ApiStatus.Internal
    public final void tabComplete(final @NotNull CommandAutoCompleteInteractionEvent event) throws IllegalStateException {
        final CommandAutoCompleteInteraction interaction = event.getInteraction();
        final var entry = this.userRemainingTabCompletes.get(interaction.getUser());
        final TabCompleterHandler handler;

        if (
                entry == null
                || entry.getKey() != interaction.getCommandIdLong()
        ) {
            handler = new TabCompleterHandler(this.getPlugin(), interaction);

            this.userRemainingTabCompletes.put(
                    interaction.getUser(),
                    Map.entry(
                            interaction.getCommandIdLong(),
                            handler
                    )
            );
        } else {
            handler = entry.getValue();
        }

        final var choices = this.onTabComplete(handler);
        final int size = choices.size();

        if (size > MAX_TAB_SIZE) {
            throw new IllegalStateException("Choices cannot be more than 25! Handle it yourself!");
        }

        if (size != 0) {
            event.replyChoices(choices).queue();
        }
    }
}
