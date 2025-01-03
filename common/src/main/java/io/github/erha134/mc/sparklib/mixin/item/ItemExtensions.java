package io.github.erha134.mc.sparklib.mixin.item;

import io.github.erha134.mc.sparklib.registry.Registrable;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public interface ItemExtensions extends Registrable<Item> {
    @Override
    default Registry<Item> sparklib$registry() {
        return Registries.ITEM;
    }

    @Override
    default RegistryEntry<Item> sparklib$registryEntry() {
        throw new UnsupportedOperationException();
    }
}
