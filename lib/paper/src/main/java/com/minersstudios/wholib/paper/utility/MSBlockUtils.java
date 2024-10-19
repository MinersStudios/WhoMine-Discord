package com.minersstudios.wholib.paper.utility;

import com.minersstudios.wholib.annotation.Path;
import com.minersstudios.wholib.annotation.Resource;
import com.minersstudios.wholib.utility.ChatUtils;
import com.minersstudios.wholib.paper.custom.block.CustomBlockData;
import com.minersstudios.wholib.paper.custom.block.CustomBlockRegistry;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Utility class for {@link CustomBlockData}
 */
public final class MSBlockUtils {
    public static final String NAMESPACED_KEY_REGEX = '(' + Resource.WMBLOCK + "):(" + Path.REGEX + ")";
    public static final Pattern NAMESPACED_KEY_PATTERN = Pattern.compile(NAMESPACED_KEY_REGEX);

    @Contract(" -> fail")
    private MSBlockUtils() throws AssertionError {
        throw new AssertionError("Utility class");
    }

    /**
     * Gets {@link CustomBlockData} item stack from key from
     * {@link CustomBlockRegistry#keySet()} by using
     * {@link CustomBlockRegistry#fromKey(String)} method
     *
     * @param key {@link CustomBlockData} key string
     * @return Optional of {@link ItemStack} object
     *         or empty optional if not found
     * @see CustomBlockRegistry#fromKey(String)
     * @see CustomBlockData#craftItemStack()
     */
    public static @NotNull Optional<ItemStack> getItemStack(final @Nullable String key) {
        return CustomBlockRegistry.fromKey(key).map(CustomBlockData::craftItemStack);
    }

    /**
     * @param string String to be checked
     * @return True if string matches {@link #NAMESPACED_KEY_REGEX}
     */
    @Contract("null -> false")
    public static boolean matchesNamespacedKey(final @Nullable String string) {
        return ChatUtils.isNotBlank(string)
                && NAMESPACED_KEY_PATTERN.matcher(string).matches();
    }
}
