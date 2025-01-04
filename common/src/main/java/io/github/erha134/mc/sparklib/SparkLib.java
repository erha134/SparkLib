package io.github.erha134.mc.sparklib;

import dev.architectury.platform.Platform;
import io.github.erha134.mc.sparklib.registry.Registrar;
import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SparkLib {
    public static final String MOD_ID = "sparklib";
    public static final String MOD_NAME = "Spark Lib";
    public static final String MOD_VERSION = Platform.getMod(MOD_ID).getVersion();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        Registrar registrar = new Registrar(MOD_ID);
        registrar.item("example_item", new Item(new Item.Settings()));
        registrar.register();
    }

    private SparkLib() {
        // NO-OP
    }
}
