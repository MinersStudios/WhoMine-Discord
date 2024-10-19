package com.minersstudios.whomine;

import com.google.common.base.Charsets;
import com.minersstudios.wholib.locale.TranslationRegistry;
import com.minersstudios.wholib.locale.Translations;
import com.minersstudios.wholib.paper.PaperCache;
import com.minersstudios.wholib.paper.PaperConfig;
import com.minersstudios.wholib.paper.WhoMine;
import com.minersstudios.wholib.paper.utility.*;
import com.minersstudios.wholib.status.StatusHandler;
import com.minersstudios.wholib.status.StatusWatcher;
import com.minersstudios.wholib.utility.ChatUtils;
import com.minersstudios.wholib.utility.Font;
import com.minersstudios.wholib.utility.SharedConstants;
import com.minersstudios.wholib.paper.chat.ChatType;
import com.minersstudios.whomine.command.api.CommandManager;
import com.minersstudios.wholib.paper.custom.decor.CustomDecorType;
import com.minersstudios.wholib.paper.custom.item.CustomItemType;
import com.minersstudios.wholib.paper.discord.DiscordManager;
import com.minersstudios.wholib.paper.gui.PaperGuiManager;
import com.minersstudios.wholib.paper.listener.PaperListenerManager;
import com.minersstudios.whomine.listener.event.block.*;
import com.minersstudios.whomine.listener.event.entity.*;
import com.minersstudios.whomine.listener.event.inventory.*;
import com.minersstudios.whomine.listener.event.mechanic.*;
import com.minersstudios.whomine.listener.event.player.*;
import com.minersstudios.whomine.listener.event.chat.AsyncChatListener;
import com.minersstudios.whomine.listener.event.command.UnknownCommandListener;
import com.minersstudios.whomine.listener.event.hanging.HangingBreakByEntityListener;
import com.minersstudios.whomine.listener.event.server.ServerCommandListener;
import com.minersstudios.whomine.listener.packet.player.PlayerActionListener;
import com.minersstudios.whomine.listener.packet.player.PlayerUpdateSignListener;
import com.minersstudios.whomine.listener.packet.player.SwingArmListener;
import com.minersstudios.whomine.menu.DiscordLinkCodeMenu;
import com.minersstudios.whomine.menu.PronounMenu;
import com.minersstudios.whomine.menu.ResourcePackMenu;
import com.minersstudios.whomine.menu.SkinsMenu;
import com.minersstudios.wholib.paper.player.collection.PlayerInfoMap;
import com.minersstudios.whomine.scheduler.task.BanListTask;
import com.minersstudios.whomine.scheduler.task.MuteMapTask;
import com.minersstudios.whomine.scheduler.task.PlayerListTask;
import com.minersstudios.whomine.scheduler.task.SeatsTask;
import com.minersstudios.wholib.paper.world.WorldDark;
import com.minersstudios.wholib.paper.world.sound.SoundAdapter;
import com.minersstudios.wholib.paper.world.sound.SoundGroup;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.coreprotect.CoreProtect;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.minersstudios.wholib.locale.Translations.*;
import static com.minersstudios.wholib.utility.Font.Chars.RED_EXCLAMATION_MARK;
import static net.kyori.adventure.text.Component.text;

@ApiStatus.Internal
public final class WhoMineImpl extends JavaPlugin implements WhoMine {

    private final StatusHandler statusHandler;
    private final PaperCacheImpl cache;
    private final PaperConfigImpl config;
    private final PaperListenerManager listenerManager;
    private final PaperGuiManager guiManager;
    private final CommandManager commandManager;
    private final DiscordManager discordManager;

    private final File configFile;
    private FileConfiguration newConfig;

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
        SINGLETON.set(this);

