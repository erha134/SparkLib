package io.github.erha134.mc.sparklib.extension.block;

import io.github.erha134.mc.sparklib.block.BlockProvider;
import io.github.erha134.mc.sparklib.item.stack.ItemStackProvider;
import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

public interface BlockExtensions extends Registrable<Block>, BlockProvider, ItemStackProvider {
    @Override
    default Registry<Block> sparklib$registry() {
        return Registry.BLOCK;
    }

    @Override
    default RegistryEntry.Reference<Block> sparklib$entry() {
        throw new UnsupportedOperationException();
    }

    @Override
    default Block sparklib$asBlock() {
        throw new UnsupportedOperationException();
    }

    @Override
    default ItemStack sparklib$asStack() {
        throw new UnsupportedOperationException();
    }
}
