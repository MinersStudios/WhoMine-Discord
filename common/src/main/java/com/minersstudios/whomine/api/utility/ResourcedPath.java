package com.minersstudios.whomine.api.utility;

import com.minersstudios.whomine.api.annotation.Path;
import com.minersstudios.whomine.api.annotation.Resource;
import com.minersstudios.whomine.api.annotation.ResourcePath;
import com.minersstudios.whomine.api.throwable.InvalidResourceException;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;

/**
 * Represents a resourced-path used in the plugin.
 * <p>
 * You can create a new resourced-path using the following methods:
 * <table>
 *     <tr>
 *         <th>Method</th>
 *         <th>Description</th>
 *         <th>Example</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #empty(String)}</td>
 *         <td>Creates a new resourced-path with the given path and empty
 *             resource</td>
 *         <td>{@code ResourcedPath.empty("path")}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #minecraft(String)}</td>
 *         <td>Creates a new resourced-path with the given path and the
 *             {@link Resource#MINECRAFT minecraft} resource</td>
 *         <td>{@code ResourcedPath.minecraft("path")}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #realms(String)}</td>
 *         <td>Creates a new resourced-path with the given path and the
 *             {@link Resource#REALMS realms} resource</td>
 *         <td>{@code ResourcedPath.realms("path")}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #paper(String)}</td>
 *         <td>Creates a new resourced-path with the given path and the
 *             {@link Resource#PAPER paper} resource</td>
 *         <td>{@code ResourcedPath.paper("path")}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #whomine(String)}</td>
 *         <td>Creates a new resourced-path with the given path and the
 *             {@link Resource#WHOMINE whomine} resource</td>
 *         <td>{@code ResourcedPath.whomine("path")}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(String, String)}</td>
 *         <td>Creates a new resourced-path with the given resource and path</td>
 *         <td>{@code ResourcedPath.of("resource", "path")}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #of(String)}</td>
 *         <td>Creates a new resourced-path with the given resourced-path string</td>
 *         <td>{@code ResourcedPath.of("resource:path")}</td>
 *     </tr>
 * </table>
 *
 * @see Resource
 * @see Path
 * @see ResourcePath
 */
@SuppressWarnings("unused")
@Immutable
public final class ResourcedPath {

    private final String resource;
    private final String path;

    private ResourcedPath(
            final @Resource @Nullable String resource,
            final @Path @NotNull String path
    ) throws InvalidResourceException {
        this.resource = resource == null ? "" : resource;
        this.path = path;
    }

    /**
     * Returns the resource of the resourced-path
     *
     * @return The resource of the resourced-path
     */
    public @NotNull String getResource() {
        return this.resource;
    }

    /**
     * Returns the path of the resourced-path
     *
     * @return The path of the resourced-path
     */
    public @NotNull String getPath() {
        return this.path;
    }

