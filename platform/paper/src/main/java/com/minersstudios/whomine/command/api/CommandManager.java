package com.minersstudios.whomine.command.api;

import com.minersstudios.wholib.module.MainModule;
import com.minersstudios.whomine.WhoMineImpl;
import com.minersstudios.whomine.command.api.minecraft.CommandData;
import com.minersstudios.whomine.command.api.minecraft.Commodore;
import com.minersstudios.whomine.command.impl.discord.*;
import com.minersstudios.whomine.command.impl.minecraft.CraftsCommand;
import com.minersstudios.whomine.command.impl.minecraft.MSCoreCommandHandler;
import com.minersstudios.whomine.command.impl.minecraft.admin.GetMapLocationCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.KickCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.SetServerSpawn;
import com.minersstudios.whomine.command.impl.minecraft.admin.WhitelistCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.ban.BanCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.ban.UnBanCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.msessentials.MSEssentialsCommandHandler;
import com.minersstudios.whomine.command.impl.minecraft.admin.mute.MuteCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.mute.UnMuteCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.player.AdminPlayerCommandHandler;
import com.minersstudios.whomine.command.impl.minecraft.admin.teleport.TeleportToLastDeathLocationCommand;
import com.minersstudios.whomine.command.impl.minecraft.admin.teleport.WorldTeleportCommand;
import com.minersstudios.whomine.command.impl.minecraft.block.MSBlockCommandHandler;
import com.minersstudios.whomine.command.impl.minecraft.decor.MSDecorCommandHandler;
import com.minersstudios.whomine.command.impl.minecraft.item.MSItemCommandHandler;
import com.minersstudios.whomine.command.impl.minecraft.item.RenamesCommand;
import com.minersstudios.whomine.command.impl.minecraft.player.DiscordCommand;
import com.minersstudios.whomine.command.impl.minecraft.player.PrivateMessageCommand;
import com.minersstudios.whomine.command.impl.minecraft.player.ResourcePackCommand;
import com.minersstudios.whomine.command.impl.minecraft.player.SkinsCommand;
import com.minersstudios.whomine.command.impl.minecraft.player.roleplay.*;
import com.minersstudios.wholib.status.StatusWatcher;
import com.minersstudios.wholib.utility.ChatUtils;
import com.mojang.brigadier.tree.LiteralCommandNode;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a command manager
 */
public final class CommandManager {
    private final WhoMineImpl plugin;
    private final Commodore commodore;
    private final Long2ObjectMap<SlashCommandExecutor> discordExecutorMap;
    private final Map<String, PluginCommandExecutor> minecraftExecutorMap;
    private final Set<String> onlyPlayerCommandSet;

    private static final Constructor<PluginCommand> COMMAND_CONSTRUCTOR;

