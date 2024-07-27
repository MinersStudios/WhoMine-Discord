package com.minersstudios.whomine.packet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static net.minecraft.network.protocol.PacketFlow.CLIENTBOUND;
import static net.minecraft.network.protocol.PacketFlow.SERVERBOUND;

/**
 * This class represents a packet type used in the Minecraft server networking.
 * It contains information about the packet's flow (CLIENTBOUND or SERVERBOUND),
 * its ID, and its name.
 *
 * @see PacketProtocol
 * @see PacketFlow
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol">Protocol Wiki</a>
 * @version 1.20.4, protocol 765
 */
@SuppressWarnings("unused")
public enum PacketType {
    //<editor-fold desc="Handshaking client packets" defaultstate="collapsed">
    // There are no clientbound packets in the Handshaking state, since the
    // protocol immediately switches to a different state after the client sends
    // the first packet.
    //</editor-fold>
    //<editor-fold desc="Handshaking server packets" defaultstate="collapsed">
    HANDSHAKING_SERVER_HANDSHAKE                   (SERVERBOUND, 0x00, "Handshake"),
    HANDSHAKING_SERVER_LEGACY_SERVER_LIST_PING     (SERVERBOUND, 0xFE, "Legacy Server List Ping"),
    //</editor-fold>
    //<editor-fold desc="Status client packets" defaultstate="collapsed">
    STATUS_CLIENT_STATUS_RESPONSE                  (CLIENTBOUND, 0x00, "Status Response"),
    STATUS_CLIENT_PING_RESPONSE                    (CLIENTBOUND, 0x01, "Ping Response"),
    //</editor-fold>
    //<editor-fold desc="Status server packets" defaultstate="collapsed">
    STATUS_SERVER_STATUS_REQUEST                   (SERVERBOUND, 0x00, "Status Request"),
    STATUS_SERVER_PING_REQUEST                     (SERVERBOUND, 0x01, "Ping Request"),
    //</editor-fold>
    //<editor-fold desc="Login client packets" defaultstate="collapsed">
    LOGIN_CLIENT_DISCONNECT                        (CLIENTBOUND, 0x00, "Disconnect (login)"),
    LOGIN_CLIENT_ENCRYPTION_REQUEST                (CLIENTBOUND, 0x01, "Encryption Request"),
    LOGIN_CLIENT_LOGIN_SUCCESS                     (CLIENTBOUND, 0x02, "Login Success"),
    LOGIN_CLIENT_SET_COMPRESSION                   (CLIENTBOUND, 0x03, "Set Compression"),
    LOGIN_CLIENT_LOGIN_PLUGIN_REQUEST              (CLIENTBOUND, 0x04, "Login Plugin Request"),
    //</editor-fold>
    //<editor-fold desc="Login server packets" defaultstate="collapsed">
    LOGIN_SERVER_LOGIN_START                       (SERVERBOUND, 0x00, "Login Start"),
    LOGIN_SERVER_ENCRYPTION_RESPONSE               (SERVERBOUND, 0x01, "Encryption Response"),
    LOGIN_SERVER_LOGIN_PLUGIN_RESPONSE             (SERVERBOUND, 0x02, "Login Plugin Response"),
    LOGIN_SERVER_LOGIN_ACKNOWLEDGED                (SERVERBOUND, 0x03, "Login Acknowledged"),
    //</editor-fold>
    //<editor-fold desc="Configuration client packets" defaultstate="collapsed">
    CONFIGURATION_CLIENT_PLUGIN_MESSAGE            (CLIENTBOUND, 0x00, "Clientbound Plugin Message (configuration)"),
    CONFIGURATION_CLIENT_DISCONNECT                (CLIENTBOUND, 0x01, "Disconnect (configuration)"),
    CONFIGURATION_CLIENT_FINISH_CONFIGURATION      (CLIENTBOUND, 0x02, "Finish Configuration"),
    CONFIGURATION_CLIENT_KEEP_ALIVE                (CLIENTBOUND, 0x03, "Clientbound Keep Alive (configuration)"),
    CONFIGURATION_CLIENT_PING                      (CLIENTBOUND, 0x04, "Ping (configuration)"),
    CONFIGURATION_CLIENT_REGISTRY_DATA             (CLIENTBOUND, 0x05, "Registry Data"),
    CONFIGURATION_CLIENT_REMOVE_RESOURCE_PACK      (CLIENTBOUND, 0x06, "Remove Resource Pack (configuration)"),
    CONFIGURATION_CLIENT_ADD_RESOURCE_PACK         (CLIENTBOUND, 0x07, "Add Resource Pack (configuration)"),
    CONFIGURATION_CLIENT_FEATURE_FLAGS             (CLIENTBOUND, 0x08, "Feature Flags"),
    CONFIGURATION_CLIENT_UPDATE_TAGS               (CLIENTBOUND, 0x09, "Update Tags"),
    //</editor-fold>
    //<editor-fold desc="Configuration server packets" defaultstate="collapsed">
    CONFIGURATION_SERVER_CLIENT_INFORMATION        (SERVERBOUND, 0x00, "Client Information (configuration)"),
    CONFIGURATION_SERVER_PLUGIN_MESSAGE            (SERVERBOUND, 0x01, "Serverbound Plugin Message (configuration)"),
    CONFIGURATION_SERVER_FINISH_CONFIGURATION      (SERVERBOUND, 0x02, "Finish Configuration"),
    CONFIGURATION_SERVER_KEEP_ALIVE                (SERVERBOUND, 0x03, "Serverbound Keep Alive (configuration)"),
    CONFIGURATION_SERVER_PONG                      (SERVERBOUND, 0x04, "Pong (configuration)"),
    CONFIGURATION_SERVER_RESOURCE_PACK_RESPONSE    (SERVERBOUND, 0x05, "Resource Pack Response (configuration)"),
    //</editor-fold>
    //<editor-fold desc="Play client packets" defaultstate="collapsed">
    PLAY_CLIENT_BUNDLE_DELIMITER                   (CLIENTBOUND, 0x00, "Bundle Delimiter"),
    PLAY_CLIENT_SPAWN_ENTITY                       (CLIENTBOUND, 0x01, "Spawn Entity"),
    PLAY_CLIENT_SPAWN_EXPERIENCE_ORB               (CLIENTBOUND, 0x02, "Spawn Experience Orb"),
    PLAY_CLIENT_ENTITY_ANIMATION                   (CLIENTBOUND, 0x03, "Entity Animation"),
    PLAY_CLIENT_AWARD_STATISTICS                   (CLIENTBOUND, 0x04, "Award Statistics"),
    PLAY_CLIENT_ACKNOWLEDGE_BLOCK_CHANGE           (CLIENTBOUND, 0x05, "Acknowledge Block Change"),
    PLAY_CLIENT_SET_BLOCK_DESTROY_STAGE            (CLIENTBOUND, 0x06, "Set Block Destroy Stage"),
    PLAY_CLIENT_BLOCK_ENTITY_DATA                  (CLIENTBOUND, 0x07, "Block Entity Data"),
    PLAY_CLIENT_BLOCK_ACTION                       (CLIENTBOUND, 0x08, "Block Action"),
    PLAY_CLIENT_BLOCK_UPDATE                       (CLIENTBOUND, 0x09, "Block Update"),
    PLAY_CLIENT_BOSS_BAR                           (CLIENTBOUND, 0x0A, "Boss Bar"),
    PLAY_CLIENT_CHANGE_DIFFICULTY                  (CLIENTBOUND, 0x0B, "Change Difficulty"),
    PLAY_CLIENT_CHUNK_BATCH_FINISHED               (CLIENTBOUND, 0x0C, "Chunk Batch Finished"),
    PLAY_CLIENT_CHUNK_BATCH_START                  (CLIENTBOUND, 0x0D, "Chunk Batch Start"),
    PLAY_CLIENT_CHUNK_BIOMES                       (CLIENTBOUND, 0x0E, "Chunk Biomes"),
    PLAY_CLIENT_CLEAR_TITLES                       (CLIENTBOUND, 0x0F, "Clear Titles"),
    PLAY_CLIENT_COMMAND_SUGGESTIONS_RESPONSE       (CLIENTBOUND, 0x10, "Command Suggestions Response"),
    PLAY_CLIENT_COMMANDS                           (CLIENTBOUND, 0x11, "Commands"),
    PLAY_CLIENT_CLOSE_CONTAINER                    (CLIENTBOUND, 0x12, "Close Container"),
    PLAY_CLIENT_SET_CONTAINER_CONTENT              (CLIENTBOUND, 0x13, "Set Container Content"),
    PLAY_CLIENT_SET_CONTAINER_PROPERTY             (CLIENTBOUND, 0x14, "Set Container Property"),
    PLAY_CLIENT_SET_CONTAINER_SLOT                 (CLIENTBOUND, 0x15, "Set Container Slot"),
    PLAY_CLIENT_SET_COOLDOWN                       (CLIENTBOUND, 0x16, "Set Cooldown"),
    PLAY_CLIENT_CHAT_SUGGESTIONS                   (CLIENTBOUND, 0x17, "Chat Suggestions"),
    PLAY_CLIENT_PLUGIN_MESSAGE                     (CLIENTBOUND, 0x18, "Clientbound Plugin Message (play)"),
    PLAY_CLIENT_DAMAGE_EVENT                       (CLIENTBOUND, 0x19, "Damage Event"),
    PLAY_CLIENT_DELETE_MESSAGE                     (CLIENTBOUND, 0x1A, "Delete Message"),
    PLAY_CLIENT_DISCONNECT                         (CLIENTBOUND, 0x1B, "Disconnect (play)"),
    PLAY_CLIENT_DISGUISED_CHAT_MESSAGE             (CLIENTBOUND, 0x1C, "Disguised Chat Message"),
    PLAY_CLIENT_ENTITY_EVENT                       (CLIENTBOUND, 0x1D, "Entity Event"),
    PLAY_CLIENT_EXPLOSION                          (CLIENTBOUND, 0x1E, "Explosion"),
    PLAY_CLIENT_UNLOAD_CHUNK                       (CLIENTBOUND, 0x1F, "Unload Chunk"),
    PLAY_CLIENT_GAME_EVENT                         (CLIENTBOUND, 0x20, "Game Event"),
    PLAY_CLIENT_OPEN_HORSE_SCREEN                  (CLIENTBOUND, 0x21, "Open Horse Screen"),
    PLAY_CLIENT_HURT_ANIMATION                     (CLIENTBOUND, 0x22, "Hurt Animation"),
    PLAY_CLIENT_INITIALIZE_WORLD_BORDER            (CLIENTBOUND, 0x23, "Initialize World Border"),
    PLAY_CLIENT_KEEP_ALIVE                         (CLIENTBOUND, 0x24, "Clientbound Keep Alive (play)"),
    PLAY_CLIENT_CHUNK_DATA_AND_UPDATE_LIGHT        (CLIENTBOUND, 0x25, "Chunk Data and Update Light"),
    PLAY_CLIENT_WORLD_EVENT                        (CLIENTBOUND, 0x26, "World Event"),
    PLAY_CLIENT_PARTICLE                           (CLIENTBOUND, 0x27, "Particle"),
    PLAY_CLIENT_UPDATE_LIGHT                       (CLIENTBOUND, 0x28, "Update Light"),
    PLAY_CLIENT_LOGIN                              (CLIENTBOUND, 0x29, "Login (play)"),
    PLAY_CLIENT_MAP_DATA                           (CLIENTBOUND, 0x2A, "Map Data"),
    PLAY_CLIENT_MERCHANT_OFFERS                    (CLIENTBOUND, 0x2B, "Merchant Offers"),
    PLAY_CLIENT_UPDATE_ENTITY_POSITION             (CLIENTBOUND, 0x2C, "Update Entity Position"),
    PLAY_CLIENT_UPDATE_ENTITY_POSITION_AND_ROTATION(CLIENTBOUND, 0x2D, "Update Entity Position and Rotation"),
    PLAY_CLIENT_UPDATE_ENTITY_ROTATION             (CLIENTBOUND, 0x2E, "Update Entity Rotation"),
    PLAY_CLIENT_MOVE_VEHICLE                       (CLIENTBOUND, 0x2F, "Move Vehicle"),
    PLAY_CLIENT_OPEN_BOOK                          (CLIENTBOUND, 0x30, "Open Book"),
    PLAY_CLIENT_OPEN_SCREEN                        (CLIENTBOUND, 0x31, "Open Screen"),
    PLAY_CLIENT_OPEN_SIGN_EDITOR                   (CLIENTBOUND, 0x32, "Open Sign Editor"),
    PLAY_CLIENT_PING                               (CLIENTBOUND, 0x33, "Ping (play)"),
    PLAY_CLIENT_PING_RESPONSE                      (CLIENTBOUND, 0x34, "Ping Response (play)"),
    PLAY_CLIENT_PLACE_GHOST_RECIPE                 (CLIENTBOUND, 0x35, "Place Ghost Recipe"),
    PLAY_CLIENT_PLAYER_ABILITIES                   (CLIENTBOUND, 0x36, "Player Abilities"),
    PLAY_CLIENT_PLAYER_CHAT_MESSAGE                (CLIENTBOUND, 0x37, "Player Chat Message"),
    PLAY_CLIENT_END_COMBAT                         (CLIENTBOUND, 0x38, "End Combat"),
    PLAY_CLIENT_ENTER_COMBAT                       (CLIENTBOUND, 0x39, "Enter Combat"),
    PLAY_CLIENT_COMBAT_DEATH                       (CLIENTBOUND, 0x3A, "Combat Death"),
    PLAY_CLIENT_PLAYER_INFO_REMOVE                 (CLIENTBOUND, 0x3B, "Player Info Remove"),
    PLAY_CLIENT_PLAYER_INFO_UPDATE                 (CLIENTBOUND, 0x3C, "Player Info Update"),
    PLAY_CLIENT_LOOK_AT                            (CLIENTBOUND, 0x3D, "Look At"),
    PLAY_CLIENT_SYNCHRONIZE_PLAYER_POSITION        (CLIENTBOUND, 0x3E, "Synchronize Player Position"),
    PLAY_CLIENT_UPDATE_RECIPE_BOOK                 (CLIENTBOUND, 0x3F, "Update Recipe Book"),
    PLAY_CLIENT_REMOVE_ENTITIES                    (CLIENTBOUND, 0x40, "Remove Entities"),
    PLAY_CLIENT_REMOVE_ENTITY_EFFECT               (CLIENTBOUND, 0x41, "Remove Entity Effect"),
    PLAY_CLIENT_RESET_SCORE                        (CLIENTBOUND, 0x42, "Reset Score"),
    PLAY_CLIENT_REMOVE_RESOURCE_PACK               (CLIENTBOUND, 0x43, "Remove Resource Pack (play)"),
    PLAY_CLIENT_ADD_RESOURCE_PACK                  (CLIENTBOUND, 0x44, "Add Resource Pack (play)"),
    PLAY_CLIENT_RESPAWN                            (CLIENTBOUND, 0x45, "Respawn"),
    PLAY_CLIENT_SET_HEAD_ROTATION                  (CLIENTBOUND, 0x46, "Set Head Rotation"),
    PLAY_CLIENT_UPDATE_SECTION_BLOCKS              (CLIENTBOUND, 0x47, "Update Section Blocks"),
    PLAY_CLIENT_SELECT_ADVANCEMENT_TAB             (CLIENTBOUND, 0x48, "Select Advancement Tab"),
    PLAY_CLIENT_SERVER_DATA                        (CLIENTBOUND, 0x49, "Server Data"),
    PLAY_CLIENT_SET_ACTION_BAR_TEXT                (CLIENTBOUND, 0x4A, "Set Action Bar Text"),
    PLAY_CLIENT_SET_BORDER_CENTER                  (CLIENTBOUND, 0x4B, "Set Border Center"),
    PLAY_CLIENT_SET_BORDER_LERP_SIZE               (CLIENTBOUND, 0x4C, "Set Border Lerp Size"),
    PLAY_CLIENT_SET_BORDER_SIZE                    (CLIENTBOUND, 0x4D, "Set Border Size"),
    PLAY_CLIENT_SET_BORDER_WARNING_DELAY           (CLIENTBOUND, 0x4E, "Set Border Warning Delay"),
    PLAY_CLIENT_SET_BORDER_WARNING_DISTANCE        (CLIENTBOUND, 0x4F, "Set Border Warning Distance"),
    PLAY_CLIENT_SET_CAMERA                         (CLIENTBOUND, 0x50, "Set Camera"),
    PLAY_CLIENT_SET_HELD_ITEM                      (CLIENTBOUND, 0x51, "Set Held Item"),
    PLAY_CLIENT_SET_CENTER_CHUNK                   (CLIENTBOUND, 0x52, "Set Center Chunk"),
    PLAY_CLIENT_SET_RENDER_DISTANCE                (CLIENTBOUND, 0x53, "Set Render Distance"),
    PLAY_CLIENT_SET_DEFAULT_SPAWN_POSITION         (CLIENTBOUND, 0x54, "Set Default Spawn Position"),
    PLAY_CLIENT_DISPLAY_OBJECTIVE                  (CLIENTBOUND, 0x55, "Display Objective"),
    PLAY_CLIENT_SET_ENTITY_METADATA                (CLIENTBOUND, 0x56, "Set Entity Metadata"),
    PLAY_CLIENT_LINK_ENTITIES                      (CLIENTBOUND, 0x57, "Link Entities"),
    PLAY_CLIENT_SET_ENTITY_VELOCITY                (CLIENTBOUND, 0x58, "Set Entity Velocity"),
    PLAY_CLIENT_SET_EQUIPMENT                      (CLIENTBOUND, 0x59, "Set Equipment"),
    PLAY_CLIENT_SET_EXPERIENCE                     (CLIENTBOUND, 0x5A, "Set Experience"),
    PLAY_CLIENT_SET_HEALTH                         (CLIENTBOUND, 0x5B, "Set Health"),
    PLAY_CLIENT_UPDATE_OBJECTIVES                  (CLIENTBOUND, 0x5C, "Update Objectives"),
    PLAY_CLIENT_SET_PASSENGERS                     (CLIENTBOUND, 0x5D, "Set Passengers"),
    PLAY_CLIENT_UPDATE_TEAMS                       (CLIENTBOUND, 0x5E, "Update Teams"),
    PLAY_CLIENT_UPDATE_SCORE                       (CLIENTBOUND, 0x5F, "Update Score"),
    PLAY_CLIENT_SET_SIMULATION_DISTANCE            (CLIENTBOUND, 0x60, "Set Simulation Distance"),
    PLAY_CLIENT_SET_SUBTITLE_TEXT                  (CLIENTBOUND, 0x61, "Set Subtitle Text"),
    PLAY_CLIENT_UPDATE_TIME                        (CLIENTBOUND, 0x62, "Update Time"),
    PLAY_CLIENT_SET_TITLE_TEXT                     (CLIENTBOUND, 0x63, "Set Title Text"),
    PLAY_CLIENT_SET_TITLE_ANIMATION_TIMES          (CLIENTBOUND, 0x64, "Set Title Animation Times"),
    PLAY_CLIENT_ENTITY_SOUND_EFFECT                (CLIENTBOUND, 0x65, "Entity Sound Effect"),
    PLAY_CLIENT_SOUND_EFFECT                       (CLIENTBOUND, 0x66, "Sound Effect"),
    PLAY_CLIENT_START_CONFIGURATION                (CLIENTBOUND, 0x67, "Start Configuration"),
    PLAY_CLIENT_STOP_SOUND                         (CLIENTBOUND, 0x68, "Stop Sound"),
    PLAY_CLIENT_SYSTEM_CHAT_MESSAGE                (CLIENTBOUND, 0x69, "System Chat Message"),
    PLAY_CLIENT_SET_TAB_LIST_HEADER_AND_FOOTER     (CLIENTBOUND, 0x6A, "Set Tab List Header And Footer"),
    PLAY_CLIENT_TAG_QUERY_RESPONSE                 (CLIENTBOUND, 0x6B, "Tag Query Response"),
    PLAY_CLIENT_PICKUP_ITEM                        (CLIENTBOUND, 0x6C, "Pickup Item"),
    PLAY_CLIENT_TELEPORT_ENTITY                    (CLIENTBOUND, 0x6D, "Teleport Entity"),
    PLAY_CLIENT_SET_TICKING_STATE                  (CLIENTBOUND, 0x6E, "Set Ticking State"),
    PLAY_CLIENT_STEP_TICK                          (CLIENTBOUND, 0x6F, "Step Tick"),
    PLAY_CLIENT_UPDATE_ADVANCEMENTS                (CLIENTBOUND, 0x70, "Update Advancements"),
    PLAY_CLIENT_UPDATE_ATTRIBUTES                  (CLIENTBOUND, 0x71, "Update Attributes"),
    PLAY_CLIENT_ENTITY_EFFECT                      (CLIENTBOUND, 0x72, "Entity Effect"),
    PLAY_CLIENT_UPDATE_RECIPES                     (CLIENTBOUND, 0x73, "Update Recipes"),
    PLAY_CLIENT_UPDATE_TAGS                        (CLIENTBOUND, 0x74, "Update Tags"),
    //</editor-fold>
    //<editor-fold desc="Play server packets" defaultstate="collapsed">
    PLAY_SERVER_CONFIRM_TELEPORTATION              (SERVERBOUND, 0x00, "Confirm Teleportation"),
    PLAY_SERVER_QUERY_BLOCK_ENTITY_TAG             (SERVERBOUND, 0x01, "Query Block Entity Tag"),
    PLAY_SERVER_CHANGE_DIFFICULTY                  (SERVERBOUND, 0x02, "Change Difficulty"),
    PLAY_SERVER_ACKNOWLEDGE_MESSAGE                (SERVERBOUND, 0x03, "Acknowledge Message"),
    PLAY_SERVER_CHAT_COMMAND                       (SERVERBOUND, 0x04, "Chat Command"),
    PLAY_SERVER_CHAT_MESSAGE                       (SERVERBOUND, 0x05, "Chat Message"),
    PLAY_SERVER_PLAYER_SESSION                     (SERVERBOUND, 0x06, "Player Session"),
    PLAY_SERVER_CHUNK_BATCH_RECEIVED               (SERVERBOUND, 0x07, "Chunk Batch Received"),
    PLAY_SERVER_CLIENT_STATUS                      (SERVERBOUND, 0x08, "Client Status"),
    PLAY_SERVER_CLIENT_INFORMATION                 (SERVERBOUND, 0x09, "Client Information (play)"),
    PLAY_SERVER_COMMAND_SUGGESTIONS_REQUEST        (SERVERBOUND, 0x0A, "Command Suggestions Request"),
    PLAY_SERVER_ACKNOWLEDGE_CONFIGURATION          (SERVERBOUND, 0x0B, "Acknowledge Configuration"),
    PLAY_SERVER_CLICK_CONTAINER_BUTTON             (SERVERBOUND, 0x0C, "Click Container Button"),
    PLAY_SERVER_CLICK_CONTAINER                    (SERVERBOUND, 0x0D, "Click Container"),
    PLAY_SERVER_CLOSE_CONTAINER                    (SERVERBOUND, 0x0E, "Close Container"),
    PLAY_SERVER_CHANGE_CONTAINER_SLOT_STATE        (SERVERBOUND, 0x0F, "Change Container Slot State"),
    PLAY_SERVER_PLUGIN_MESSAGE                     (SERVERBOUND, 0x10, "Serverbound Plugin Message (play)"),
    PLAY_SERVER_EDIT_BOOK                          (SERVERBOUND, 0x11, "Edit Book"),
    PLAY_SERVER_QUERY_ENTITY_TAG                   (SERVERBOUND, 0x12, "Query Entity Tag"),
    PLAY_SERVER_INTERACT                           (SERVERBOUND, 0x13, "Interact"),
    PLAY_SERVER_JIGSAW_GENERATE                    (SERVERBOUND, 0x14, "Jigsaw Generate"),
    PLAY_SERVER_KEEP_ALIVE                         (SERVERBOUND, 0x15, "Serverbound Keep Alive (play)"),
    PLAY_SERVER_LOCK_DIFFICULTY                    (SERVERBOUND, 0x16, "Lock Difficulty"),
    PLAY_SERVER_SET_PLAYER_POSITION                (SERVERBOUND, 0x17, "Set Player Position"),
    PLAY_SERVER_SET_PLAYER_POSITION_AND_ROTATION   (SERVERBOUND, 0x18, "Set Player Position and Rotation"),
    PLAY_SERVER_SET_PLAYER_ROTATION                (SERVERBOUND, 0x19, "Set Player Rotation"),
    PLAY_SERVER_SET_PLAYER_ON_GROUND               (SERVERBOUND, 0x1A, "Set Player On Ground"),
    PLAY_SERVER_MOVE_VEHICLE                       (SERVERBOUND, 0x1B, "Move Vehicle"),
    PLAY_SERVER_PADDLE_BOAT                        (SERVERBOUND, 0x1C, "Paddle Boat"),
    PLAY_SERVER_PICK_ITEM                          (SERVERBOUND, 0x1D, "Pick Item"),
    PLAY_SERVER_PING_REQUEST                       (SERVERBOUND, 0x1E, "Ping Request (play)"),
    PLAY_SERVER_PLACE_RECIPE                       (SERVERBOUND, 0x1F, "Place Recipe"),
    PLAY_SERVER_PLAYER_ABILITIES                   (SERVERBOUND, 0x20, "Player Abilities"),
    PLAY_SERVER_PLAYER_ACTION                      (SERVERBOUND, 0x21, "Player Action"),
    PLAY_SERVER_PLAYER_COMMAND                     (SERVERBOUND, 0x22, "Player Command"),
    PLAY_SERVER_PLAYER_INPUT                       (SERVERBOUND, 0x23, "Player Input"),
    PLAY_SERVER_PONG                               (SERVERBOUND, 0x24, "Pong (play)"),
    PLAY_SERVER_CHANGE_RECIPE_BOOK_SETTINGS        (SERVERBOUND, 0x25, "Change Recipe Book Settings"),
    PLAY_SERVER_SET_SEEN_RECIPE                    (SERVERBOUND, 0x26, "Set Seen Recipe"),
    PLAY_SERVER_RENAME_ITEM                        (SERVERBOUND, 0x27, "Rename Item"),
    PLAY_SERVER_RESOURCE_PACK_RESPONSE             (SERVERBOUND, 0x28, "Resource Pack Response (play)"),
    PLAY_SERVER_SEEN_ADVANCEMENTS                  (SERVERBOUND, 0x29, "Seen Advancements"),
    PLAY_SERVER_SELECT_TRADE                       (SERVERBOUND, 0x2A, "Select Trade"),
    PLAY_SERVER_SET_BEACON_EFFECT                  (SERVERBOUND, 0x2B, "Set Beacon Effect"),
    PLAY_SERVER_SET_HELD_ITEM                      (SERVERBOUND, 0x2C, "Set Held Item"),
    PLAY_SERVER_PROGRAM_COMMAND_BLOCK              (SERVERBOUND, 0x2D, "Program Command Block"),
    PLAY_SERVER_PROGRAM_COMMAND_BLOCK_MINECART     (SERVERBOUND, 0x2E, "Program Command Block Minecart"),
    PLAY_SERVER_SET_CREATIVE_MODE_SLOT             (SERVERBOUND, 0x2F, "Set Creative Mode Slot"),
    PLAY_SERVER_PROGRAM_JIGSAW_BLOCK               (SERVERBOUND, 0x30, "Program Jigsaw Block"),
    PLAY_SERVER_PROGRAM_STRUCTURE_BLOCK            (SERVERBOUND, 0x31, "Program Structure Block"),
    PLAY_SERVER_UPDATE_SIGN                        (SERVERBOUND, 0x32, "Update Sign"),
    PLAY_SERVER_SWING_ARM                          (SERVERBOUND, 0x33, "Swing Arm"),
    PLAY_SERVER_TELEPORT_TO_ENTITY                 (SERVERBOUND, 0x34, "Teleport To Entity"),
    PLAY_SERVER_USE_ITEM_ON                        (SERVERBOUND, 0x35, "Use Item On"),
    PLAY_SERVER_USE_ITEM                           (SERVERBOUND, 0x36, "Use Item");
    //</editor-fold>

