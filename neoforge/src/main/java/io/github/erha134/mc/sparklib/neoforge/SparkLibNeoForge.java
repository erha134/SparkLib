package io.github.erha134.mc.sparklib.neoforge;

import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.client.SparkLibClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(SparkLib.MOD_ID)
public final class SparkLibNeoForge {
    public SparkLibNeoForge(IEventBus bus) {
        SparkLib.init();

        if (FMLEnvironment.dist.isClient()) {
            SparkLibClient.initClient();
        }
    }
}