    static {
        try {
            COMMAND_CONSTRUCTOR = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);

            COMMAND_CONSTRUCTOR.setAccessible(true);
        } catch (final NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Constructs a new CommandManager instance
     *
     * @param plugin Plugin instance
     */
    public CommandManager(final @NotNull WhoMineImpl plugin) {
        this.plugin = plugin;
        this.commodore = new Commodore(plugin);
        this.discordExecutorMap = new Long2ObjectOpenHashMap<>();
        this.minecraftExecutorMap = new Object2ObjectOpenHashMap<>();
        this.onlyPlayerCommandSet = new ObjectOpenHashSet<>();
    }

    /**
     * Returns an unmodifiable view of the discord command id set
     *
     * @return An unmodifiable view of the discord command id set
     */
    public @NotNull @UnmodifiableView Set<Long> discordIdSet() {
        return Collections.unmodifiableSet(this.discordExecutorMap.keySet());
    }

    /**
     * Returns an unmodifiable view of the discord command executors
     *
     * @return An unmodifiable view of the discord command executors
     */
    public @NotNull @UnmodifiableView Collection<SlashCommandExecutor> discordExecutors() {
        return Collections.unmodifiableCollection(this.discordExecutorMap.values());
    }

    /**
     * Returns an unmodifiable view of the minecraft command name set
     *
     * @return An unmodifiable view of the minecraft command name set
     */
    public @NotNull @UnmodifiableView Set<String> minecraftNameSet() {
        return Collections.unmodifiableSet(this.minecraftExecutorMap.keySet());
    }

    /**
     * Returns an unmodifiable view of the minecraft command executors
     *
     * @return An unmodifiable view of the minecraft command executors
     */
    public @NotNull @UnmodifiableView Collection<PluginCommandExecutor> minecraftExecutors() {
        return Collections.unmodifiableCollection(this.minecraftExecutorMap.values());
    }

    /**
     * Returns an unmodifiable view of the only player command set
     *
     * @return An unmodifiable view of the only player command set
     */
    public @NotNull @UnmodifiableView Set<String> onlyPlayerCommandSet() {
        return Collections.unmodifiableSet(this.onlyPlayerCommandSet);
    }

    /**
     * Returns the commodore instance
     *
     * @return The commodore instance
     */
    public @NotNull Commodore getCommodore() {
        return this.commodore;
    }

    /**
     * Gets and returns the discord command executor for the given id
     *
     * @param id The id of the command
     * @return Discord command executor for the given id or null if not found
     */
    public @Nullable SlashCommandExecutor getDiscordExecutor(final long id) {
        return this.discordExecutorMap.get(id);
    }

    /**
     * Gets and returns the minecraft command executor for the given name
     *
     * @param name The name of the command
     * @return Minecraft command executor for the given name or null if not found
     */
    public @Nullable PluginCommandExecutor getMinecraftExecutor(final @NotNull String name) {
        return ChatUtils.isBlank(name)
                ? null
                : this.minecraftExecutorMap.get(name);
    }

    /**
     * Creates a new {@link PluginCommand} instance
     *
     * @param command Command name
     * @return A new {@link PluginCommand} instance or null if failed
     */
    public @Nullable PluginCommand createCommand(final @NotNull String command) {
        try {
            return COMMAND_CONSTRUCTOR.newInstance(command, this.plugin);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            this.plugin.getLogger().log(
                    Level.SEVERE,
                    "Failed to create command : " + command,
                    e
            );

            return null;
        }
    }

    /**
     * Returns whether the command is player only
     *
     * @param command Command to check
     * @return True if the command is player only
     */
    public boolean isPlayerOnly(final @Nullable String command) {
        return ChatUtils.isNotBlank(command)
                && this.onlyPlayerCommandSet.contains(command);
    }

    /**
     * Bootstraps all commands
     */
    public void bootstrap() {
        //<editor-fold desc="Discord commands">
        this.plugin.getStatusHandler().addWatcher(
                StatusWatcher.builder()
                .statuses(MainModule.LOADED_DISCORD)
                .successRunnable(
                        () -> {
                            new AddSkinCommand(this.plugin).register();
                            new EditSkinCommand(this.plugin).register();
                            new HelpCommand(this.plugin).register();
                            new RemoveSkinCommand(this.plugin).register();
                            new SkinListCommand(this.plugin).register();
                            new UnlinkCommand(this.plugin).register();
                        }
                )
                .build()
        );
        //</editor-fold>

        //<editor-fold desc="Minecraft commands">
        new BanCommand(this.plugin).register();
        new UnBanCommand(this.plugin).register();
        new MSEssentialsCommandHandler(this.plugin).register();
        new MuteCommand(this.plugin).register();
        new UnMuteCommand(this.plugin).register();
        new AdminPlayerCommandHandler(this.plugin).register();
        new TeleportToLastDeathLocationCommand(this.plugin).register();
        new WorldTeleportCommand(this.plugin).register();
        new GetMapLocationCommand(this.plugin).register();
        new KickCommand(this.plugin).register();
        new SetServerSpawn(this.plugin).register();
        new WhitelistCommand(this.plugin).register();
        new MSBlockCommandHandler(this.plugin).register();
        new MSDecorCommandHandler(this.plugin).register();
        new MSItemCommandHandler(this.plugin).register();
        new RenamesCommand(this.plugin).register();
        new DoCommand(this.plugin).register();
        new FartCommand(this.plugin).register();
        new ItCommand(this.plugin).register();
        new MeCommand(this.plugin).register();
        new SitCommand(this.plugin).register();
        new SpitCommand(this.plugin).register();
        new TodoCommand(this.plugin).register();
        new TryCommand(this.plugin).register();
        new DiscordCommand(this.plugin).register();
        new PrivateMessageCommand(this.plugin).register();
        new ResourcePackCommand(this.plugin).register();
        new SkinsCommand(this.plugin).register();
        new CraftsCommand(this.plugin).register();
        new MSCoreCommandHandler(this.plugin).register();
        //</editor-fold>
    }

    void registerDiscord(final @NotNull SlashCommandExecutor executor) throws IllegalStateException {
        if (this.discordExecutorMap.containsValue(executor)) {
            throw new IllegalStateException("Command already registered : " + executor);
        }

        this.plugin.getDiscordModule().getJda()
        .ifPresent(
                jda -> jda.upsertCommand(executor.getData())
                          .onSuccess(
                                  command -> {
                                      synchronized (this.discordExecutorMap) {
                                          this.discordExecutorMap.put(command.getIdLong(), executor);
                                      }
                                  }
                          )
                          .queue()
        );
    }

    void registerMinecraft(final @NotNull PluginCommandExecutor executor) throws IllegalStateException {
        final CommandData data = executor.getData();
        final String name = data.getName();

        if (this.minecraftExecutorMap.containsKey(name)) {
            throw new IllegalStateException("Command already registered : " + executor);
        }

        final PluginCommand bukkitCommand = this.plugin.getCommand(name);
        final PluginCommand pluginCommand =
                bukkitCommand == null
                ? this.createCommand(name)
                : bukkitCommand;

        if (pluginCommand == null) {
            this.plugin.getLogger().severe("Failed to register command : " + name);

            return;
        }

        final Server server = this.plugin.getServer();
        final var aliases = data.getAliases();
        final String usage = data.getUsage();
        final String description = data.getDescription();
        final String permission = data.getPermission();

        if (!aliases.isEmpty()) {
            pluginCommand.setAliases(aliases);
        }

        if (!usage.isEmpty()) {
            pluginCommand.setUsage(usage);
        }

        if (!description.isEmpty()) {
            pluginCommand.setDescription(description);
        }

        if (ChatUtils.isNotBlank(permission)) {
            final PluginManager pluginManager = server.getPluginManager();

            if (pluginManager.getPermission(permission) == null) {
                pluginManager.addPermission(
                        new Permission(
                                permission,
                                data.getPermissionDefault(),
                                data.getPermissionChildren()
                        )
                );
            }

            pluginCommand.setPermission(permission);
        }

        if (data.isPlayerOnly()) {
            this.onlyPlayerCommandSet.add(name);
            this.onlyPlayerCommandSet.addAll(aliases);
        }

        pluginCommand.setExecutor(executor);
        pluginCommand.setTabCompleter(executor);

        final var commandNode = data.getCommandNode();

        if (commandNode != null) {
            this.commodore.register(
                    pluginCommand,
                    (LiteralCommandNode<?>) commandNode,
                    pluginCommand::testPermissionSilent
            );
        }

        synchronized (this.minecraftExecutorMap) {
            this.minecraftExecutorMap.put(name, executor);
        }

        server.getCommandMap().register(this.plugin.getName(), pluginCommand);
    }
}