    private final PacketFlow flow;
    private final int id;
    private final String name;

    private static final PacketType[] VALUES;
    static final Map<PacketFlow, Int2ObjectMap<PacketType>> HANDSHAKING_PACKET_MAP;
    static final Map<PacketFlow, Int2ObjectMap<PacketType>> STATUS_PACKET_MAP;
    static final Map<PacketFlow, Int2ObjectMap<PacketType>> LOGIN_PACKET_MAP;
    static final Map<PacketFlow, Int2ObjectMap<PacketType>> CONFIGURATION_PACKET_MAP;
    static final Map<PacketFlow, Int2ObjectMap<PacketType>> PLAY_PACKET_MAP;

    static {
        VALUES = values();
        HANDSHAKING_PACKET_MAP =   createMap("HANDSHAKING_CLIENT_",   "HANDSHAKING_SERVER_");
        STATUS_PACKET_MAP =        createMap("STATUS_CLIENT_",        "STATUS_SERVER_");
        LOGIN_PACKET_MAP =         createMap("LOGIN_CLIENT_",         "LOGIN_SERVER_");
        CONFIGURATION_PACKET_MAP = createMap("CONFIGURATION_CLIENT_", "CONFIGURATION_SERVER_");
        PLAY_PACKET_MAP =          createMap("PLAY_CLIENT_",          "PLAY_SERVER_");
    }

