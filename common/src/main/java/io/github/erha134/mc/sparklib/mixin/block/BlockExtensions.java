package io.github.erha134.mc.sparklib.mixin.block;

import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public interface BlockExtensions extends Registrable<Block> {
    @Override
    default Registry<Block> sparklib$registry() {
        return Registries.BLOCK;
    }

    @Override
    default RegistryEntry<Block> sparklib$registryEntry() {
        throw new UnsupportedOperationException();
    }
}
