package io.github.erha134.mc.sparklib;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import io.github.erha134.mc.sparklib.command.SparkLibCommands;
import io.github.erha134.mc.sparklib.patched.SIdentifier;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SparkLib {
    public static final String MOD_ID = "sparklib";
    public static final String MOD_NAME = "Spark Lib";
    public static final String MOD_VERSION = Platform.getMod(MOD_ID).getVersion();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        CommandRegistrationEvent.EVENT.register(SparkLibCommands::register);
    }

    private SparkLib() {
        // NO-OP
    }

    public static Identifier id(String path) {
        return SIdentifier.create(MOD_ID, path);
    }
}
