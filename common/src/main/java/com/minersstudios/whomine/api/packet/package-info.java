/**
 * This package contains classes for handling packets.
 * <p>
 * <table>
 *     <caption>Available Classes</caption>
 *     <tr>
 *         <th>Class</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.whomine.api.packet.PacketContainer}</td>
 *         <td>Represents a packet container. It contains the packet and the
 *         packet type. The packet can be modified, but the packet type cannot
 *         be changed.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.whomine.api.packet.PacketEvent}</td>
 *         <td>Represents a packet event. It contains the packet container and
 *         the connection who sent or received the packet.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.whomine.api.packet.PacketListener}</td>
 *         <td>Represents a packet listener. It listens for packet events and
 *         handles them.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.whomine.api.packet.PacketRegistry}</td>
 *         <td>Represents a packet registry, which contains the packet maps.
 *         The packet registry contains two packet maps:
 *         <ul>
 *             <li>The path packet map, which maps the packet's path to the
 *             packet type</li>
 *             <li>The class packet map, which maps the packet's class to the
 *             packet type</li>
 *         </ul>
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.whomine.api.packet.PacketType}</td>
 *         <td>Represents a packet type used in the Minecraft server networking.
 *         It contains information about the packet's bound, its ID, and its
 *         resourced-path.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.whomine.api.packet.PacketBound}</td>
 *         <td>Represents a packet's bound in the Minecraft server networking.
 *         Available bounds are:
 *         <ul>
 *             <li>{@link com.minersstudios.whomine.api.packet.PacketBound#SERVERBOUND}
 *             - from the client to the server</li>
 *             <li>{@link com.minersstudios.whomine.api.packet.PacketBound#CLIENTBOUND}
 *             - from the server to the client</li>
 *         </ul>
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.whomine.api.packet.PacketProtocol}</td>
 *         <td>Represents a packet protocol in the Minecraft server networking.
 *         Available protocols are:
 *         <ul>
 *             <li>{@link com.minersstudios.whomine.api.packet.PacketProtocol#HANDSHAKING}
 *             - the handshake protocol</li>
 *             <li>{@link com.minersstudios.whomine.api.packet.PacketProtocol#PLAY}
 *             - the play protocol</li>
 *             <li>{@link com.minersstudios.whomine.api.packet.PacketProtocol#STATUS}
 *             - the status protocol</li>
 *             <li>{@link com.minersstudios.whomine.api.packet.PacketProtocol#LOGIN}
 *             - the login protocol</li>
 *             <li>{@link com.minersstudios.whomine.api.packet.PacketProtocol#CONFIGURATION}
 *             - the configuration protocol</li>
 *          </ul>
 *          </td>
 *     </tr>
 * </table>
 *
 *
 * @see <a href="https://wiki.vg/Protocol">Protocol Wiki</a>
 * @see com.minersstudios.whomine.api.packet.registry Packet type registries
 * @see com.minersstudios.whomine.api.packet.collection Packet collections
 */
package com.minersstudios.whomine.api.packet;
