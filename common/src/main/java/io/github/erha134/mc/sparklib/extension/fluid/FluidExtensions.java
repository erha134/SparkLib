package io.github.erha134.mc.sparklib.extension.fluid;

import io.github.erha134.mc.sparklib.registry.api.Registrable;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public interface FluidExtensions extends Registrable<Fluid> {
    @Override
    default Registry<Fluid> sparklib$registry() {
        return Registries.FLUID;
    }

    @Override
    default RegistryEntry.Reference<Fluid> sparklib$entry() {
        throw new UnsupportedOperationException();
    }
}
