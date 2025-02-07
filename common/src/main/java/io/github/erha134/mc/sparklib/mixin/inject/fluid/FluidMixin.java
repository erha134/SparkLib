package io.github.erha134.mc.sparklib.mixin.inject.fluid;

import dev.architectury.extensions.injected.InjectedFluidExtension;
import io.github.erha134.mc.sparklib.extension.fluid.FluidExtensions;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Fluid.class)
public abstract class FluidMixin implements InjectedFluidExtension, FluidExtensions {
    @Shadow
    @Deprecated
    public abstract RegistryEntry.Reference<Fluid> getRegistryEntry();

    @Override
    public RegistryEntry.Reference<Fluid> sparklib$entry() {
        return this.getRegistryEntry();
    }
}
