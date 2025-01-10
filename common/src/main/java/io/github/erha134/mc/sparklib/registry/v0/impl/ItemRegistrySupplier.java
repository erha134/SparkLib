package io.github.erha134.mc.sparklib.registry.v0.impl;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.item.ItemProvider;
import io.github.erha134.mc.sparklib.registry.v0.api.WrappedRegistrySupplier;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemRegistrySupplier<I extends Item> extends WrappedRegistrySupplier<I> implements ItemProvider {
    public ItemRegistrySupplier(RegistrySupplier<I> delegate) {
        super(delegate);
    }

    @Override
    public Item asItem() {
        return this.delegate.get();
    }

    public ItemRegistrySupplier<I> group(RegistrySupplier<ItemGroup> group) {
        CreativeTabRegistry.append(group, this);
        return this;
    }

    public ItemRegistrySupplier<I> fuel(int tick) {
        FuelRegistry.register(tick, this);
        return this;
    }

    public ItemRegistrySupplier<I> color(ItemColorProvider color) {
        ColorHandlerRegistry.registerItemColors(color, this);
        return this;
    }
}
