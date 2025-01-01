package io.github.erha134.mc.sparklib.fabric.client;

import io.github.erha134.mc.sparklib.client.SparkLibClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class SparkLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SparkLibClient.initClient();
    }
}
