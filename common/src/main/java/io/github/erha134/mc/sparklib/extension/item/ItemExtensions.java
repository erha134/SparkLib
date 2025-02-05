package io.github.erha134.mc.sparklib.extension.item;

import io.github.erha134.mc.sparklib.item.stack.ItemStackProvider;
import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

public interface ItemExtensions extends Registrable<Item>, ItemStackProvider {
    @Override
    default Registry<Item> sparklib$registry() {
        return Registry.ITEM;
    }

    @Override
    default RegistryEntry.Reference<Item> sparklib$entry() {
        throw new UnsupportedOperationException();
    }

    @Override
    default ItemStack sparklib$asStack() {
        throw new UnsupportedOperationException();
    }
}
