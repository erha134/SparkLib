package io.github.erha134.mc.sparklib.data.fabric;

import io.github.erha134.mc.sparklib.data.SDataGeneration;
import io.github.erha134.mc.sparklib.mixin.fabric.access.data.FabricDataGeneratorAccessor;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SDataGenerationFabric {
    public static SDataGeneration create(String modId, FabricDataGenerator generator) {
        return new SDataGeneration(modId, generator, ((FabricDataGeneratorAccessor) (Object) generator).getRegistriesFuture());
    }
}
