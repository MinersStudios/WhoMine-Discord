package com.minersstudios.whomine;

import com.google.common.base.Charsets;
import com.minersstudios.whomine.api.utility.ChatUtils;
import com.minersstudios.whomine.api.utility.Font;
import com.minersstudios.whomine.api.utility.SharedConstants;
import com.minersstudios.whomine.chat.ChatType;
import com.minersstudios.whomine.command.api.CommandManager;
import com.minersstudios.whomine.custom.decor.CustomDecorType;
import com.minersstudios.whomine.custom.item.CustomItemType;
import com.minersstudios.whomine.discord.DiscordManager;
import com.minersstudios.whomine.inventory.holder.AbstractInventoryHolder;
import com.minersstudios.whomine.listener.api.ListenerManager;
import com.minersstudios.whomine.listener.impl.event.mechanic.DosimeterMechanic;
import com.minersstudios.whomine.api.locale.TranslationRegistry;
import com.minersstudios.whomine.api.locale.Translations;
import com.minersstudios.whomine.menu.DiscordLinkCodeMenu;
import com.minersstudios.whomine.menu.PronounMenu;
import com.minersstudios.whomine.menu.ResourcePackMenu;
import com.minersstudios.whomine.menu.SkinsMenu;
import com.minersstudios.whomine.player.collection.PlayerInfoMap;
import com.minersstudios.whomine.scheduler.task.BanListTask;
import com.minersstudios.whomine.scheduler.task.MuteMapTask;
import com.minersstudios.whomine.scheduler.task.PlayerListTask;
import com.minersstudios.whomine.scheduler.task.SeatsTask;
import com.minersstudios.whomine.api.status.StatusHandler;
import com.minersstudios.whomine.api.status.StatusWatcher;
import com.minersstudios.whomine.utility.*;
import com.minersstudios.whomine.world.WorldDark;
import com.minersstudios.whomine.world.sound.SoundAdapter;
import com.minersstudios.whomine.world.sound.SoundGroup;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.coreprotect.CoreProtect;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.minersstudios.whomine.api.locale.Translations.*;
import static com.minersstudios.whomine.api.utility.Font.Chars.RED_EXCLAMATION_MARK;
import static net.kyori.adventure.text.Component.text;

@ApiStatus.Internal
public final class WhoMineImpl extends JavaPlugin implements WhoMine {
    static WhoMine singleton;

    private final File configFile;
    private final Cache cache;
    private final Config config;
    private final StatusHandler statusHandler;
    private final ListenerManager listenerManager;
    private final CommandManager commandManager;
    private final DiscordManager discordManager;
    private final Map<Class<? extends AbstractInventoryHolder>, AbstractInventoryHolder> inventoryHolderMap;
    private FileConfiguration newConfig;
    private Scoreboard scoreboardHideTags;
    private Team scoreboardHideTagsTeam;

    static {
        initClass(Font.class);
        initClass(BlockUtils.class);
        initClass(ChatUtils.class);
        initClass(DateUtils.class);
        initClass(LocationUtils.class);
        initClass(SoundGroup.class);
        initClass(SoundAdapter.class);
    }

    public WhoMineImpl() {
        singleton = this;

        this.setupDataFolder();

        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.cache = new Cache(this);
        this.config = new Config(this);
        this.statusHandler = new StatusHandler();
        this.listenerManager = new ListenerManager(this);
        this.commandManager = new CommandManager(this);
        this.discordManager = new DiscordManager(this);
        this.inventoryHolderMap = new Object2ObjectOpenHashMap<>();
    }

    @Override
    public @NotNull File getConfigFile() {
        return this.configFile;
    }

    @Override
    public @NotNull Cache getCache() {
        return this.cache;
    }

    @Override
    public @NotNull Config getConfiguration() {
        return this.config;
    }

    @Override
    public @NotNull StatusHandler getStatusHandler() {
        return this.statusHandler;
    }

    @Override
    public @NotNull ListenerManager getListenerManager() {
        return this.listenerManager;
    }

    @Override
    public @NotNull CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    @Override
    public @NotNull @UnmodifiableView Map<Class<? extends AbstractInventoryHolder>, AbstractInventoryHolder> getInventoryHolderMap() {
        return Collections.unmodifiableMap(this.inventoryHolderMap);
    }

