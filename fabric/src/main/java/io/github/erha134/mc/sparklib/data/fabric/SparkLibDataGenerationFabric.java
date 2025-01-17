package io.github.erha134.mc.sparklib.data.fabric;

import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.data.SparkLibDataGeneration;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class SparkLibDataGenerationFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        SparkLibDataGeneration.initDataGen(SDataGenerationFabric.create(SparkLib.MOD_ID, generator));
    }
}
