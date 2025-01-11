package io.github.erha134.mc.sparklib.registry.impl;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.erha134.mc.sparklib.registry.api.WrappedRegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;

import java.util.function.UnaryOperator;

public class EntityRegistrySupplier<E extends Entity> extends WrappedRegistrySupplier<EntityType<E>> {
    public EntityRegistrySupplier(RegistrySupplier<EntityType<E>> delegate) {
        super(delegate);
    }

    public EntityRegistrySupplier<E> attributes(UnaryOperator<DefaultAttributeContainer.Builder> builder) {
        EntityAttributeRegistry.register(() -> (EntityType<LivingEntity>) this.get(), () -> builder.apply(new DefaultAttributeContainer.Builder()));
        return this;
    }

    public EntityRegistrySupplier<E> spawnRestriction(SpawnRestriction.Location location, Heightmap.Type type, SpawnRestriction.SpawnPredicate<E> predicate) {
        SpawnPlacementsRegistry.register(() -> (EntityType<MobEntity>) this.get(), location, type, (SpawnRestriction.SpawnPredicate<MobEntity>) predicate);
        return this;
    }
}
