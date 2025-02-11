package com.example.test.client.fabric;

import com.example.test.client.TestModClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class TestModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TestModClient.initClient();
    }
}
