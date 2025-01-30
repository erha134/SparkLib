package io.github.erha134.mc.sparklib.data.fabric;

import io.github.erha134.mc.sparklib.data.SDataGeneration;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SDataGenerationFabric {
    public static SDataGeneration create(String modId, FabricDataGenerator generator) {
        return new SDataGeneration(modId, generator);
    }
}
