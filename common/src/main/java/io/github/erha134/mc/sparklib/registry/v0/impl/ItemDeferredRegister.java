package io.github.erha134.mc.sparklib.registry.v0.impl;

import io.github.erha134.mc.sparklib.registry.v0.api.WrappedDeferredRegister;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;
import java.util.function.Supplier;

public class ItemDeferredRegister extends WrappedDeferredRegister<Item> {
    public ItemDeferredRegister(String modId) {
        super(modId, RegistryKeys.ITEM);
    }

    @Override
    public <I extends Item> ItemRegistrySupplier<I> register(String id, Supplier<I> supplier) {
        return new ItemRegistrySupplier<>(this.delegate.register(id, supplier));
    }

    public <I extends Item> ItemRegistrySupplier<I> register(String id, Function<Item.Settings, I> factory) {
        return this.register(id, () -> factory.apply(new Item.Settings()));
    }
}
