package io.github.erha134.mc.sparklib.data.fabric;

import io.github.erha134.mc.sparklib.data.SparkLibDataGeneration;
import io.github.erha134.mc.sparklib.mixin.fabric.access.data.FabricDataGeneratorAccessor;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class SparkLibDataGenerationFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        SparkLibDataGeneration.initDataGen(generator, ((FabricDataGeneratorAccessor) (Object) generator).getRegistriesFuture());
    }
}
