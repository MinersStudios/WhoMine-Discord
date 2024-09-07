package com.minersstudios.whomine.api.packet.type;

import com.minersstudios.whomine.api.packet.PacketBound;
import com.minersstudios.whomine.api.packet.PacketProtocol;
import com.minersstudios.whomine.api.packet.PacketRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;

import static com.minersstudios.whomine.api.packet.PacketBound.CLIENTBOUND;
import static com.minersstudios.whomine.api.packet.PacketBound.SERVERBOUND;

/**
 * This enum represents a play packet type used in the Minecraft server
 * networking. It contains information about packet's {@link PacketBound flow}
 * (CLIENTBOUND or SERVERBOUND), its ID, and its name.
 *
 * @see PacketType
 * @see PacketProtocol
 * @see PacketBound
 * @see PacketRegistry
 * @see <a href="https://wiki.vg/Protocol#Play">Protocol Wiki</a>
 * @version 1.21.1, protocol 767
 */
@SuppressWarnings("unused")
public enum PlayPackets implements PacketType {
    //<editor-fold desc="Play client packets" defaultstate="collapsed">

    CLIENT_BUNDLE_DELIMITER                   (CLIENTBOUND, 0x00, "Bundle Delimiter"),
    CLIENT_SPAWN_ENTITY                       (CLIENTBOUND, 0x01, "Spawn Entity"),
    CLIENT_SPAWN_EXPERIENCE_ORB               (CLIENTBOUND, 0x02, "Spawn Experience Orb"),
    CLIENT_ENTITY_ANIMATION                   (CLIENTBOUND, 0x03, "Entity Animation"),
    CLIENT_AWARD_STATISTICS                   (CLIENTBOUND, 0x04, "Award Statistics"),
    CLIENT_ACKNOWLEDGE_BLOCK_CHANGE           (CLIENTBOUND, 0x05, "Acknowledge Block Change"),
    CLIENT_SET_BLOCK_DESTROY_STAGE            (CLIENTBOUND, 0x06, "Set Block Destroy Stage"),
    CLIENT_BLOCK_ENTITY_DATA                  (CLIENTBOUND, 0x07, "Block Entity Data"),
    CLIENT_BLOCK_ACTION                       (CLIENTBOUND, 0x08, "Block Action"),
    CLIENT_BLOCK_UPDATE                       (CLIENTBOUND, 0x09, "Block Update"),
    CLIENT_BOSS_BAR                           (CLIENTBOUND, 0x0A, "Boss Bar"),
    CLIENT_CHANGE_DIFFICULTY                  (CLIENTBOUND, 0x0B, "Change Difficulty"),
    CLIENT_CHUNK_BATCH_FINISHED               (CLIENTBOUND, 0x0C, "Chunk Batch Finished"),
    CLIENT_CHUNK_BATCH_START                  (CLIENTBOUND, 0x0D, "Chunk Batch Start"),
    CLIENT_CHUNK_BIOMES                       (CLIENTBOUND, 0x0E, "Chunk Biomes"),
    CLIENT_CLEAR_TITLES                       (CLIENTBOUND, 0x0F, "Clear Titles"),
    CLIENT_COMMAND_SUGGESTIONS_RESPONSE       (CLIENTBOUND, 0x10, "Command Suggestions Response"),
    CLIENT_COMMANDS                           (CLIENTBOUND, 0x11, "Commands"),
    CLIENT_CLOSE_CONTAINER                    (CLIENTBOUND, 0x12, "Close Container"),
    CLIENT_SET_CONTAINER_CONTENT              (CLIENTBOUND, 0x13, "Set Container Content"),
    CLIENT_SET_CONTAINER_PROPERTY             (CLIENTBOUND, 0x14, "Set Container Property"),
    CLIENT_SET_CONTAINER_SLOT                 (CLIENTBOUND, 0x15, "Set Container Slot"),
    CLIENT_COOKIE_REQUEST                     (CLIENTBOUND, 0x16, "Cookie Request (play)"),
    CLIENT_SET_COOLDOWN                       (CLIENTBOUND, 0x17, "Set Cooldown"),
    CLIENT_CHAT_SUGGESTIONS                   (CLIENTBOUND, 0x18, "Chat Suggestions"),
    CLIENT_PLUGIN_MESSAGE                     (CLIENTBOUND, 0x19, "Clientbound Plugin Message (play)"),
    CLIENT_DAMAGE_EVENT                       (CLIENTBOUND, 0x1A, "Damage Event"),
    CLIENT_DEBUG_SAMPLE                       (CLIENTBOUND, 0x1B, "Debug Sample"),
    CLIENT_DELETE_MESSAGE                     (CLIENTBOUND, 0x1C, "Delete Message"),
    CLIENT_DISCONNECT                         (CLIENTBOUND, 0x1D, "Disconnect (play)"),
    CLIENT_DISGUISED_CHAT_MESSAGE             (CLIENTBOUND, 0x1E, "Disguised Chat Message"),
    CLIENT_ENTITY_EVENT                       (CLIENTBOUND, 0x1F, "Entity Event"),
    CLIENT_EXPLOSION                          (CLIENTBOUND, 0x20, "Explosion"),
    CLIENT_UNLOAD_CHUNK                       (CLIENTBOUND, 0x21, "Unload Chunk"),
    CLIENT_GAME_EVENT                         (CLIENTBOUND, 0x22, "Game Event"),
    CLIENT_OPEN_HORSE_SCREEN                  (CLIENTBOUND, 0x23, "Open Horse Screen"),
    CLIENT_HURT_ANIMATION                     (CLIENTBOUND, 0x24, "Hurt Animation"),
    CLIENT_INITIALIZE_WORLD_BORDER            (CLIENTBOUND, 0x25, "Initialize World Border"),
    CLIENT_KEEP_ALIVE                         (CLIENTBOUND, 0x26, "Clientbound Keep Alive (play)"),
    CLIENT_CHUNK_DATA_AND_UPDATE_LIGHT        (CLIENTBOUND, 0x27, "Chunk Data and Update Light"),
    CLIENT_WORLD_EVENT                        (CLIENTBOUND, 0x28, "World Event"),
    CLIENT_PARTICLE                           (CLIENTBOUND, 0x29, "Particle"),
    CLIENT_UPDATE_LIGHT                       (CLIENTBOUND, 0x2A, "Update Light"),
    CLIENT_LOGIN                              (CLIENTBOUND, 0x2B, "Login (play)"),
    CLIENT_MAP_DATA                           (CLIENTBOUND, 0x2C, "Map Data"),
    CLIENT_MERCHANT_OFFERS                    (CLIENTBOUND, 0x2D, "Merchant Offers"),
    CLIENT_UPDATE_ENTITY_POSITION             (CLIENTBOUND, 0x2E, "Update Entity Position"),
    CLIENT_UPDATE_ENTITY_POSITION_AND_ROTATION(CLIENTBOUND, 0x2F, "Update Entity Position and Rotation"),
    CLIENT_UPDATE_ENTITY_ROTATION             (CLIENTBOUND, 0x30, "Update Entity Rotation"),
    CLIENT_MOVE_VEHICLE                       (CLIENTBOUND, 0x31, "Move Vehicle"),
    CLIENT_OPEN_BOOK                          (CLIENTBOUND, 0x32, "Open Book"),
    CLIENT_OPEN_SCREEN                        (CLIENTBOUND, 0x33, "Open Screen"),
    CLIENT_OPEN_SIGN_EDITOR                   (CLIENTBOUND, 0x34, "Open Sign Editor"),
    CLIENT_PING                               (CLIENTBOUND, 0x35, "Ping (play)"),
    CLIENT_PING_RESPONSE                      (CLIENTBOUND, 0x36, "Ping Response (play)"),
    CLIENT_PLACE_GHOST_RECIPE                 (CLIENTBOUND, 0x37, "Place Ghost Recipe"),
    CLIENT_PLAYER_ABILITIES                   (CLIENTBOUND, 0x38, "Player Abilities (clientbound)"),
    CLIENT_PLAYER_CHAT_MESSAGE                (CLIENTBOUND, 0x39, "Player Chat Message"),
    CLIENT_END_COMBAT                         (CLIENTBOUND, 0x3A, "End Combat"),
    CLIENT_ENTER_COMBAT                       (CLIENTBOUND, 0x3B, "Enter Combat"),
    CLIENT_COMBAT_DEATH                       (CLIENTBOUND, 0x3C, "Combat Death"),
    CLIENT_PLAYER_INFO_REMOVE                 (CLIENTBOUND, 0x3D, "Player Info Remove"),
    CLIENT_PLAYER_INFO_UPDATE                 (CLIENTBOUND, 0x3E, "Player Info Update"),
    CLIENT_LOOK_AT                            (CLIENTBOUND, 0x3F, "Look At"),
    CLIENT_SYNCHRONIZE_PLAYER_POSITION        (CLIENTBOUND, 0x40, "Synchronize Player Position"),
    CLIENT_UPDATE_RECIPE_BOOK                 (CLIENTBOUND, 0x41, "Update Recipe Book"),
    CLIENT_REMOVE_ENTITIES                    (CLIENTBOUND, 0x42, "Remove Entities"),
    CLIENT_REMOVE_ENTITY_EFFECT               (CLIENTBOUND, 0x43, "Remove Entity Effect"),
    CLIENT_RESET_SCORE                        (CLIENTBOUND, 0x44, "Reset Score"),
    CLIENT_REMOVE_RESOURCE_PACK               (CLIENTBOUND, 0x45, "Remove Resource Pack (play)"),
    CLIENT_ADD_RESOURCE_PACK                  (CLIENTBOUND, 0x46, "Add Resource Pack (play)"),
    CLIENT_RESPAWN                            (CLIENTBOUND, 0x47, "Respawn"),
    CLIENT_SET_HEAD_ROTATION                  (CLIENTBOUND, 0x48, "Set Head Rotation"),
    CLIENT_UPDATE_SECTION_BLOCKS              (CLIENTBOUND, 0x49, "Update Section Blocks"),
    CLIENT_SELECT_ADVANCEMENT_TAB             (CLIENTBOUND, 0x4A, "Select Advancement Tab"),
    CLIENT_SERVER_DATA                        (CLIENTBOUND, 0x4B, "Server Data"),
    CLIENT_SET_ACTION_BAR_TEXT                (CLIENTBOUND, 0x4C, "Set Action Bar Text"),
    CLIENT_SET_BORDER_CENTER                  (CLIENTBOUND, 0x4D, "Set Border Center"),
    CLIENT_SET_BORDER_LERP_SIZE               (CLIENTBOUND, 0x4E, "Set Border Lerp Size"),
    CLIENT_SET_BORDER_SIZE                    (CLIENTBOUND, 0x4F, "Set Border Size"),
    CLIENT_SET_BORDER_WARNING_DELAY           (CLIENTBOUND, 0x50, "Set Border Warning Delay"),
    CLIENT_SET_BORDER_WARNING_DISTANCE        (CLIENTBOUND, 0x51, "Set Border Warning Distance"),
    CLIENT_SET_CAMERA                         (CLIENTBOUND, 0x52, "Set Camera"),
    CLIENT_SET_HELD_ITEM                      (CLIENTBOUND, 0x53, "Set Held Item (clientbound)"),
    CLIENT_SET_CENTER_CHUNK                   (CLIENTBOUND, 0x54, "Set Center Chunk"),
    CLIENT_SET_RENDER_DISTANCE                (CLIENTBOUND, 0x55, "Set Render Distance"),
    CLIENT_SET_DEFAULT_SPAWN_POSITION         (CLIENTBOUND, 0x56, "Set Default Spawn Position"),
    CLIENT_DISPLAY_OBJECTIVE                  (CLIENTBOUND, 0x57, "Display Objective"),
    CLIENT_SET_ENTITY_METADATA                (CLIENTBOUND, 0x58, "Set Entity Metadata"),
    CLIENT_LINK_ENTITIES                      (CLIENTBOUND, 0x59, "Link Entities"),
    CLIENT_SET_ENTITY_VELOCITY                (CLIENTBOUND, 0x5A, "Set Entity Velocity"),
    CLIENT_SET_EQUIPMENT                      (CLIENTBOUND, 0x5B, "Set Equipment"),
    CLIENT_SET_EXPERIENCE                     (CLIENTBOUND, 0x5C, "Set Experience"),
    CLIENT_SET_HEALTH                         (CLIENTBOUND, 0x5D, "Set Health"),
    CLIENT_UPDATE_OBJECTIVES                  (CLIENTBOUND, 0x5E, "Update Objectives"),
    CLIENT_SET_PASSENGERS                     (CLIENTBOUND, 0x5F, "Set Passengers"),
    CLIENT_UPDATE_TEAMS                       (CLIENTBOUND, 0x60, "Update Teams"),
    CLIENT_UPDATE_SCORE                       (CLIENTBOUND, 0x61, "Update Score"),
    CLIENT_SET_SIMULATION_DISTANCE            (CLIENTBOUND, 0x62, "Set Simulation Distance"),
    CLIENT_SET_SUBTITLE_TEXT                  (CLIENTBOUND, 0x63, "Set Subtitle Text"),
    CLIENT_UPDATE_TIME                        (CLIENTBOUND, 0x64, "Update Time"),
    CLIENT_SET_TITLE_TEXT                     (CLIENTBOUND, 0x65, "Set Title Text"),
    CLIENT_SET_TITLE_ANIMATION_TIMES          (CLIENTBOUND, 0x66, "Set Title Animation Times"),
    CLIENT_ENTITY_SOUND_EFFECT                (CLIENTBOUND, 0x67, "Entity Sound Effect"),
    CLIENT_SOUND_EFFECT                       (CLIENTBOUND, 0x68, "Sound Effect"),
    CLIENT_START_CONFIGURATION                (CLIENTBOUND, 0x69, "Start Configuration"),
    CLIENT_STOP_SOUND                         (CLIENTBOUND, 0x6A, "Stop Sound"),
    CLIENT_STORE_COOKIE                       (CLIENTBOUND, 0x6B, "Store Cookie (play)"),
    CLIENT_SYSTEM_CHAT_MESSAGE                (CLIENTBOUND, 0x6C, "System Chat Message"),
    CLIENT_SET_TAB_LIST_HEADER_AND_FOOTER     (CLIENTBOUND, 0x6D, "Set Tab List Header And Footer"),
    CLIENT_TAG_QUERY_RESPONSE                 (CLIENTBOUND, 0x6E, "Tag Query Response"),
    CLIENT_PICKUP_ITEM                        (CLIENTBOUND, 0x6F, "Pickup Item"),
    CLIENT_TELEPORT_ENTITY                    (CLIENTBOUND, 0x70, "Teleport Entity"),
    CLIENT_SET_TICKING_STATE                  (CLIENTBOUND, 0x71, "Set Ticking State"),
    CLIENT_STEP_TICK                          (CLIENTBOUND, 0x72, "Step Tick"),
    CLINET_TRANSFER                           (CLIENTBOUND, 0x73, "Transfer (play)"),
    CLIENT_UPDATE_ADVANCEMENTS                (CLIENTBOUND, 0x74, "Update Advancements"),
    CLIENT_UPDATE_ATTRIBUTES                  (CLIENTBOUND, 0x75, "Update Attributes"),
    CLIENT_ENTITY_EFFECT                      (CLIENTBOUND, 0x76, "Entity Effect"),
    CLIENT_UPDATE_RECIPES                     (CLIENTBOUND, 0x77, "Update Recipes"),
    CLIENT_UPDATE_TAGS                        (CLIENTBOUND, 0x78, "Update Tags (play)"),
    CLIENT_PROJECTILE_POWER                   (CLIENTBOUND, 0x79, "Projectile Power"),
    CLIENT_CUSTOM_REPORT_DETAILS              (CLIENTBOUND, 0x7A, "Custom Report Details"),
    CLIENT_SERVER_LINKS                       (CLIENTBOUND, 0x7B, "Server Links (play)"),

