package io.github.erha134.mc.sparklib.registry.impl;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.block.BlockProvider;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;

public class BlockRegistrySupplier<B extends Block> extends WrappedRegistrySupplier<B> implements BlockProvider {
    public BlockRegistrySupplier(RegistrySupplier<B> delegate) {
        super(delegate);
    }

    @Override
    public Block sparklib$asBlock() {
        return this.get();
    }

    @Environment(EnvType.CLIENT)
    public BlockRegistrySupplier<B> color(BlockColorProvider color) {
        ColorHandlerRegistry.registerBlockColors(color, this.get());
        return this;
    }

    @Environment(EnvType.CLIENT)
    public BlockRegistrySupplier<B> renderLayer(RenderLayer layer) {
        RenderTypeRegistry.register(layer, this.get());
        return this;
    }
}
