package io.github.erha134.mc.sparklib.registry.impl;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class BlockEntityRegistrySupplier<BE extends BlockEntity> extends WrappedRegistrySupplier<BlockEntityType<BE>> {
    public BlockEntityRegistrySupplier(RegistrySupplier<BlockEntityType<BE>> delegate) {
        super(delegate);
    }

    @Environment(EnvType.CLIENT)
    public BlockEntityRegistrySupplier<BE> blockEntityRenderer(BlockEntityRendererFactory<BE> factory) {
        BlockEntityRendererRegistry.register(this.get(), factory);
        return this;
    }
}
