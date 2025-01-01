package net.examplemod.forge;

import dev.architectury.platform.forge.EventBuses;
import net.examplemod.ExampleMod;
import net.examplemod.client.ExampleModClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModForge {
    public ExampleModForge(Dist dist) {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExampleMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ExampleMod.init();

        if (dist.isClient()) {
            ExampleModClient.initClient();
        }
    }
}
