package io.github.erha134.mc.sparklib.extension.block;

import io.github.erha134.mc.sparklib.block.BlockProvider;
import io.github.erha134.mc.sparklib.item.stack.ItemStackProvider;
import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public interface BlockExtensions extends Registrable<Block>, BlockProvider, ItemStackProvider {
    @Override
    default Registry<Block> sparklib$registry() {
        return Registries.BLOCK;
    }

    @Override
    default RegistryEntry<Block> sparklib$registryEntry() {
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
