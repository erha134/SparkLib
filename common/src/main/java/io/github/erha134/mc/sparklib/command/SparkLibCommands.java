package io.github.erha134.mc.sparklib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.platform.Platform;
import io.github.erha134.easylib.collection.ListUtils;
import io.github.erha134.mc.sparklib.SparkLib;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SparkLibCommands {
    private static final List<String> DEBUG_COMMANDS = ListUtils.of(
            "gamerule doDaylightCycle false",
            "gamerule doWeatherCycle false",
            "time set noon",
            "weather clear",
            "gamerule keepInventory true",
            "gamerule doTraderSpawning false",
            "gamerule doInsomnia false"
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(literal("sparklib")
                .then(literal("version")
                        .executes(SparkLibCommands::version))
                .then(literal("debug")
                        .requires(s -> s.hasPermissionLevel(2))
                        .executes(SparkLibCommands::debug)));
    }

    private static int version(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.translatable("sparklib.command.version", SparkLib.MOD_VERSION),
                false);

        return SINGLE_SUCCESS;
    }

    private static int debug(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (!Platform.isDevelopmentEnvironment()) {
            source.sendFeedback(() -> Text.translatable("sparklib.command.debug").formatted(Formatting.RED), false);
        }

        DEBUG_COMMANDS.forEach(c -> source.getServer().getCommandManager().executeWithPrefix(source, c));

        return SINGLE_SUCCESS;
    }
}
