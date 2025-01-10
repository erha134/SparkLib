package io.github.erha134.mc.sparklib.registry.v0.impl;

import io.github.erha134.mc.sparklib.registry.v0.api.DoubleWrappedDeferredRegister;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockDeferredRegister extends DoubleWrappedDeferredRegister<Block, Item> {
    public BlockDeferredRegister(String modId) {
        super(modId, RegistryKeys.BLOCK, RegistryKeys.ITEM);
    }

    @Override
    public <S1 extends Block, S2 extends Item> BlockRegistrySupplier<S1, S2> register(String id,
                                                                                      Supplier<S1> blockSupplier,
                                                                                      Supplier<S2> itemSupplier) {
        return new BlockRegistrySupplier<>(this.delegate1.register(id, blockSupplier), this.delegate2.register(id, itemSupplier));
    }

    public <S1 extends Block, S2 extends Item> BlockRegistrySupplier<S1, S2> register(String id,
                                                                                      Supplier<S1> blockSupplier,
                                                                                      BiFunction<S1, Item.Settings, S2> itemFactory) {
        return this.register(id, blockSupplier, () -> itemFactory.apply(blockSupplier.get(), new Item.Settings()));
    }

    public <S1 extends Block, S2 extends Item> BlockRegistrySupplier<S1, S2> register(String id,
                                                                                      Function<AbstractBlock.Settings, S1> blockFactory,
                                                                                      BiFunction<S1, Item.Settings, S2> itemFactory) {
        S1 block = blockFactory.apply(AbstractBlock.Settings.create());
        return this.register(id, () -> block, () -> itemFactory.apply(block, new Item.Settings()));
    }
}
