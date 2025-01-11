package io.github.erha134.mc.sparklib.registry.impl;

import io.github.erha134.mc.sparklib.registry.api.WrappedDeferredRegister;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public class RecipeTypeDeferredRegister extends WrappedDeferredRegister<RecipeType<?>> {
    public RecipeTypeDeferredRegister(String modId) {
        super(modId, RegistryKeys.RECIPE_TYPE);
    }

    @Deprecated(forRemoval = true)
    @Override
    public <R extends RecipeType<?>> WrappedRegistrySupplier<R> register(String id, Supplier<R> supplier) {
        return super.register(id, supplier);
    }

    public <R extends Recipe<?>> WrappedRegistrySupplier<RecipeType<R>> registerType(String id) {
        return new WrappedRegistrySupplier<>(this.delegate.register(id, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return id;
            }
        }));
    }
}
