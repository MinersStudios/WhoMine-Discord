package com.minersstudios.whomine.command.api.minecraft;

import com.minersstudios.whomine.utility.ChatUtils;
import com.mojang.brigadier.tree.CommandNode;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMaps;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.*;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Immutable
public final class CommandData {
    private final String name;
    private final String usage;
    private final String description;
    private final List<String> aliases;
    private final String permission;
    private final PermissionDefault permissionDefault;
    private final Object2BooleanMap<String> permissionChildren;
    private final CommandNode<?> commandNode;
    private final boolean isPlayerOnly;

    private CommandData(final @NotNull Builder builder) {
        this.name = builder.name;
        this.usage = builder.usage;
        this.description = builder.description;
        this.aliases = new ObjectArrayList<>(builder.aliases);
        this.permission = builder.permission;
        this.permissionDefault = builder.permissionDefault;
        this.permissionChildren = new Object2BooleanOpenHashMap<>(builder.permissionChildren);
        this.commandNode = builder.commandNode;
        this.isPlayerOnly = builder.playerOnly;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull String getUsage() {
        return this.usage;
    }

    public @NotNull String getDescription() {
        return this.description;
    }

    public @NotNull @Unmodifiable List<String> getAliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    public @NotNull String getPermission() {
        return this.permission;
    }

    public @NotNull PermissionDefault getPermissionDefault() {
        return this.permissionDefault;
    }

    public @NotNull @Unmodifiable Object2BooleanMap<String> getPermissionChildren() {
        return Object2BooleanMaps.unmodifiable(this.permissionChildren);
    }

    /**
     * Returns the components and its definition for better tab completion
     * <br>
     * <b>Example:</b>
     * <pre>{@code
     * literal("example")
     * .then(
     *       literal("someLiteralArgument1")
     *       .then(argument("some string argument", StringArgumentType.greedyString())))
     * )
     * .then(
     *       literal("someLiteralArgument2")
     *       .then(argument("some integer argument", IntegerArgumentType.integer())))
     * )
     * .build();
     * }</pre>
     *
     * @return CommandNode for {@link Commodore} registration
     */
    public @Nullable CommandNode<?> getCommandNode() {
        return this.commandNode;
    }

    public boolean isPlayerOnly() {
        return this.isPlayerOnly;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.name.hashCode();
        result = prime * result + this.usage.hashCode();
        result = prime * result + this.description.hashCode();
        result = prime * result + this.aliases.hashCode();
        result = prime * result + this.permission.hashCode();
        result = prime * result + this.permissionDefault.hashCode();
        result = prime * result + this.permissionChildren.hashCode();
        result = prime * result + Boolean.hashCode(this.isPlayerOnly);

        return result;
    }

    @Contract("null -> false")
    @Override
    public boolean equals(final @Nullable Object obj) {
        return this == obj
                || (
                        obj instanceof CommandData that
                        && this.name.equals(that.name)
                        && this.usage.equals(that.usage)
                        && this.description.equals(that.description)
                        && this.aliases.equals(that.aliases)
                        && this.permission.equals(that.permission)
                        && this.permissionDefault == that.permissionDefault
                        && this.permissionChildren.keySet().containsAll(that.permissionChildren.keySet())
                        && this.permissionChildren.values().containsAll(that.permissionChildren.values())
                        && this.isPlayerOnly == that.isPlayerOnly
                );
    }

    @Override
    public @NotNull String toString() {
        return "CommandInfo{" +
                "name='" + this.name + '\'' +
                ", usage='" + this.usage + '\'' +
                ", description='" + this.description + '\'' +
                ", aliases=" + this.aliases +
                ", permission='" + this.permission + '\'' +
                ", permissionDefault=" + this.permissionDefault +
                ", permissionParent=" + this.permissionChildren +
                ", isPlayerOnly=" + this.isPlayerOnly +
                '}';
    }

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Contract("_, _ -> new")
    public static @NotNull PermissionEntry permissionEntry(
            final @NotNull String permission,
            final boolean value
    ) {
        return new PermissionEntry(permission, value);
    }

    public static final class Builder {
        private String name;
        private String usage;
        private String description;
        private String[] aliases;
        private String permission;
        private PermissionDefault permissionDefault;
        private Object2BooleanMap<String> permissionChildren;
        private CommandNode<?> commandNode;
        private boolean playerOnly;

        private Builder() {
            this.usage = "";
            this.description = "";
            this.aliases = new String[0];
            this.permission = "";
            this.permissionDefault = PermissionDefault.NOT_OP;
            this.permissionChildren = Object2BooleanMaps.emptyMap();
        }

        public @UnknownNullability String name() {
            return this.name;
        }

        @Contract("_ -> this")
        public @NotNull Builder name(final @NotNull String name) throws IllegalArgumentException {
            if (ChatUtils.isBlank(name)) {
                throw new IllegalArgumentException("Name cannot be blank");
            }

            this.name = name;

            return this;
        }

        public @NotNull String usage() {
            return this.usage;
        }

        @Contract("_ -> this")
        public @NotNull Builder usage(final @NotNull String usage) {
            this.usage = usage;

            return this;
        }

        public @NotNull String description() {
            return this.description;
        }

        @Contract("_ -> this")
        public @NotNull Builder description(final @NotNull String description) {
            this.description = description;

            return this;
        }

        public String @NotNull [] aliases() {
            return this.aliases;
        }

        @Contract("_ -> this")
        public @NotNull Builder aliases(final String @NotNull ... aliases) {
            this.aliases = aliases.clone();

            return this;
        }

        public @NotNull String permission() {
            return this.permission;
        }

        @Contract("_ -> this")
        public @NotNull Builder permission(final @NotNull String permission) {
            this.permission = permission;

            return this;
        }

        public @NotNull PermissionDefault permissionDefault() {
            return this.permissionDefault;
        }

        @Contract("_ -> this")
        public @NotNull Builder permissionDefault(final @NotNull PermissionDefault permissionDefault) {
            this.permissionDefault = permissionDefault;

            return this;
        }

        public @NotNull Object2BooleanMap<String> permissionChildren() {
            return this.permissionChildren;
        }

        @Contract("_ -> this")
        public @NotNull Builder permissionChildren(final PermissionEntry @NotNull ... entries) {
            this.permissionChildren = new Object2BooleanOpenHashMap<>(entries.length);

            for (final var entry : entries) {
                this.permissionChildren.put(entry.getPermission(), entry.getValue());
            }

            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder permissionChildren(
                final String @NotNull [] keys,
                final boolean @NotNull [] values
        ) throws IllegalArgumentException {
            if (keys.length != values.length) {
                throw new IllegalArgumentException("Keys and values must have the same length");
            }

            return this.permissionChildren(new Object2BooleanOpenHashMap<>(keys, values));
        }

        @Contract("_ -> this")
        public @NotNull Builder permissionChildren(final @NotNull Map<String, Boolean> map) {
            this.permissionChildren = new Object2BooleanOpenHashMap<>(map);

            return this;
        }

        public @Nullable CommandNode<?> commandNode() {
            return this.commandNode;
        }

        @Contract("_ -> this")
        public @NotNull Builder commandNode(final @Nullable CommandNode<?> commandNode) {
            this.commandNode = commandNode;

            return this;
        }

        public boolean playerOnly() {
            return this.playerOnly;
        }

        @Contract("_ -> this")
        public @NotNull Builder playerOnly(final boolean isPlayerOnly) {
            this.playerOnly = isPlayerOnly;

            return this;
        }

        @Contract(" -> new")
        public @NotNull CommandData build() throws IllegalStateException {
            if (ChatUtils.isBlank(this.name)) {
                throw new IllegalStateException("Provide a name for the command");
            }

            return new CommandData(this);
        }
    }

    @Immutable
    public static final class PermissionEntry {
        private final String permission;
        private final boolean value;

        private PermissionEntry(
                final @NotNull String permission,
                final boolean value
        ) {
            this.permission = permission;
            this.value = value;
        }

        public @NotNull String getPermission() {
            return this.permission;
        }

        public boolean getValue() {
            return this.value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result = prime * result + this.permission.hashCode();
            result = prime * result + Boolean.hashCode(this.value);

            return result;
        }

        @Contract("null -> false")
        @Override
        public boolean equals(final @Nullable Object obj) {
            return this == obj
                    || (
                            obj instanceof PermissionEntry that
                            && this.permission.equals(that.permission)
                            && this.value == that.value
                    );
        }

        @Override
        public @NotNull String toString() {
            return "PermissionEntry{" +
                    "permission='" + this.permission + '\'' +
                    ", value=" + this.value +
                    '}';
        }
    }
}
