package io.github.erha134.mc.sparklib.registry.impl;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.item.ItemProvider;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class ItemRegistrySupplier<I extends Item> extends WrappedRegistrySupplier<I> implements ItemProvider {
    public ItemRegistrySupplier(RegistrySupplier<I> delegate) {
        super(delegate);
    }

    @Override
    public Item asItem() {
        return this.get();
    }

    public ItemRegistrySupplier<I> group(RegistrySupplier<ItemGroup> group) {
        CreativeTabRegistry.append(group, this.get());
        return this;
    }

    public ItemRegistrySupplier<I> fuel(int tick) {
        FuelRegistry.register(tick, this.get());
        return this;
    }

    @Environment(EnvType.CLIENT)
    public ItemRegistrySupplier<I> color(ItemColorProvider color) {
        ColorHandlerRegistry.registerItemColors(color, this.get());
        return this;
    }

    @Environment(EnvType.CLIENT)
    public ItemRegistrySupplier<I> modelPredicate(Identifier property, ClampedModelPredicateProvider provider) {
        ItemPropertiesRegistry.register(this.get(), property, provider);
        return this;
    }
}