    //</editor-fold>
    //<editor-fold desc="Play server packets" defaultstate="collapsed">

    SERVER_CONFIRM_TELEPORTATION              (SERVERBOUND, 0x00, "Confirm Teleportation"),
    SERVER_QUERY_BLOCK_ENTITY_TAG             (SERVERBOUND, 0x01, "Query Block Entity Tag"),
    SERVER_CHANGE_DIFFICULTY                  (SERVERBOUND, 0x02, "Change Difficulty"),
    SERVER_ACKNOWLEDGE_MESSAGE                (SERVERBOUND, 0x03, "Acknowledge Message"),
    SERVER_CHAT_COMMAND                       (SERVERBOUND, 0x04, "Chat Command"),
    SERVER_SIGNED_CHAT_COMMAND                (SERVERBOUND, 0x05, "Signed Chat Command"),
    SERVER_CHAT_MESSAGE                       (SERVERBOUND, 0x06, "Chat Message"),
    SERVER_PLAYER_SESSION                     (SERVERBOUND, 0x07, "Player Session"),
    SERVER_CHUNK_BATCH_RECEIVED               (SERVERBOUND, 0x08, "Chunk Batch Received"),
    SERVER_CLIENT_STATUS                      (SERVERBOUND, 0x09, "Client Status"),
    SERVER_CLIENT_INFORMATION                 (SERVERBOUND, 0x0A, "Client Information (play)"),
    SERVER_COMMAND_SUGGESTIONS_REQUEST        (SERVERBOUND, 0x0B, "Command Suggestions Request"),
    SERVER_ACKNOWLEDGE_CONFIGURATION          (SERVERBOUND, 0x0C, "Acknowledge Configuration"),
    SERVER_CLICK_CONTAINER_BUTTON             (SERVERBOUND, 0x0D, "Click Container Button"),
    SERVER_CLICK_CONTAINER                    (SERVERBOUND, 0x0E, "Click Container"),
    SERVER_CLOSE_CONTAINER                    (SERVERBOUND, 0x0F, "Close Container"),
    SERVER_CHANGE_CONTAINER_SLOT_STATE        (SERVERBOUND, 0x10, "Change Container Slot State"),
    SERVER_COOKIE_RESPONSE                    (SERVERBOUND, 0x11, "Cookie Response (play)"),
    SERVER_PLUGIN_MESSAGE                     (SERVERBOUND, 0x12, "Serverbound Plugin Message (play)"),
    SERVER_DEBUG_SAMPLE_SUBSCRIPTION          (SERVERBOUND, 0x13, "Debug Sample Subscription"),
    SERVER_EDIT_BOOK                          (SERVERBOUND, 0x14, "Edit Book"),
    SERVER_QUERY_ENTITY_TAG                   (SERVERBOUND, 0x15, "Query Entity Tag"),
    SERVER_INTERACT                           (SERVERBOUND, 0x16, "Interact"),
    SERVER_JIGSAW_GENERATE                    (SERVERBOUND, 0x17, "Jigsaw Generate"),
    SERVER_KEEP_ALIVE                         (SERVERBOUND, 0x18, "Serverbound Keep Alive (play)"),
    SERVER_LOCK_DIFFICULTY                    (SERVERBOUND, 0x19, "Lock Difficulty"),
    SERVER_SET_PLAYER_POSITION                (SERVERBOUND, 0x1A, "Set Player Position"),
    SERVER_SET_PLAYER_POSITION_AND_ROTATION   (SERVERBOUND, 0x1B, "Set Player Position and Rotation"),
    SERVER_SET_PLAYER_ROTATION                (SERVERBOUND, 0x1C, "Set Player Rotation"),
    SERVER_SET_PLAYER_ON_GROUND               (SERVERBOUND, 0x1D, "Set Player On Ground"),
    SERVER_MOVE_VEHICLE                       (SERVERBOUND, 0x1E, "Move Vehicle"),
    SERVER_PADDLE_BOAT                        (SERVERBOUND, 0x1F, "Paddle Boat"),
    SERVER_PICK_ITEM                          (SERVERBOUND, 0x20, "Pick Item"),
    SERVER_PING_REQUEST                       (SERVERBOUND, 0x21, "Ping Request (play)"),
    SERVER_PLACE_RECIPE                       (SERVERBOUND, 0x22, "Place Recipe"),
    SERVER_PLAYER_ABILITIES                   (SERVERBOUND, 0x23, "Player Abilities (serverbound)"),
    SERVER_PLAYER_ACTION                      (SERVERBOUND, 0x24, "Player Action"),
    SERVER_PLAYER_COMMAND                     (SERVERBOUND, 0x25, "Player Command"),
    SERVER_PLAYER_INPUT                       (SERVERBOUND, 0x26, "Player Input"),
    SERVER_PONG                               (SERVERBOUND, 0x27, "Pong (play)"),
    SERVER_CHANGE_RECIPE_BOOK_SETTINGS        (SERVERBOUND, 0x28, "Change Recipe Book Settings"),
    SERVER_SET_SEEN_RECIPE                    (SERVERBOUND, 0x29, "Set Seen Recipe"),
    SERVER_RENAME_ITEM                        (SERVERBOUND, 0x2A, "Rename Item"),
    SERVER_RESOURCE_PACK_RESPONSE             (SERVERBOUND, 0x2B, "Resource Pack Response (play)"),
    SERVER_SEEN_ADVANCEMENTS                  (SERVERBOUND, 0x2C, "Seen Advancements"),
    SERVER_SELECT_TRADE                       (SERVERBOUND, 0x2D, "Select Trade"),
    SERVER_SET_BEACON_EFFECT                  (SERVERBOUND, 0x2E, "Set Beacon Effect"),
    SERVER_SET_HELD_ITEM                      (SERVERBOUND, 0x2F, "Set Held Item (serverbound)"),
    SERVER_PROGRAM_COMMAND_BLOCK              (SERVERBOUND, 0x30, "Program Command Block"),
    SERVER_PROGRAM_COMMAND_BLOCK_MINECART     (SERVERBOUND, 0x31, "Program Command Block Minecart"),
    SERVER_SET_CREATIVE_MODE_SLOT             (SERVERBOUND, 0x32, "Set Creative Mode Slot"),
    SERVER_PROGRAM_JIGSAW_BLOCK               (SERVERBOUND, 0x33, "Program Jigsaw Block"),
    SERVER_PROGRAM_STRUCTURE_BLOCK            (SERVERBOUND, 0x34, "Program Structure Block"),
    SERVER_UPDATE_SIGN                        (SERVERBOUND, 0x35, "Update Sign"),
    SERVER_SWING_ARM                          (SERVERBOUND, 0x36, "Swing Arm"),
    SERVER_TELEPORT_TO_ENTITY                 (SERVERBOUND, 0x37, "Teleport To Entity"),
    SERVER_USE_ITEM_ON                        (SERVERBOUND, 0x38, "Use Item On"),
    SERVER_USE_ITEM                           (SERVERBOUND, 0x39, "Use Item");