    PacketType(
            final @NotNull PacketFlow flow,
            final int id,
            final @NotNull String name
    ) {
        this.flow = flow;
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the flow of the packet (CLIENTBOUND or SERVERBOUND)
     *
     * @return The flow of the packet (CLIENTBOUND or SERVERBOUND)
     */
    public @NotNull PacketFlow getFlow() {
        return this.flow;
    }

    /**
     * Returns the ID of the packet
     *
     * @return The ID of the packet
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name of the packet
     *
     * @return The name of the packet
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Retrieves the packet class associated with this PacketType
     *
     * @return The packet class associated with this PacketType
     */
    public @NotNull Class<?> getPacketClass() {
        return PacketRegistry.getClassFromType(this);
    }

    /**
     * Returns whether the packet is {@link PacketFlow#SERVERBOUND serverbound}
     *
     * @return True if the packet is {@link PacketFlow#SERVERBOUND serverbound},
     *         false otherwise
     */
    public boolean isReceive() {
        return this.flow == SERVERBOUND;
    }

    /**
     * Returns whether the packet is {@link PacketFlow#CLIENTBOUND clientbound}
     *
     * @return True if the packet is {@link PacketFlow#CLIENTBOUND clientbound},
     *         false otherwise
     */
    public boolean isSend() {
        return this.flow == CLIENTBOUND;
    }

    /**
     * Returns the string representation of this packet type
     *
     * @return The string representation of this packet type
     */
    @Override
    public @NotNull String toString() {
        return this.name() + '{' +
                "bound=" + this.flow +
                ", id=" + this.id +
                ", name='" + this.name + '\'' +
                '}';
    }

    /**
     * Returns an unmodifiable map of a handshaking packet flows to packet IDs
     * to PacketType instances
     *
     * @return An unmodifiable map of a handshaking packet flows to packet IDs
     *         to PacketType instances
     */
    public static @NotNull @Unmodifiable Map<PacketFlow, Int2ObjectMap<PacketType>> handshaking() {
        return Collections.unmodifiableMap(HANDSHAKING_PACKET_MAP);
    }

    /**
     * Returns an unmodifiable map of a status packet flows to packet IDs to
     * PacketType instances
     *
     * @return An unmodifiable map of a status packet flows to packet IDs to
     *         PacketType instances
     */
    public static @NotNull @Unmodifiable Map<PacketFlow, Int2ObjectMap<PacketType>> status() {
        return Collections.unmodifiableMap(STATUS_PACKET_MAP);
    }

    /**
     * Returns an unmodifiable map of a login packet flows to packet IDs to
     * PacketType instances
     *
     * @return An unmodifiable map of a login packet flows to packet IDs to
     *         PacketType instances
     */
    public static @NotNull @Unmodifiable Map<PacketFlow, Int2ObjectMap<PacketType>> login() {
        return Collections.unmodifiableMap(LOGIN_PACKET_MAP);
    }

    /**
     * Returns an unmodifiable map of a configuration packet flows to packet IDs
     * to PacketType instances
     *
     * @return An unmodifiable map of a configuration packet flows to packet IDs
     *         to PacketType instances
     */
    public static @NotNull @Unmodifiable Map<PacketFlow, Int2ObjectMap<PacketType>> configuration() {
        return Collections.unmodifiableMap(CONFIGURATION_PACKET_MAP);
    }

    /**
     * Returns an unmodifiable map of a play packet flows to packet IDs to
     * PacketType instances
     *
     * @return An unmodifiable map of a play packet flows to packet IDs to
     *         PacketType instances
     */
    public static @NotNull @Unmodifiable Map<PacketFlow, Int2ObjectMap<PacketType>> play() {
        return Collections.unmodifiableMap(PLAY_PACKET_MAP);
    }

    /**
     * Retrieves the PacketType corresponding to a given packet class
     *
     * @param clazz The packet class for which to retrieve the PacketType
     * @return The PacketType for the given packet class, or null if not found
     */
    public static @Nullable PacketType fromClass(final @NotNull Class<?> clazz) {
        return PacketRegistry.getTypeFromClass(clazz);
    }

    private static @NotNull Map<PacketFlow, Int2ObjectMap<PacketType>> createMap(
            final @NotNull String clientboundPrefix,
            final @NotNull String serverboundPrefix
    ) {
        final var clientboundMap = new Int2ObjectOpenHashMap<PacketType>();
        final var serverboundMap = new Int2ObjectOpenHashMap<PacketType>();

        for (final var value : VALUES) {
            final String name = value.name();

            if (name.startsWith(clientboundPrefix)) {
                clientboundMap.put(value.id, value);
            } else if (name.startsWith(serverboundPrefix)) {
                serverboundMap.put(value.id, value);
            }
        }

        final var flowMap = new EnumMap<PacketFlow, Int2ObjectMap<PacketType>>(PacketFlow.class);

        flowMap.put(CLIENTBOUND, clientboundMap);
        flowMap.put(SERVERBOUND, serverboundMap);

        return flowMap;
    }
}
