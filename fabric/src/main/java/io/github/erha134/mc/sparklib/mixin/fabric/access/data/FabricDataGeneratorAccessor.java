package io.github.erha134.mc.sparklib.mixin.fabric.access.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.CompletableFuture;

@Mixin(value = FabricDataGenerator.class, remap = false)
public interface FabricDataGeneratorAccessor {
    @Accessor("registriesFuture")
    CompletableFuture<RegistryWrapper.WrapperLookup> getRegistriesFuture();
}
