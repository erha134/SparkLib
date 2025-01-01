package io.github.erha134.mc.sparklib;

import dev.architectury.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SparkLib {
    public static final String MOD_ID = "spark-lib";
    public static final String MOD_NAME = "Spark Lib";
    public static final String MOD_VERSION = Platform.getMod(MOD_ID).getVersion();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {

    }

    private SparkLib() {
        // NO-OP
    }
}
