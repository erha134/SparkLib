package io.github.erha134.mc.sparklib.registry.impl;

import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.block.BlockProvider;
import io.github.erha134.mc.sparklib.item.ItemProvider;
import io.github.erha134.mc.sparklib.registry.api.DoubleWrappedRegisterSupplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

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
}
