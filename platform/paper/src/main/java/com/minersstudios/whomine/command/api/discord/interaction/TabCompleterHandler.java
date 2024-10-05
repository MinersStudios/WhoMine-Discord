package com.minersstudios.whomine.command.api.discord.interaction;

import com.minersstudios.whomine.WhoMine;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;
import org.jetbrains.annotations.NotNull;

public class TabCompleterHandler extends AbstractInteractionHandler<CommandAutoCompleteInteraction> {

    public TabCompleterHandler(
            final @NotNull WhoMine plugin,
            final @NotNull CommandAutoCompleteInteraction interaction
    ) {
        super(plugin, interaction);
    }

    @Override
    public @NotNull String getCommandName() {
        return this.getInteraction().getName();
    }

    public @NotNull AutoCompleteQuery getCurrentOption() {
        return this.getInteraction().getFocusedOption();
    }
}