    //</editor-fold>

    private static final Map<PacketBound, Int2ObjectMap<PlayPackets>> PACKET_MAP = PacketType.createMap(values());

    private final PacketBound flow;
    private final int id;
    private final String name;

    PlayPackets(
            final @NotNull PacketBound flow,
            final int id,
            final @NotNull String name
    ) {
        this.flow = flow;
        this.id = id;
        this.name = name;
    }

    @Override
    public @NotNull PacketBound getFlow() {
        return this.flow;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull String toString() {
        return this.name() + '{' +
                "bound=" + this.flow +
                ", id=" + this.id +
                ", name='" + this.name + '\'' +
                '}';
    }

    /**
     * Retrieves the packet associated with given flow and ID
     *
     * @param flow The flow of the packet
     * @param id   The ID of the packet
     *
     * @return The packet associated with given flow and ID
     */
    public static @Nullable PlayPackets getPacket(
            final @NotNull PacketBound flow,
            final int id
    ) {
        return PACKET_MAP.get(flow).get(id);
    }

    /**
     * Returns an unmodifiable map of a play packet flows to packet IDs to
     * packet instances
     *
     * @return An unmodifiable map of a play packet flows to packet IDs to
     *         packet instances
     */
    public static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<PlayPackets>> map() {
        return Collections.unmodifiableMap(PACKET_MAP);
    }

    /**
     * Returns an unmodifiable map of a configuration packet flows to packet IDs
     * to {@link PacketType} instances
     *
     * @return An unmodifiable map of a configuration packet flows to packet IDs
     *         to {@link PacketType} instances
     */
    public static @NotNull @Unmodifiable Map<PacketBound, Int2ObjectMap<? extends PacketType>> boundedMap() {
        return Collections.unmodifiableMap(PACKET_MAP);
    }
}