    @Override
    public @NotNull Optional<AbstractInventoryHolder> getInventoryHolder(final @NotNull Class<? extends AbstractInventoryHolder> clazz) {
        return Optional.ofNullable(this.inventoryHolderMap.get(clazz));
    }

    @Override
    public @UnknownNullability Scoreboard getScoreboardHideTags() {
        return this.scoreboardHideTags;
    }

    @Override
    public @UnknownNullability Team getScoreboardHideTagsTeam() {
        return this.scoreboardHideTagsTeam;
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        if (this.newConfig == null) {
            this.reloadConfig();
        }

        return this.newConfig;
    }

    @Override
    public boolean isFullyLoaded() {
        return this.getStatusHandler().containsAll(
                LOADED_DECORATIONS,
                LOADED_BLOCKS,
                LOADED_ITEMS,
                LOADED_RENAMEABLES,
                LOADED_LANGUAGES,
                LOADED_DISCORD,
                LOADED_RESOURCE_PACKS
        );
    }

    @Override
    public void onLoad() {
        final long time = System.currentTimeMillis();

        this.statusHandler.assignStatus(LOADING);

        this.config.reload();

        PacketClassBinder.bindAll();

        TranslationRegistry.bootstrap(this.config.getDefaultLocale());
        initClass(Translations.class);

        ItemUtils.setMaxStackSize(
                Material.LEATHER_HORSE_ARMOR,
                SharedConstants.LEATHER_HORSE_ARMOR_MAX_STACK_SIZE
        );

        PaperUtils
        .editConfig(PaperUtils.ConfigType.GLOBAL, this.getServer())
        .set("messages.kick.connection-throttle",       "<red><lang:" + ERROR_CONNECTION_THROTTLE.getPath() + '>')
        .set("messages.kick.flying-player",             "<red><lang:" + ERROR_FLYING_PLAYER.getPath() + '>')
        .set("messages.kick.flying-vehicle",            "<red><lang:" + ERROR_FLYING_VEHICLE.getPath() + '>')
        .set("messages.no-permission",                  ' ' + RED_EXCLAMATION_MARK + " <red><lang:" + ERROR_NO_PERMISSION.getPath() + '>')
        .set("packet-limiter.kick-message",             "<red><lang:" + ERROR_TOO_MANY_PACKETS.getPath() + '>')
        .set("block-updates.disable-noteblock-updates", true)
        .save();

        CompletableFuture.runAsync(() -> CustomDecorType.load(this));
        CompletableFuture.runAsync(() -> CustomItemType.load(this));

        this.statusHandler.assignStatus(LOADED);
        this.getComponentLogger()
        .info(
                text(
                        "Loaded in " + (System.currentTimeMillis() - time) + "ms",
                        NamedTextColor.GREEN
                )
        );
    }

    @Override
    public void onEnable() {
        final long time = System.currentTimeMillis();

        this.statusHandler.assignStatus(ENABLING);

        this.cache.load();
        this.discordManager.load();
        this.listenerManager.bootstrap();
        this.commandManager.bootstrap();

        this.statusHandler.addWatcher(
                StatusWatcher.builder()
                .statuses(LOADED_DISCORD)
                .successRunnable(
                        () -> new DiscordLinkCodeMenu().register(this)
                )
                .build()
        );
        new PronounMenu().register(this);
        new ResourcePackMenu().register(this);
        new SkinsMenu().register(this);

        this.setupCoreProtect();
        this.setupAuthMe();
        this.setupHideTags();

        this.runTask(() -> this.cache.worldDark = new WorldDark());
        this.runTaskTimer(new SeatsTask(this), 0L, 1L);            // 0.05 seconds
        this.runTaskTimer(new PlayerListTask(this), 6000L, 6000L); // 5 minutes
        this.runTaskTimer(new MuteMapTask(this), 0L, 50L);         // 2.5 seconds
        this.runTaskTimer(new BanListTask(this), 0L, 6000L);       // 5 minutes
        this.runTaskTimerAsync(
                () -> new DosimeterMechanic.DosimeterTask(this).run(),
                0L, this.config.getDosimeterCheckRate()
        );

        this.config.onEnable();

        this.statusHandler.assignStatus(ENABLED);
        if (this.isEnabled()) {
            this.getComponentLogger()
            .info(
                    text(
                            "Enabled in " + (System.currentTimeMillis() - time) + "ms",
                            NamedTextColor.GREEN
                    )
            );
        }
    }

