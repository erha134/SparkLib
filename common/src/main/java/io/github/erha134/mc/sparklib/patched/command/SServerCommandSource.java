package io.github.erha134.mc.sparklib.patched.command;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class SServerCommandSource {
    public static void sendFeedback(ServerCommandSource source, Text text) {
        sendFeedback(source, text, false);
    }

    public static void sendFeedback(ServerCommandSource source, Text text, boolean broadcastToOps) {
        source.sendFeedback(() -> text, broadcastToOps);
    }
}
