package io.github.erha134.mc.sparklib.fabric;

import io.github.erha134.mc.sparklib.SparkLib;
import net.fabricmc.api.ModInitializer;

public final class SparkLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SparkLib.init();
    }
}
