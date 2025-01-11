package io.github.erha134.mc.sparklib.registry.impl;

import io.github.erha134.mc.sparklib.registry.api.WrappedDeferredRegister;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public class BlockEntityDeferredRegister extends WrappedDeferredRegister<BlockEntityType<?>> {
    public BlockEntityDeferredRegister(String modId) {
        super(modId, RegistryKeys.BLOCK_ENTITY_TYPE);
    }

    @Deprecated(forRemoval = true)
    @Override
    public <R extends BlockEntityType<?>> WrappedRegistrySupplier<R> register(String id,
                                                                              Supplier<R> supplier) {
        return super.register(id, supplier);
    }

    public <BE extends BlockEntity> BlockEntityRegistrySupplier<BE> registerType(String id,
                                                                                 Supplier<BlockEntityType<BE>> supplier) {
        return new BlockEntityRegistrySupplier<>(this.delegate.register(id, supplier));
    }

    public <BE extends BlockEntity> BlockEntityRegistrySupplier<BE> registerType(String id,
                                                                                 BlockEntityType.BlockEntityFactory<BE> factory,
                                                                                 Block... validateBlocks) {
        return this.registerType(id, () -> BlockEntityType.Builder.create(factory, validateBlocks).build(null));
    }
}
