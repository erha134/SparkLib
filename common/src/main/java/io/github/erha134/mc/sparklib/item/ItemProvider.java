package io.github.erha134.mc.sparklib.item;

import io.github.erha134.mc.sparklib.item.stack.ItemStackProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public interface ItemProvider extends ItemConvertible, ItemStackProvider {
    @Override
    default ItemStack sparklib$asStack() {
        return new ItemStack(this);
    }
}
