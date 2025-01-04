package io.github.erha134.mc.sparklib.registry.impl;

import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.item.ItemProvider;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.minecraft.item.Item;

public class ItemRegistrySupplier<I extends Item> extends WrappedRegistrySupplier<I> implements ItemProvider {
    public ItemRegistrySupplier(RegistrySupplier<I> delegate) {
        super(delegate);
    }

    @Override
    public Item asItem() {
        return this.delegate.get();
    }
}
