package com.example.test.data.fabric;

import com.example.test.TestMod;
import com.example.test.data.TestModDataGeneration;
import io.github.erha134.mc.sparklib.data.fabric.SDataGenerationFabric;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class TestModDataGenerationFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        TestModDataGeneration.initDataGen(SDataGenerationFabric.create(TestMod.MOD_ID, fabricDataGenerator));
    }
}
