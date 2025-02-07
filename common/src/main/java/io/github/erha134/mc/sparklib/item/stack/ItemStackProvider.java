package io.github.erha134.mc.sparklib.item.stack;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public interface ItemStackProvider {
    default ItemStack sparklib$asStack() {
        return new ItemStack((ItemConvertible) this);
    }
}
