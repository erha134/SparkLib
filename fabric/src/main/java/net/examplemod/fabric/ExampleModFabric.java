package net.examplemod.fabric;

import net.examplemod.ExampleMod;
import net.fabricmc.api.ModInitializer;

public final class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.init();
    }
}