    @Override
    public void onDisable() {
        final long time = System.currentTimeMillis();

        this.statusHandler.assignStatus(DISABLING);

        this.kickAll();
        this.sendServerDisableMessage();
        this.discordManager.unload();
        this.cache.unload();

        this.statusHandler.assignStatus(DISABLED);
        this.getComponentLogger()
        .info(
                text(
                        "Disabled in " + (System.currentTimeMillis() - time) + "ms",
                        NamedTextColor.GREEN
                )
        );
    }

    @Override
    public void reloadConfig() {
        this.newConfig = YamlConfiguration.loadConfiguration(this.configFile);
        final InputStream defaultInput = this.getResource("config.yml");

        if (defaultInput == null) {
            return;
        }

        final InputStreamReader inputReader = new InputStreamReader(defaultInput, Charsets.UTF_8);
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(inputReader);

        this.newConfig.setDefaults(configuration);
    }

    @Override
    public void saveConfig() {
        try {
            this.getConfig().save(this.configFile);
        } catch (final IOException e) {
            this.getLogger().severe("Could not save config to " + this.configFile);
        }
    }

    @Override
    public void saveResource(
            final @NotNull String resourcePath,
            final boolean replace
    ) throws IllegalArgumentException {
        if (ChatUtils.isBlank(resourcePath)) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        final String path = resourcePath.replace('\\', '/');
        final InputStream in = this.getResource(path);

        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + path + "' cannot be found");
        }

        final Logger logger = this.getLogger();
        final String dirPath = path.substring(0, Math.max(path.lastIndexOf('/'), 0));
        final File dataFolder = this.getDataFolder();
        final File outFile = new File(dataFolder, path);
        final File outDir = new File(dataFolder, dirPath);
        final String outFileName = outFile.getName();
        final String outDirName = outDir.getName();

        if (
                !outDir.exists()
                && !outDir.mkdirs()
        ) {
            logger.warning("Directory " + outDirName + " creation failed");
        }

        if (
                !outFile.exists()
                || replace
        ) {
            try (
                    final var out = new FileOutputStream(outFile);
                    in
            ) {
                final byte[] buffer = new byte[1024];
                int read;

                while ((read = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }
            } catch (final IOException e) {
                logger.log(
                        Level.SEVERE,
                        "Could not save " + outFileName + " to " + outFile,
                        e
                );
            }
        } else {
            logger.warning(
                    "Could not save " + outFileName + " to " + outFile + " because " + outFileName + " already exists."
            );
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    @Override
    public @NotNull BukkitTask runTaskAsync(final @NotNull Runnable task) {
        return this.getServer().getScheduler().runTaskAsynchronously(this, task);
    }

    @Override
    public @NotNull BukkitTask runTaskTimerAsync(
            final @NotNull Runnable task,
            final long delay,
            final long period
    ) {
        return this.getServer().getScheduler().runTaskTimerAsynchronously(this, task, delay, period);
    }

    @Override
    public @NotNull BukkitTask runTask(final @NotNull Runnable task) {
        return this.getServer().getScheduler().runTask(this, task);
    }

    @Override
    public @NotNull BukkitTask runTaskLaterAsync(
            final @NotNull Runnable task,
            final long delay
    ) {
        return this.getServer().getScheduler().runTaskLaterAsynchronously(this, task, delay);
    }

    @Override
    public @NotNull BukkitTask runTaskLater(
            final @NotNull Runnable task,
            final long delay
    ) {
        return this.getServer().getScheduler().runTaskLater(this, task, delay);
    }

    @Override
    public @NotNull BukkitTask runTaskTimer(
            final @NotNull Runnable task,
            final long delay,
            final long period
    ) {
        return this.getServer().getScheduler().runTaskTimer(this, task, delay, period);
    }

    @Override
    public void runTaskAsync(final @NotNull Consumer<BukkitTask> task) {
        this.getServer().getScheduler().runTaskAsynchronously(this, task);
    }

    @Override
    public void runTaskTimerAsync(
            final @NotNull Consumer<BukkitTask> task,
            final long delay,
            final long period
    ) {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, task, delay, period);
    }

    @Override
    public void runTask(final @NotNull Consumer<BukkitTask> task) {
        this.getServer().getScheduler().runTask(this, task);
    }

    @Override
    public void runTaskLaterAsync(
            final @NotNull Consumer<BukkitTask> task,
            final long delay
    ) {
        this.getServer().getScheduler().runTaskLaterAsynchronously(this, task, delay);
    }

    @Override
    public void runTaskLater(
            final @NotNull Consumer<BukkitTask> task,
            final long delay
    ) {
        this.getServer().getScheduler().runTaskLater(this, task, delay);
    }

    @Override
    public void runTaskTimer(
            final @NotNull Consumer<BukkitTask> task,
            final long delay,
            final long period
    ) {
        this.getServer().getScheduler().runTaskTimer(this, task, delay, period);
    }

    @Override
    public void openCustomInventory(
            final @NotNull Class<? extends AbstractInventoryHolder> clazz,
            final @NotNull Player player
    ) {
        this.getInventoryHolder(clazz)
        .ifPresent(
                holder -> holder.open(player)
        );
    }

    private void setupCoreProtect() {
        try {
            final CoreProtect coreProtect = CoreProtect.getInstance();

            if (coreProtect == null) {
                MSLogger.warning("CoreProtectAPI is not running yet");
            } else if (coreProtect.isEnabled()) {
                CoreProtectUtils.set(coreProtect.getAPI());
                MSLogger.fine("CoreProtect connected");
            } else {
                MSLogger.warning("CoreProtect is not Enabled, actions logging will not be available");
            }
        } catch (final IllegalStateException e) {
            MSLogger.warning("CoreProtect is already connected");
        } catch (final NoClassDefFoundError e) {
            MSLogger.warning("CoreProtect is not installed, actions logging will not be available");
        }
    }

    private void setupHideTags() {
        this.scoreboardHideTags = this.getServer().getScoreboardManager().getNewScoreboard();
        this.scoreboardHideTagsTeam = this.scoreboardHideTags.registerNewTeam(SharedConstants.HIDE_TAGS_TEAM_NAME);

        this.scoreboardHideTagsTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        this.scoreboardHideTagsTeam.setCanSeeFriendlyInvisibles(false);
    }

    private void setupAuthMe() {
        final Logger logger = this.getLogger();
        final PluginManager pluginManager = this.getServer().getPluginManager();

        try {
            final AuthMe authMe = AuthMeApi.getInstance().getPlugin();

            if (!authMe.isEnabled()) {
                logger.log(
                        Level.SEVERE,
                        "AuthMe is not enabled, MSEssentials will not work properly"
                );
                pluginManager.disablePlugin(this);
            }
        } catch (final Throwable e) {
            logger.log(
                    Level.SEVERE,
                    "AuthMe is not installed, MSEssentials will not work properly"
            );
            pluginManager.disablePlugin(this);
        }
    }

    private void kickAll() {
        final PlayerInfoMap playerInfoMap = this.cache.getPlayerInfoMap();
        final var onlinePlayers = this.getServer().getOnlinePlayers();

        if (
                playerInfoMap != null
                && !playerInfoMap.isEmpty()
                && !onlinePlayers.isEmpty()
        ) {
            for (final var player : onlinePlayers) {
                playerInfoMap
                .get(player)
                .kick(
                        player,
                        ON_DISABLE_MESSAGE_TITLE.asTranslatable(),
                        ON_DISABLE_MESSAGE_SUBTITLE.asTranslatable(),
                        PlayerKickEvent.Cause.RESTART_COMMAND
                );
            }
        }
    }

    private void sendServerDisableMessage() {
        this.discordManager.sendMessage(ChatType.GLOBAL, DISCORD_SERVER_DISABLED.asString());
        this.discordManager.sendMessage(ChatType.LOCAL, DISCORD_SERVER_DISABLED.asString());
    }

    private void setupDataFolder() {
        final File pluginFolder = new File(SharedConstants.GLOBAL_FOLDER_PATH);

        if (
                !pluginFolder.exists()
                && !pluginFolder.mkdirs()
        ) {
            throw new IllegalStateException("Could not create plugin folder");
        }

        try {
            final Field dataFolderField = JavaPlugin.class.getDeclaredField("dataFolder");

            dataFolderField.setAccessible(true);
            dataFolderField.set(this, pluginFolder);
        } catch (final NoSuchFieldException e) {
            throw new IllegalStateException("Could not find data folder field", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not set data folder", e);
        }
    }

    private static void initClass(final @NotNull Class<?> clazz) throws ExceptionInInitializerError {
        try {
            Class.forName(clazz.getName());
        } catch (final Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
