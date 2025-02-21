package io.github.erha134.mc.sparklib.forge;

import io.github.erha134.mc.sparklib.SparkLib;
import io.github.erha134.mc.sparklib.client.SparkLibClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(SparkLib.MOD_ID)
public final class SparkLibForge {
    public SparkLibForge() {
        SparkLib.init();

        if (FMLEnvironment.dist.isClient()) {
            SparkLibClient.initClient();
        }


    }
}
