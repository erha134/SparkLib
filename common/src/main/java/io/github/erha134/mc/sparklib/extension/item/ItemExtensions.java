package io.github.erha134.mc.sparklib.extension.item;

import io.github.erha134.mc.sparklib.item.stack.ItemStackProvider;
import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public interface ItemExtensions extends Registrable<Item>, ItemStackProvider {
    @Override
    default Registry<Item> sparklib$registry() {
        return Registries.ITEM;
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
