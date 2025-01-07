package io.github.erha134.mc.sparklib.registry.impl;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.block.BlockProvider;
import io.github.erha134.mc.sparklib.item.ItemProvider;
import io.github.erha134.mc.sparklib.registry.api.DoubleWrappedRegisterSupplier;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BlockRegistrySupplier<B extends Block, I extends Item>
        extends DoubleWrappedRegisterSupplier<B, I>
        implements BlockProvider, ItemProvider {
    public BlockRegistrySupplier(RegistrySupplier<B> delegate1, RegistrySupplier<I> delegate2) {
        super(delegate1, delegate2);
    }

    @Override
    public Block sparklib$asBlock() {
        return this.delegate1.get();
    }

    @Override
    public Item asItem() {
        return this.delegate2.get();
    }

    public BlockRegistrySupplier<B, I> group(RegistrySupplier<ItemGroup> group) {
        CreativeTabRegistry.append(group, this);
        return this;
    }

    public BlockRegistrySupplier<B, I> fuel(int tick) {
        FuelRegistry.register(tick, this);
        return this;
    }

    public BlockRegistrySupplier<B, I> itemColor(ItemColorProvider color) {
        ColorHandlerRegistry.registerItemColors(color, this);
        return this;
    }

    public BlockRegistrySupplier<B, I> blockColor(BlockColorProvider color) {
        ColorHandlerRegistry.registerBlockColors(color, this.sparklib$asBlock());
        return this;
    }

    public BlockRegistrySupplier<B, I> renderLayer(RenderLayer layer) {
        RenderTypeRegistry.register(layer, this.sparklib$asBlock());
        return this;
    }
}
