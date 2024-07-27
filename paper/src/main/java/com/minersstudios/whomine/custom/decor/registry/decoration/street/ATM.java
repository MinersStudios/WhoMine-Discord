package com.minersstudios.whomine.custom.decor.registry.decoration.street;

import com.minersstudios.whomine.utility.ChatUtils;
import com.minersstudios.whomine.world.sound.SoundGroup;
import com.minersstudios.whomine.custom.decor.CustomDecorDataImpl;
import com.minersstudios.whomine.custom.decor.DecorHitBox;
import com.minersstudios.whomine.custom.decor.Facing;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class ATM extends CustomDecorDataImpl<ATM> {

    @Override
    protected @NotNull Builder builder() {
        final ItemStack itemStack = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setCustomModelData(1152);
        itemMeta.displayName(ChatUtils.createDefaultStyledText("Банкомат"));
        itemStack.setItemMeta(itemMeta);

        return new Builder()
                .key("atm")
                .hitBox(
                        DecorHitBox.builder()
                        .type(DecorHitBox.Type.SOLID)
                        .size(1.0d, 2.0d, 1.0d)
                        .build()
                )
                .facings(Facing.FLOOR)
                .soundGroup(SoundGroup.ANVIL)
                .itemStack(itemStack);
    }
}
