package io.github.erha134.mc.sparklib.registry.impl;

import io.github.erha134.mc.sparklib.registry.api.WrappedDeferredRegister;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EntityDeferredRegister extends WrappedDeferredRegister<EntityType<?>> {
    public EntityDeferredRegister(String modId) {
        super(modId, RegistryKeys.ENTITY_TYPE);
    }

    @Deprecated(forRemoval = true)
    @Override
    public <R extends EntityType<?>> WrappedRegistrySupplier<R> register(String id,
                                                                         Supplier<R> supplier) {
        return super.register(id, supplier);
    }

    public <E extends Entity> EntityRegistrySupplier<E> registerType(String id,
                                                                     Supplier<EntityType<E>> supplier) {
        return new EntityRegistrySupplier<>(this.delegate.register(id, supplier));
    }

    public <E extends Entity> EntityRegistrySupplier<E> registerType(String id,
                                                                     EntityType.EntityFactory<E> factory,
                                                                     UnaryOperator<EntityType.Builder<E>> builder) {
        return this.registerType(id, factory, SpawnGroup.MISC, builder);
    }

    public <E extends Entity> EntityRegistrySupplier<E> registerType(String id,
                                                                     EntityType.EntityFactory<E> factory,
                                                                     SpawnGroup group,
                                                                     UnaryOperator<EntityType.Builder<E>> builder) {
        return this.registerType(id, () -> builder.apply(EntityType.Builder.create(factory, group)).build(id));
    }
}
