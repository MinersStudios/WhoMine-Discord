package com.minersstudios.whomine.command.api;

import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.wholib.module.AbstractModuleComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Represents a class which contains a bunch of methods for handling commands
 */
public abstract class PluginCommandExecutor extends AbstractModuleComponent<WhoMine> implements CommandExecutor, TabCompleter {
    private final CommandData data;

    /**
     * An empty tab completion list. Used when no tab completion is needed or
     * when the tab completion is handled by the command executor itself.
     */
    public static final List<String> EMPTY_TAB = Collections.emptyList();

    /**
     * Plugin command executor constructor
     *
     * @param plugin The plugin instance
     * @param data   The command data
     */
    protected PluginCommandExecutor(
            final @NotNull WhoMine plugin,
            final @NotNull CommandData data
    ) {
        super(plugin);

        this.data = data;
    }

    /**
     * Returns the command data for this command executor
     *
     * @return The command data for this command executor
     */
    public final @NotNull CommandData getData() {
        return this.data;
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getSimpleName() + '{' +
                "plugin=" + this.getModule() +
                ", commandData=" + this.data +
                '}';
    }

    public void register() throws IllegalStateException {
        //this.getModule().getCommandManager().registerMinecraft(this);
        //this.onRegister();
    }

    /**
     * Executes the given command, returning its success. If false is returned,
     * then the "usage" plugin.yml entry for this command (if defined) will be
     * sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return True if a valid command, otherwise false
     */
    @Override
    public abstract boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    );

    /**
     * Requests a list of possible completions for a command argument
     *
     * @param sender  Source of the command. For player tab-completing a command
     *                inside a command block, this will be the player, not the
     *                command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null to
     *         default to the command executor
     */
    @Override
    public @NotNull List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final String @NotNull ... args
    ) {
        return EMPTY_TAB;
    }
}
