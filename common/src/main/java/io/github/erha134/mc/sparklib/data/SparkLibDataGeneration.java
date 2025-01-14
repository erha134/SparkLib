package io.github.erha134.mc.sparklib.data;

import io.github.erha134.mc.sparklib.SparkLib;
import net.minecraft.data.DataGenerator;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SparkLibDataGeneration {
    public static void initDataGen(DataGenerator generator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        new SDataGeneration(SparkLib.MOD_ID, generator, registriesFuture).run();
    }
}
