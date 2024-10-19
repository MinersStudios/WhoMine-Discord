/**
 * This package contains all the annotations used in the API.
 * <p>
 * <table>
 *     <caption>Available Annotations</caption>
 *     <tr>
 *         <th>Annotation</th>
 *         <th>Description</th>
 *         <th>Example</th>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.wholib.annotation.Resource}</td>
 *         <td>Indicates that the annotated element is a resource string.</td>
 *         <td>
 *             <pre>{@code @Resource String resource = "resource";}</pre>
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.wholib.annotation.Path}</td>
 *         <td>Indicates that the annotated element is a path string.</td>
 *         <td>
 *             <pre>{@code @Path String path = "path";}</pre>
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.wholib.annotation.ResourcePath}</td>
 *         <td>Indicates that the annotated element is a resource path.</td>
 *         <td>
 *             <pre>
 *                 {@code @ResourcePath String resourcedPath = "resource:path";}
 *                 {@code @ResourcePath String resourcedPath = "path";}
 *             </pre>
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>{@link com.minersstudios.wholib.annotation.StatusKey}</td>
 *         <td>
 *             Indicates that the annotated element is a status key string.<p>
 *             Used in the {@link com.minersstudios.wholib.status StatusAPI}.
 *         </td>
 *         <td>
 *             <pre>{@code @StatusKey String statusKey = "STATUS_KEY";}</pre>
 *         </td>
 *     </tr>
 * </table>
 */
package com.minersstudios.wholib.annotation;