    /**
     * Returns a hash code value for the resourced-path
     *
     * @return A hash code value for the resourced-path
     */
    @Override
    public int hashCode() {
        return 31 * this.resource.hashCode() + this.path.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one
     *
     * @param obj The reference object with which to compare
     * @return True if this object is the same as the obj argument
     */
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof ResourcedPath that
                        && this.resource.equals(that.resource)
                        && this.path.equals(that.path)
                );
    }

    /**
     * Returns the string representation of the resourced-path
     *
     * @return The string representation of the resourced-path
     */
    @Override
    public @NotNull String toString() {
        return ChatUtils.isBlank(this.resource)
               ? this.path
               : this.resource + ':' + this.path;
    }

    /**
     * Creates a new resourced-path with the given path and no resource
     *
     * @param path The path of the resourced-path
     * @return A new resourced-path with the given path and no resource
     * @throws InvalidResourceException If the path is invalid
     */
    @Contract("_ -> new")
    public static @NotNull ResourcedPath empty(final @Path @NotNull String path) throws InvalidResourceException {
        return dummyResource(null, path);
    }

    /**
     * Creates a new resourced-path with the given path and the resource
     * {@link Resource#MINECRAFT minecraft}
     *
     * @param path The path of the resourced-path
     * @return A new resourced-path with the given path and the {@code minecraft}
     *         resource
     * @throws InvalidResourceException If the path is invalid
     */
    @Contract("_ -> new")
    public static @NotNull ResourcedPath minecraft(final @Path @NotNull String path) throws InvalidResourceException {
        return dummyResource(Resource.MINECRAFT, path);
    }

    /**
     * Creates a new resourced-path with the given path and the resource
     * {@link Resource#REALMS realms}
     *
     * @param path The path of the resourced-path
     * @return A new resourced-path with the given path and the {@code realms}
     *         resource
     * @throws InvalidResourceException If the path is invalid
     */
    @Contract("_ -> new")
    public static @NotNull ResourcedPath realms(final @Path @NotNull String path) throws InvalidResourceException {
        return dummyResource(Resource.REALMS, path);
    }

    /**
     * Creates a new resourced-path with the given path and the resource
     * {@link Resource#PAPER paper}
     *
     * @param path The path of the resourced-path
     * @return A new resourced-path with the given path and the {@code paper}
     *         resource
     * @throws InvalidResourceException If the path is invalid
     */
    @Contract("_ -> new")
    public static @NotNull ResourcedPath paper(final @Path @NotNull String path) throws InvalidResourceException {
        return dummyResource(Resource.PAPER, path);
    }

    /**
     * Creates a new resourced-path with the given path and the resource
     * {@link Resource#WHOMINE whomine}
     *
     * @param path The path of the resourced-path
     * @return A new resourced-path with the given path and the {@code whomine}
     *         resource
     * @throws InvalidResourceException If the path is invalid
     */
    @Contract("_ -> new")
    public static @NotNull ResourcedPath whomine(final @Path @NotNull String path) throws InvalidResourceException {
        return dummyResource(Resource.WHOMINE, path);
    }

    /**
     * Creates a new resourced-path with the given resource and path
     *
     * @param resource The resource of the resourced-path
     * @param path     The path of the resourced-path
     * @return A new resourced-path with the given resource and path
     * @throws InvalidResourceException If the resource or path is invalid
     */
    @Contract("_, _ -> new")
    public static @NotNull ResourcedPath of(
            final @Resource @Nullable String resource,
            final @Path @NotNull String path
    ) throws InvalidResourceException {
        Resource.Validator.validate(resource);

        return dummyResource(resource, path);
    }

    /**
     * Creates a new resourced-path with the given resourced-path string
     *
     * @param resourcedPath The resourced-path string
     * @return A new resourced-path with the given resourced-path string
     * @throws InvalidResourceException If the resourced-path is invalid
     */
    @Contract("_ -> new")
    public static @NotNull ResourcedPath of(final @ResourcePath @NotNull String resourcedPath) throws InvalidResourceException {
        ResourcePath.Validator.validateLength(resourcedPath);

        final int colonIndex = resourcedPath.indexOf(':');

        @Subst("resource") String resource = null;
        @Subst("path") String path = resourcedPath;

        if (colonIndex >= 0) {
            path = resourcedPath.substring(colonIndex + 1);

            if (colonIndex >= 1) {
                resource = resourcedPath.substring(0, colonIndex);
            }
        }

        Resource.Validator.validate(resource);
        Path.Validator.validate(path);

        return new ResourcedPath(resource, path);
    }

    /**
     * Creates a new resourced-path with the given resource and path.
     * <p>
     * The resource is not validated.
     *
     * @param resource The resource of the resourced-path
     * @param path     The path of the resourced-path
     * @return A new resourced-path with the given resource and path
     * @throws InvalidResourceException If the path is invalid
     */
    @Contract("_, _ -> new")
    private static @NotNull ResourcedPath dummyResource(
            final @Resource @Nullable String resource,
            final @Path @NotNull String path
    ) throws InvalidResourceException {
        Path.Validator.validate(path);

        final @Subst("resource:path") String resourcedPath = resource + ':' + path;

        ResourcePath.Validator.validateLength(resourcedPath);

        return new ResourcedPath(resource, path);
    }
}