        this.setupDataFolder();

        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.cache = new PaperCacheImpl(this);
        this.config = new PaperConfigImpl(this);
        this.statusHandler = new StatusHandler();
        this.listenerManager = new PaperListenerManager(this);
        this.guiManager = new PaperGuiManager(this);
        this.commandManager = new CommandManager(this);
        this.discordManager = new DiscordManager(this);
    }

    @Override
    public @NotNull PaperCache getCache() {
        return this.cache;
    }

    @Override
    public @NotNull PaperConfig getConfiguration() {
        return this.config;
    }

    @Override
    public @NotNull StatusHandler getStatusHandler() {
        return this.statusHandler;
    }

    @Override
    public @NotNull PaperListenerManager getListenerManager() {
        return this.listenerManager;
    }

    @Override
    public @NotNull PaperGuiManager getGuiManager() {
        return this.guiManager;
    }

    @Override
    public @NotNull DiscordManager getDiscordModule() {
        return this.discordManager;
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

        BlockUtils.editBlockStrength(Blocks.NOTE_BLOCK, -1.0f, 3600000.8F);

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
        this.commandManager.bootstrap();
        this.bootstrap();

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

        MSLogger.info(
                "Note block strength: " + Blocks.NOTE_BLOCK.defaultDestroyTime() + ", " +
                "Acacia planks strength: " + Blocks.ACACIA_PLANKS.defaultDestroyTime() + ", " +
                "Note block getDestroySpeed: " + Blocks.NOTE_BLOCK.defaultBlockState().destroySpeed + ", " +
                "Acacia planks getDestroySpeed: " + Blocks.ACACIA_PLANKS.defaultBlockState().destroySpeed
        );

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

        if (defaultInput != null) {
            try(final var inputReader = new InputStreamReader(defaultInput, Charsets.UTF_8)) {
                final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(inputReader);

                this.newConfig.setDefaults(configuration);
            } catch (final Exception e) {
                this.getLogger().severe("Could not load default config from jar");
            }
        }
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

    @NotNull File getConfigFile() {
        return this.configFile;
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

    private void bootstrap() {
        //<editor-fold desc="Discord listeners" defaultstate="collapsed">

        // TODO: Discord listener manager
        //module.getStatusHandler().addWatcher(
        //        StatusWatcher.builder()
        //        .statuses(MainModule.LOADED_DISCORD)
        //        .successRunnable(
        //                () -> {
        //                    this.listenerManager.register(new CommandAutoCompleteInteractionListener());
        //                    this.listenerManager.register(new MessageReceivedListener());
        //                    this.listenerManager.register(new SlashCommandInteractionListener());
        //                }
        //        )
        //        .build()
        //);
        //</editor-fold>

        //<editor-fold desc="Event listeners" defaultstate="collapsed">

        // Block listeners
        this.listenerManager.register(new BlockBreakListener());

        this.listenerManager.register(new BlockDamageListener());
        this.listenerManager.register(new BlockDropItemListener());
        this.listenerManager.register(new BlockExplodeListener());
        this.listenerManager.register(new BlockPistonExtendListener());
        this.listenerManager.register(new BlockPistonRetractListener());
        this.listenerManager.register(new BlockPlaceListener());
        this.listenerManager.register(new NotePlayListener());

        // Chat listeners
        this.listenerManager.register(new AsyncChatListener());

        // Command listeners
        this.listenerManager.register(new UnknownCommandListener());

        // Entity listeners
        this.listenerManager.register(new EntityChangeBlockListener());
        this.listenerManager.register(new EntityDamageByEntityListener());
        this.listenerManager.register(new EntityDamageListener());
        this.listenerManager.register(new EntityDismountListener());
        this.listenerManager.register(new EntityExplodeListener());

        // Hanging listeners
        this.listenerManager.register(new HangingBreakByEntityListener());

        // Inventory listeners
        this.listenerManager.register(new InventoryClickListener());
        this.listenerManager.register(new InventoryCloseListener());
        this.listenerManager.register(new InventoryCreativeListener());
        this.listenerManager.register(new InventoryDragListener());
        this.listenerManager.register(new InventoryOpenListener());
        this.listenerManager.register(new PrepareAnvilListener());
        this.listenerManager.register(new PrepareItemCraftListener());

        // Player listeners
        this.listenerManager.register(new AsyncPlayerPreLoginListener());
        this.listenerManager.register(new PlayerAdvancementDoneListener());
        this.listenerManager.register(new PlayerBucketEmptyListener());
        this.listenerManager.register(new PlayerChangedWorldListener());
        this.listenerManager.register(new PlayerCommandPreprocessListener());
        this.listenerManager.register(new PlayerDeathListener());
        this.listenerManager.register(new PlayerDropItemListener());
        this.listenerManager.register(new PlayerEditBookListener());
        this.listenerManager.register(new PlayerGameModeChangeListener());
        this.listenerManager.register(new PlayerInteractEntityListener());
        this.listenerManager.register(new PlayerInteractListener());
        this.listenerManager.register(new PlayerJoinListener());
        this.listenerManager.register(new PlayerKickListener());
        this.listenerManager.register(new PlayerMoveListener());
        this.listenerManager.register(new PlayerQuitListener());
        this.listenerManager.register(new PlayerResourcePackStatusListener());
        this.listenerManager.register(new PlayerSpawnLocationListener());
        this.listenerManager.register(new PlayerStopSpectatingEntityListener());
        this.listenerManager.register(new PlayerTeleportListener());

        // Server listeners
        this.listenerManager.register(new ServerCommandListener());

        // Mechanic listeners
        this.listenerManager.register(new BanSwordMechanic.EntityDamageByEntity());
        this.listenerManager.register(new BanSwordMechanic.InventoryClick());
        this.listenerManager.register(new CardBoxMechanic.InventoryMoveItem());
        this.listenerManager.register(new CardBoxMechanic.InventoryDrag());
        this.listenerManager.register(new CardBoxMechanic.InventoryClick());
        this.listenerManager.register(new CocaineMechanic.PlayerItemConsume());
        this.listenerManager.register(new DamageableItemMechanic.PlayerItemDamage());
        this.listenerManager.register(new DosimeterMechanic.PlayerSwapHandItems());
        this.listenerManager.register(new DosimeterMechanic.PlayerItemHeld());
        this.listenerManager.register(new DosimeterMechanic.InventoryClick());
        this.listenerManager.register(new DosimeterMechanic.PlayerDropItem());
        this.listenerManager.register(new DosimeterMechanic.PlayerQuit());
        this.listenerManager.register(new DosimeterMechanic.PlayerInteract());
        this.listenerManager.register(new PoopMechanic.PlayerInteract());

        //</editor-fold>

        //<editor-fold desc="Packet listeners" defaultstate="collapsed">

        this.listenerManager.register(new PlayerActionListener());
        this.listenerManager.register(new PlayerUpdateSignListener());
        this.listenerManager.register(new SwingArmListener());

        //</editor-fold>
    }

    private static void initClass(final @NotNull Class<?> clazz) throws ExceptionInInitializerError {
        try {
            Class.forName(clazz.getName());
        } catch (final Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
