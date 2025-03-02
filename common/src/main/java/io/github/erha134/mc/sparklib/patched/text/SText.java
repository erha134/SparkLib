package io.github.erha134.mc.sparklib.patched.text;

import net.minecraft.text.Text;

public final class SText {
    public static Text literal(String s) {
        return Text.literal(s);
    }

    public static Text translatable(String key) {
        return Text.translatable(key);
    }

    public static Text translatable(String key, Object... args) {
        return Text.translatable(key, args);
    }
}
