package io.github.erha134.mc.sparklib.registry.impl;

import io.github.erha134.mc.sparklib.registry.api.WrappedDeferredRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockDeferredRegister extends WrappedDeferredRegister<Block> {
    public BlockDeferredRegister(String modId) {
        super(modId, RegistryKeys.BLOCK);
    }

    @Override
    public <B extends Block> BlockRegistrySupplier<B> register(String id,
                                                               Supplier<B> supplier) {
        return new BlockRegistrySupplier<>(this.delegate.register(id, supplier));
    }

    public <B extends Block> BlockRegistrySupplier<B> register(String id,
                                                               Function<AbstractBlock.Settings, B> function) {
        return this.register(id, () -> function.apply(AbstractBlock.Settings.create()));
    }
}
